package com.lww.mina.config;

import javax.annotation.Resource;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lww
 * @date 2020-06-16 00:21
 */
@Configuration
@EnableConfigurationProperties(MinaServerProperty.class)
public class MinaServerConfig {

    @Resource
    private MinaServerProperty config;

    /**
     * 配置mina的多线程过滤器
     */
    @Bean
    public ExecutorFilter executorFilter() {
        //设置初始化线程数，最大线程数
        return new ExecutorFilter(config.getCorePoolSize(), config.getMaximumPoolSize());
    }

    /**
     * 编解码器filter
     */
    //@Bean
    //public ProtocolCodecFilter protocolCodecFilter() {
    //    //return new ProtocolCodecFilter(new MessageProtocolCodecFactory());
    //}

    /**
     * 配置mina的日志过滤器
     */
    @Bean
    public LoggingFilter loggingFilter() {
        return new LoggingFilter();
    }

    /**
     * 心跳检测
     */
    //@Bean
    //public ServerKeepAliveFactoryImpl keepAliveFactoryImpl() {
    //    return new ServerKeepAliveFactoryImpl();
    //}

    /**
     * 心跳filter
     */
    //@Bean
    //public KeepAliveFilter keepAliveFilter(ServerKeepAliveFactoryImpl keepAliveFactory) {
    //    // 注入心跳工厂，读写空闲
    //    KeepAliveFilter filter = new KeepAliveFilter(keepAliveFactory, IdleStatus.BOTH_IDLE);
    //    // 设置是否forward到下一个filter
    //    filter.setForwardEvent(true);
    //    // 设置心跳频率 5秒一次
    //    filter.setRequestInterval(Const.HEART_BEAT_RATE);
    //    return filter;
    //}

    /**
     * 将过滤器注入到mina的链式管理器中
     */
    //@Bean
    //public DefaultIoFilterChainBuilder defaultIoFilterChainBuilder(ExecutorFilter executorFilter,
    //        LoggingFilter loggingFilter, ProtocolCodecFilter protocolCodecFilter, KeepAliveFilter keepAliveFilter) {
    //    DefaultIoFilterChainBuilder chainBuilder = new DefaultIoFilterChainBuilder();
    //    Map<String, IoFilter> filters = new LinkedHashMap<>();
    //    //多线程过滤器
    //    filters.put("executor", executorFilter);
    //    //日志
    //    filters.put("logger", loggingFilter);
    //    //编码 解码
    //    filters.put("codec", protocolCodecFilter);
    //    //心跳
    //    filters.put("keepAliveFilter", keepAliveFilter);
    //    chainBuilder.setFilters(filters);
    //    return chainBuilder;
    //}

    /**
     * 开启mina的server服务，并设置对应的参数
     */
    //@Bean
    //public IoAcceptor ioAcceptor(DefaultIoFilterChainBuilder filterChainBuilder) throws IOException {
    //    IoAcceptor acceptor = new NioSocketAcceptor();
    //    //设置缓冲区大小
    //    acceptor.getSessionConfig().setReadBufferSize(config.getReadBufferSize());
    //    //设置空闲状态时间，10秒没操作就进入空闲状态
    //    acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, config.getIdelTimeOut());
    //    //过滤器链
    //    acceptor.setFilterChainBuilder(filterChainBuilder);
    //    //处理器 这个 handler 处理所有的连接事件
    //    acceptor.setHandler(new ServerHandler());
    //    //绑定地址
    //    acceptor.bind(new InetSocketAddress(config.getAddress(), config.getPort()));
    //    return acceptor;
    //}
}
