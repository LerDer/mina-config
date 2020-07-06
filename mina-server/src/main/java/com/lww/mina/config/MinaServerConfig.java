package com.lww.mina.config;

import com.lww.mina.filter.ServerKeepAliveFactoryImpl;
import com.lww.mina.handler.MinaServerHandler;
import com.lww.mina.protocol.MessageProtocolCodecFactory;
import com.lww.mina.util.Const;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lww
 * @date 2020-07-05 16:13
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
     * 配置mina的日志过滤器
     */
    @Bean
    public LoggingFilter loggingFilter() {
        return new LoggingFilter();
    }

    /**
     * 编解码器filter
     */
    @Bean
    public ProtocolCodecFilter protocolCodecFilter() {
        return new ProtocolCodecFilter(new MessageProtocolCodecFactory());
    }

    /**
     * 心跳检测
     */
    @Bean
    public ServerKeepAliveFactoryImpl keepAliveFactoryImpl() {
        return new ServerKeepAliveFactoryImpl();
    }

    /**
     * 心跳filter
     */
    @Bean
    public KeepAliveFilter keepAliveFilter(ServerKeepAliveFactoryImpl keepAliveFactory) {
        // 注入心跳工厂，读写空闲
        KeepAliveFilter filter = new KeepAliveFilter(keepAliveFactory, IdleStatus.BOTH_IDLE);
        // 设置是否forward到下一个filter
        filter.setForwardEvent(true);
        // 设置心跳频率 5秒一次
        filter.setRequestInterval(Const.HEART_BEAT_RATE);
        return filter;
    }

    /**
     * 将过滤器注入到mina的链式管理器中
     */
    @Bean
    public DefaultIoFilterChainBuilder defaultIoFilterChainBuilder(ExecutorFilter executorFilter,
            LoggingFilter loggingFilter, ProtocolCodecFilter protocolCodecFilter, KeepAliveFilter keepAliveFilter) {
        DefaultIoFilterChainBuilder chainBuilder = new DefaultIoFilterChainBuilder();
        Map<String, IoFilter> filters = new LinkedHashMap<>();
        //多线程过滤器
        filters.put("executor", executorFilter);
        //日志
        filters.put("logger", loggingFilter);
        //编码 解码
        filters.put("codec", protocolCodecFilter);
        //心跳
        filters.put("keepAliveFilter", keepAliveFilter);
        chainBuilder.setFilters(filters);
        return chainBuilder;
    }

    /**
     * 开启mina的server服务，并设置对应的参数
     */
    @Bean
    public IoAcceptor ioAcceptor(DefaultIoFilterChainBuilder filterChainBuilder) throws IOException {
        IoAcceptor acceptor = new NioSocketAcceptor();
        //设置缓冲区大小
        acceptor.getSessionConfig().setReadBufferSize(config.getReadBufferSize());
        //设置空闲状态时间，10秒没操作就进入空闲状态
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, config.getIdelTimeOut());
        //过滤器链
        acceptor.setFilterChainBuilder(filterChainBuilder);
        //处理器 这个 handler 处理所有的连接事件
        acceptor.setHandler(new MinaServerHandler());
        //绑定地址
        acceptor.bind(new InetSocketAddress(config.getAddress(), config.getPort()));
        return acceptor;
    }
}
