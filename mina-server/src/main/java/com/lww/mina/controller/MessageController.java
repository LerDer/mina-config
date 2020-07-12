package com.lww.mina.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lww.mina.domain.Message;
import com.lww.mina.result.HttpResult;
import com.lww.mina.service.MessageService;
import com.lww.mina.vo.MessageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 消息体 前端控制器
 * </p>
 *
 * @author lww
 * @since 2020-07-05
 */
@Api(value = "/message", tags = "配置信息相关")
@RestController
@RequestMapping("/message")
@Slf4j
public class MessageController {

    @Resource
    private MessageService messageService;

    @ApiOperation("添加配置信息")
    @PostMapping(value = "/add", name = "添加配置信息")
    public HttpResult add(@RequestBody MessageVO vo) {
        Assert.isTrue(vo != null, "参数错误！");
        Assert.isTrue(StringUtils.isNotBlank(vo.getProjectName()), "项目名称不能为空！");
        Assert.isTrue(StringUtils.isNotBlank(vo.getPropertyValue()), "配置的值的不能为空，对应properties文件中的值！");
        Assert.isTrue(StringUtils.isNotBlank(vo.getConfigValue()), "项目中要注入的值不能为空！");
        messageService.addMessage(vo);
        return HttpResult.success();
    }

    @ApiOperation("修改配置信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "messageId", value = "消息id", required = true),
            @ApiImplicitParam(name = "configValue", value = "项目中要注入的值", required = true),
    })
    @PostMapping(value = "/edit", name = "修改配置信息")
    public HttpResult edit(Long messageId, String configValue) {
        log.info("MessageController_edit_messageId:{}, configValue:{}", messageId, configValue);
        messageService.editMessage(messageId, configValue);
        return HttpResult.success();
    }

    @ApiOperation("获取单个配置信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectName", value = "项目名称", required = true),
            @ApiImplicitParam(name = "env", value = "环境", required = true),
            @ApiImplicitParam(name = "propertyValue", value = "application.properties 配置的值", required = true),
    })
    @GetMapping(value = "/conf", name = "获取配置信息")
    public Message conf(String projectName, String env, String propertyValue) {
        Message message = messageService.getOne(new QueryWrapper<Message>().lambda()
                .eq(Message::getProjectName, projectName)
                .eq(Message::getEnvValue, env)
                .eq(Message::getPropertyValue, propertyValue)
                .eq(Message::getIsDeleted, 0));
        Assert.isTrue(message != null, "查询结果为空！");
        return message;
    }
}
