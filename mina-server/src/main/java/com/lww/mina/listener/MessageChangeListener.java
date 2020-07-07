package com.lww.mina.listener;

import com.lww.mina.domain.Message;
import com.lww.mina.event.MessageEvent;
import com.lww.mina.handler.MinaServerHandler;
import com.lww.mina.session.SessionManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.session.IoSession;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author lww
 * @date 2020-07-07 00:30
 */
@Slf4j
@Component
public class MessageChangeListener {

    @EventListener
    public void onApplicationEvent(MessageEvent event) {
        log.info("接收事件 MessageChangeListener_onApplicationEvent_event:{}", event);
        //推送配置
        Message message = event.getMessage();
        Assert.isTrue(StringUtils.isNotBlank(message.getRemoteAddress()), "初始配置无法发送配置信息，需要客户端连接一次后，获取客户端地址端口等信息！");
        try {
            IoSession session = SessionManager.getSession(message.getRemoteAddress());
            if (session != null) {
                MinaServerHandler handler = new MinaServerHandler();
                handler.messageSent(session, message);
            }
        } catch (Exception e) {
            log.info("MessageChangeListener_onApplicationEvent_e:{}", e);
        }
    }
}
