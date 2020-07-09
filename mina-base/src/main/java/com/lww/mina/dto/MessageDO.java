package com.lww.mina.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author lww
 * @date 2020-07-09 11:08
 */
@Data
public class MessageDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息体id
     */
    private Long id;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 环境，dev日常，gray灰度，online线上，local本地
     */
    private String envValue;

    /**
     * preperties 中的value
     */
    private String propertyValue;

    /**
     * 配置中心配置的需要注入的值
     */
    private String configValue;

    /**
     * 客户端 session key
     */
    private String remoteAddress;

    /**
     * 创建人
     */
    private Long creator;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改人
     */
    private Long modifier;

    /**
     * 修改时间
     */
    private Date gmtModify;

    /**
     * 乐观锁，版本
     */
    private Integer configVersion;

    /**
     * 逻辑删除，0未删除，1已删除
     */
    private Integer isDeleted;

}
