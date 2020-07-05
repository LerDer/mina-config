package com.lww.mina.service.impl;

import com.lww.mina.domain.Message;
import com.lww.mina.dao.MessageDao;
import com.lww.mina.service.MessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 消息体 服务实现类
 * </p>
 *
 * @author lww
 * @since 2020-07-05
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageDao, Message> implements MessageService {

}
