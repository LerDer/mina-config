package com.lww.mina.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.mina.dao.MessageDao;
import com.lww.mina.domain.Message;
import com.lww.mina.event.MessageEvent;
import com.lww.mina.service.MessageService;
import com.lww.mina.util.SpringBeanFactoryUtils;
import com.lww.mina.vo.MessageVO;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * <p>
 * 消息体 服务实现类
 * </p>
 *
 * @author lww
 * @since 2020-07-05
 */
@Slf4j
@Service
public class MessageServiceImpl extends ServiceImpl<MessageDao, Message> implements MessageService {

    @Resource
    private MessageDao messageDao;

    @Override
    public void addMessage(MessageVO vo) {
        final String projectName = vo.getProjectName();
        final String propertyValue = vo.getPropertyValue();
        final String configValue = vo.getConfigValue();
        final String envValue = vo.getEnvValue();
        log.info("MessageServiceImpl_addMessage_projectName:{}, propertyValue:{}, configValue:{}",
                projectName, propertyValue, configValue);

        List<Message> messages = messageDao.selectList(new QueryWrapper<Message>().lambda()
                .eq(Message::getProjectName, projectName)
                .eq(Message::getPropertyValue, propertyValue)
                .eq(Message::getEnvValue, envValue));
        Assert.isTrue(messages.size() <= 0, "存在重复配置！");

        Message message = new Message();
        message.setProjectName(projectName);
        message.setEnvValue(envValue);
        message.setPropertyValue(propertyValue);
        message.setConfigValue(configValue);
        //message.setCreator();
        boolean insertMessage = message.insert();
        log.info("MessageServiceImpl_addMessage_insertMessage:{}", insertMessage);
        Assert.isTrue(insertMessage, "保存配置信息失败！");
    }

    @Override
    public void editMessage(Long messageId, String configValue) {
        Message message = messageDao.selectById(messageId);
        Assert.isTrue(message != null, "查询配置结果为空！");
        Assert.isTrue(message.getIsDeleted() == 0, "配置已删除！");
        Assert.isTrue(!message.getConfigValue().equalsIgnoreCase(configValue), "相同配置，无需更新！");
        int updateMessage = messageDao.update(new Message(), new UpdateWrapper<Message>().lambda()
                .eq(Message::getId, messageId)
                .eq(Message::getConfigVersion, message.getConfigVersion())
                .set(Message::getConfigValue, configValue)
                .set(Message::getConfigVersion, message.getConfigVersion() + 1));
        //.set(Message::getModifier, ));
        log.info("MessageServiceImpl_editMessage_updateMessage:{}", updateMessage);
        Assert.isTrue(updateMessage == 1, "更新失败，请稍后重试！");
        Message afterMessage = messageDao.selectById(messageId);
        SpringBeanFactoryUtils.getApplicationContext().publishEvent(new MessageEvent(afterMessage));
    }
}
