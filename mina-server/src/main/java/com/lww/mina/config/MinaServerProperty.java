package com.lww.mina.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lww
 * @date 2020-07-05 16:13
 */
@ConfigurationProperties(prefix = "mina.server")
public class MinaServerProperty {

    /**
     * 服务器监听端口，默认 9123
     */
    private Integer port = 9123;

    /**
     * 服务器ip地址，默认 127.0.0.1
     */
    private String address = "127.0.0.1";

    /**
     * 缓冲区大小，默认2048
     */
    private Integer readBufferSize = 2048;

    /**
     * 空闲时间，单位秒 默认 5 秒没操作就进入空闲状态
     */
    private Integer idelTimeOut = 5;

    /**
     * 初始化线程池大小，默认10
     */
    private Integer corePoolSize = 10;

    /**
     * 最大线程数，默认20
     */
    private Integer maximumPoolSize = 20;

    /**
     * 初始化用户名
     */
    private String username;

    /**
     * 初始化密码
     */
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getReadBufferSize() {
        return readBufferSize;
    }

    public void setReadBufferSize(Integer readBufferSize) {
        this.readBufferSize = readBufferSize;
    }

    public Integer getIdelTimeOut() {
        return idelTimeOut;
    }

    public void setIdelTimeOut(Integer idelTimeOut) {
        this.idelTimeOut = idelTimeOut;
    }

    public Integer getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public Integer getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(Integer maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }
}
