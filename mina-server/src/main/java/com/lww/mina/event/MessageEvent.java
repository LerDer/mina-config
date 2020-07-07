package com.lww.mina.event;

import com.lww.mina.domain.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;

/**
 * 消息更新事件
 *
 * @author lww
 * @date 2020-07-07 00:28
 */
@Slf4j
public class MessageEvent extends ApplicationEvent {

    private Message message;

    public MessageEvent(Message message) {
        super(message);
        log.info("发布消息 MessageEvent_MessageEvent_message:{}", message);
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
