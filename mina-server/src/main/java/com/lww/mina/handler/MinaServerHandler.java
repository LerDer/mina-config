package com.lww.mina.handler;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lww.mina.dao.MessageDao;
import com.lww.mina.domain.Message;
import com.lww.mina.protocol.MessagePack;
import com.lww.mina.session.SessionManager;
import com.lww.mina.util.Const;
import com.lww.mina.util.SpringBeanFactoryUtils;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

/**
 * 处理客户端发送的消息
 *
 * @author lww
 * @date 2020-07-06 22:53
 */
@Slf4j
public class MinaServerHandler extends IoHandlerAdapter {

    @Override
    public void sessionCreated(IoSession session) {
        InetSocketAddress isa = (InetSocketAddress) session.getRemoteAddress();
        String ip = isa.getAddress().getHostAddress();
        session.setAttribute("ip", ip);
        log.info("来自" + ip + " 的终端上线，sessionId：" + session.getId());
    }

    @Override
    public void sessionClosed(IoSession session) {
        log.info(session.getAttribute(Const.SESSION_KEY) + " nid: " + session.getId() + "sessionClosed ");
        // 移除 属性
        session.removeAttribute(Const.SESSION_KEY);
        // 移除超时属性
        session.removeAttribute(Const.TIME_OUT_KEY);
        String key = (String) session.getAttribute(Const.SESSION_KEY);
        if (key != null) {
            SessionManager.removeSession(key);
        }
        session.closeNow();
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) {
        if (session.getAttribute(Const.TIME_OUT_KEY) == null) {
            session.closeNow();
            log.error(session.getAttribute(Const.SESSION_KEY) + " nid: " + session.getId() + " time_out_key null");
            return;
        }
        try {
            int isTimeoutNum = (int) session.getAttribute(Const.TIME_OUT_KEY);
            isTimeoutNum++;
            // 没有超过最大次数，超时次数加1
            if (isTimeoutNum < Const.TIME_OUT_NUM) {
                session.setAttribute(Const.TIME_OUT_KEY, isTimeoutNum);
            } else {
                // 超过最大次数，关闭会话连接
                String key = (String) session.getAttribute(Const.SESSION_KEY);
                // 移除device属性
                session.removeAttribute(Const.SESSION_KEY);
                // 移除超时属性
                session.removeAttribute(Const.TIME_OUT_KEY);
                SessionManager.removeSession(key);
                session.closeOnFlush();
                log.info("client user: " + key + " more than " + Const.TIME_OUT_NUM + " times have no response, connection closed!");
            }
        } catch (Exception e) {
            log.error(session.getAttribute(Const.SESSION_KEY) + " nid: " + session.getId() + e.getMessage());
            session.closeNow();
        }
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {
        log.error("终端用户:{} 连接发生异常，即将关闭连接，原因:{}", session.getAttribute(Const.SESSION_KEY), cause);
    }

    @Override
    public void messageReceived(IoSession session, Object message) {
        SocketAddress remoteAddress = session.getRemoteAddress();
        log.info("server received MinaServerHandler_messageReceived_remoteAddress:{}", remoteAddress);
        MessagePack pack = (MessagePack) message;
        MessagePack response;
        String body = pack.getBody();
        if (StringUtils.isBlank(body)) {
            log.error("ServerHandler_messageReceived_body:{}", body);
            response = new MessagePack(Const.BASE, "body empty");
            session.write(response);
            session.close(false);
            return;
        }
        Message msg = JSONObject.parseObject(body, Message.class);
        if (msg == null) {
            log.error("ServerHandler_messageReceived_body:{}", body);
            response = new MessagePack(Const.BASE, "message empty");
            session.write(response);
            session.close(false);
            return;
        }
        if (Const.CONF.equalsIgnoreCase(msg.getPropertyValue()) && pack.getModule() == Const.BASE) {
            log.info("ServerHandler_messageReceived_Susccess:{}", msg.getPropertyValue());
            response = new MessagePack(pack.getModule(), body);
            session.write(response);
            return;
        }
        final String key = remoteAddress.toString();
        //存储的key
        session.setAttribute(Const.SESSION_KEY, key);
        // 超时次数设为0
        session.setAttribute(Const.TIME_OUT_KEY, 0);
        synchronized (this) {
            IoSession oldSession = SessionManager.getSession(key);
            if (oldSession != null && !oldSession.equals(session)) {
                // 移除key属性
                oldSession.removeAttribute(Const.SESSION_KEY);
                // 移除超时时间
                oldSession.removeAttribute(Const.TIME_OUT_KEY);
                // 替换oldSession
                SessionManager.replaceSession(key, session);
                session.closeOnFlush();
                log.info("oldSession close!");
            }
            if (oldSession == null) {
                SessionManager.addSession(key, session);
            }
            log.info("bind success: " + session.getRemoteAddress());
        }
        MessageDao minaMessageDao = SpringBeanFactoryUtils.getApplicationContext().getBean(MessageDao.class);
        log.info("ServerHandler_messageReceived_projectName:{}, propertityValue:{}, envValue:{}", msg.getProjectName(), msg.getPropertyValue(), msg.getEnvValue());
        Message configMessage = minaMessageDao.selectOne(new QueryWrapper<Message>().lambda()
                .eq(Message::getProjectName, msg.getProjectName())
                .eq(Message::getPropertyValue, msg.getPropertyValue())
                .eq(Message::getEnvValue, msg.getEnvValue()));
        if (configMessage == null && !msg.getPropertyValue().equalsIgnoreCase(Const.CONF)) {
            log.error(session.toString() + "select null");
            response = new MessagePack(Const.BASE, "select error");
            session.write(response);
            session.closeOnFlush();
        } else {
            // 设置session key
            if (configMessage != null) {
                configMessage.setRemoteAddress(key);
                // AR模式
                boolean updateSessionKey = configMessage.updateById();
                log.info("ServerHandler_messageReceived_updateSessionKey:{}", updateSessionKey);
            }
            log.info(session.toString() + " succeed!");
            response = new MessagePack(pack.getModule(), JSONObject.toJSONString(configMessage));
            session.write(response);
        }
    }

    @Override
    public void messageSent(IoSession session, Object message) {
        if (message instanceof Message) {
            Message minaMessage = (Message) message;
            session.write(new MessagePack(Const.CONFIG_MANAGE, JSONObject.toJSONString(minaMessage)));
        }
        session.setAttribute(Const.TIME_OUT_KEY, 0);
        log.info("发送消息成功");
    }

}
