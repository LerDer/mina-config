package com.lww.mina.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 消息体
 * </p>
 *
 * @author lww
 * @since 2020-07-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Message对象", description = "消息体")
public class Message extends Model<Message> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "消息体id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "环境，dev日常，gray灰度，online线上，local本地")
    private String envValue;

    @ApiModelProperty(value = "preperties 中的value")
    private String propertyValue;

    @ApiModelProperty(value = "配置中心配置的需要注入的值")
    private String configValue;

    @ApiModelProperty(value = "客户端 session key")
    private String remoteAddress;

    @ApiModelProperty(value = "创建人")
    private Long creator;

    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;

    @ApiModelProperty(value = "修改人")
    private Long modifier;

    @ApiModelProperty(value = "修改时间")
    private Date gmtModify;

    @ApiModelProperty(value = "乐观锁，版本")
    private Integer configVersion;

    @ApiModelProperty(value = "逻辑删除，0未删除，1已删除")
    private Integer isDeleted;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
