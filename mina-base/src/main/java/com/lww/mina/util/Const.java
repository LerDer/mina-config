package com.lww.mina.util;

/**
 * @author lww
 */
public interface Const {

    /**
     * 心跳时间，单位秒
     */
    int HEART_BEAT_RATE = 5;

    /**
     * session_key采用设备编号
     */
    String SESSION_KEY = "sessionKey";

    /**
     * 超时KEY
     */
    String TIME_OUT_KEY = "timeOut";

    /**
     * 超时次数
     */
    int TIME_OUT_NUM = 3;

    /**
     * 基本
     */
    int BASE = 1;

    /**
     * 配置管理
     */
    int CONFIG_MANAGE = 2;

    /**
     * 心跳包
     */
    int HEART_BEAT = 3;

    String PLACEHOLDER_PREFIX = "${";

    String PLACEHOLDER_SUFFIX = "}";

    String VALUE_SEPARATOR = ":";

    String CONF = "mina.config";
}
