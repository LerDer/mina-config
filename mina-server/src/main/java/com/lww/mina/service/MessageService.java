package com.lww.mina.service;

import com.lww.mina.domain.Message;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lww.mina.vo.MessageVO;

/**
 * <p>
 * 消息体 服务类
 * </p>
 *
 * @author lww
 * @since 2020-07-05
 */
public interface MessageService extends IService<Message> {

    void addMessage(MessageVO vo);

    void editMessage(Long messageId, String configValue);
}
