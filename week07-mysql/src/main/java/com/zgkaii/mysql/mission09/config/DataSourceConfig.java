package com.zgkaii.mysql.mission09.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;

/**
 * @Author: Mr.Z
 * @DateTime: 2021/05/13 15:08
 * @Description:
 **/
@Configuration
@MapperScan(basePackages = "com.zgkaii.mysql.mission09.mapper", sqlSessionFactoryRef = "sqlSessionFactory")
public class DataSourceConfig {

    @Value("${mysql.datasource.mapper-locations}")
    private String mapperLocation;

    @Value("${mysql.datasource.config-location}")
    private String configLocation;

    /**
     * 写数据源
     *
     * @Primary 标志这个 Bean 如果在多个同类 Bean 候选时，该 Bean 优先被考虑。
     * 多数据源配置的时候注意，必须要有一个主数据源，用 @Primary 标志该 Bean。
     */
    @Primary
    @Bean
    @ConfigurationProperties(prefix = "mysql.datasource.write")
    public DataSource writeDataSource() {
        return new DruidDataSource();
    }

    /**
     * 读数据源
     */
    @Bean
    @ConfigurationProperties(prefix = "mysql.datasource.read")
    public DataSource read1() {
        return new DruidDataSource();
    }

    /**
     * 3个数据源 一个write、一个read、一个路由数据源，后续只用到路由数据源
     */
    @Bean
    public AbstractRoutingDataSource routingDataSource() {
        MyAbstractRoutingDataSource myRoutingDataSource = new MyAbstractRoutingDataSource();
        HashMap<Object, Object> targetDataSources = new HashMap<>(2);
        targetDataSources.put(DbContextHolder.WRITE, writeDataSource());
        targetDataSources.put(DbContextHolder.READ + "1", read1());
        // 后续可以再加从库
        myRoutingDataSource.setDefaultTargetDataSource(writeDataSource());
        myRoutingDataSource.setTargetDataSources(targetDataSources);
        return myRoutingDataSource;
    }

    /**
     * 为SqlSessionFactory指定数据源
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(routingDataSource());
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        // mybatis的xml的配置
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources(mapperLocation));
        sqlSessionFactoryBean.setConfigLocation(resolver.getResource(configLocation));
        return sqlSessionFactoryBean.getObject();
    }

    /**
     * 为事务管理器制定数据源
     */
    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager() {
        return new DataSourceTransactionManager(routingDataSource());
    }
}
