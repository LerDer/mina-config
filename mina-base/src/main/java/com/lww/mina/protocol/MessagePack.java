package com.lww.mina.protocol;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author lww
 * @date 2020-07-05 17:47
 */
@Data
public class MessagePack {

    /**
     * 数据总长度
     */
    private int len;

    /**
     * 模块代码
     */
    private int module;

    /**
     * 包体  Message json格式
     */
    private String body;

    /**
     * 包头长度
     */
    public static final int PACK_HEAD_LEN = 8;

    /**
     * 最大长度
     */
    public static final int MAX_LEN = 9999;

    public MessagePack(int module, String body) {
        this.module = module;
        this.body = body;
        // 总长度
        this.len = PACK_HEAD_LEN + (StringUtils.isBlank(body) ? 0 : body.getBytes().length);
    }
}
