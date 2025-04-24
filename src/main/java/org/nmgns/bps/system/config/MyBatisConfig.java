package org.nmgns.bps.system.config;

import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.nmgns.bps.system.utils.PgArrayToLongListTypeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisConfig {

    @Bean
    public PgArrayToLongListTypeHandler pgArrayToLongListTypeHandler() {
        return new PgArrayToLongListTypeHandler();
    }

    @Bean
    public ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return configuration -> {
            // 注册TypeHandler
            configuration.getTypeHandlerRegistry().register(PgArrayToLongListTypeHandler.class);

            // 或者指定具体类型
            // configuration.getTypeHandlerRegistry().register(List.class, JdbcType.ARRAY, pgArrayToLongListTypeHandler());
        };
    }


}
