package com.lww.mina.controller;

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

}
