package com.lww.mina.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 消息体
 * </p>
 *
 * @author lww
 * @since 2020-06-14
 */
@Data
@ApiModel(value = "Message对象", description = "消息体")
public class MessageVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "环境，dev日常，gray灰度，online线上，local本地")
    private String envValue;

    @ApiModelProperty(value = "配置key")
    private String propertyValue;

    @ApiModelProperty(value = "配置的值")
    private String configValue;

}
