package org.example.smallredbook.kv.biz.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;

/**
 * @Author: tzy
 * @Description: Cassandra 配置类 => 读取配置文件中的内容
 * @Date: 2024/9/3 19:56
 */
@Configuration
public class CassandraConfig extends AbstractCassandraConfiguration {
    @Value("${spring.cassandra.keyspace-name}")
    private String keySpace;
    @Value("${spring.cassandra.contact-points}")
    private String contactPoints;
    @Value("${spring.cassandra.port}")
    private int port;

    @Override
    protected String getKeyspaceName() {
        return keySpace;
    }
    @Override
    public String getContactPoints() {
        return contactPoints;
    }

    @Override
    public int getPort() {
        return port;
    }
}
