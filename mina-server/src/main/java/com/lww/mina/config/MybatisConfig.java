package com.lww.mina.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Mybatis-Plus配置类
 *
 * @author lww
 * @date 2020-07-03 01:25
 */
@Slf4j
@Configuration
@MapperScan(basePackages = "com.lww.mina.dao", sqlSessionTemplateRef = "sqlSessionTemplate")
public class MybatisConfig {

    private static final String BASE_PACKAGE = "com.lww.mina.";

    @Bean("dataSource")
    public DataSource dataSource() {
        try {
            DruidDataSource dataSource = new DruidDataSource();
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/mconfig?useUnicode=true&useSSL=false&characterEncoding=utf8");
            dataSource.setUsername("root");
            dataSource.setPassword("adminadmin");

            dataSource.setInitialSize(1);
            dataSource.setMaxActive(20);
            dataSource.setMinIdle(1);
            dataSource.setMaxWait(60_000);
            dataSource.setPoolPreparedStatements(true);
            dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
            dataSource.setTimeBetweenEvictionRunsMillis(60_000);
            dataSource.setMinEvictableIdleTimeMillis(300_000);
            dataSource.setValidationQuery("SELECT 1");
            return dataSource;
        } catch (Throwable throwable) {
            log.error("ex caught", throwable);
            throw new RuntimeException();
        }
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setVfs(SpringBootVFS.class);
        factoryBean.setTypeAliasesPackage(BASE_PACKAGE + "domain");

        Resource[] mapperResources = new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/*.xml");
        factoryBean.setMapperLocations(mapperResources);

        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);
        //分页
        configuration.addInterceptor(new PaginationInterceptor());
        //打印sql
        configuration.addInterceptor(new PerformanceInterceptor());

        configuration.setUseGeneratedKeys(true);
        factoryBean.setConfiguration(configuration);
        return factoryBean.getObject();
    }

    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager platformTransactionManager(@Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "transactionTemplate")
    public TransactionTemplate transactionTemplate(@Qualifier("transactionManager") PlatformTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);
    }

}
