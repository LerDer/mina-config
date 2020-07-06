package com.lww.mina.filter;

import com.alibaba.fastjson.JSONObject;
import com.lww.mina.protocol.MessagePack;
import com.lww.mina.util.Const;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;

/**
 * 被动型心跳机制
 *
 * @author lww
 * @date 2020-07-06 22:40
 */
@Slf4j
public class ServerKeepAliveFactoryImpl implements KeepAliveMessageFactory {

    /**
     * 用来判断接收到的消息是不是一个心跳请求包，是就返回true[接收端使用]
     */
    @Override
    public boolean isRequest(IoSession session, Object message) {
        if (message instanceof MessagePack) {
            MessagePack pack = (MessagePack) message;
            if (pack.getModule() == Const.HEART_BEAT) {
                log.info("收到 心跳请求 ServerKeepAliveFactoryImpl_isRequest_pack:{}", JSONObject.toJSONString(message));
                return true;
            }
        }
        return false;
    }

    /**
     * 用来判断接收到的消息是不是一个心跳回复包，是就返回true[发送端使用]
     */
    @Override
    public boolean isResponse(IoSession session, Object message) {
        return false;
    }

    /**
     * 在需要发送心跳时，用来获取一个心跳请求包[发送端使用]
     */
    @Override
    public Object getRequest(IoSession session) {
        return null;
    }

    /**
     * 在需要回复心跳时，用来获取一个心跳回复包[接收端使用]
     */
    @Override
    public Object getResponse(IoSession session, Object message) {
        MessagePack pack = (MessagePack) message;
        // 将超时次数置为0
        session.setAttribute(Const.TIME_OUT_KEY, 0);
        log.info("响应 心跳请求 ServerKeepAliveFactoryImpl_getResponse_request:{}", JSONObject.toJSONString(message));
        return new MessagePack(Const.HEART_BEAT, "heart");
    }
}