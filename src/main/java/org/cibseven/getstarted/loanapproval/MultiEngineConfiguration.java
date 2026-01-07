/*
 * Copyright CIB software GmbH and/or licensed to CIB software GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. CIB software licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.cibseven.getstarted.loanapproval;

import javax.sql.DataSource;

import org.cibseven.bpm.engine.ProcessEngine;
import org.cibseven.bpm.engine.ProcessEngineConfiguration;
import org.cibseven.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.cibseven.bpm.spring.boot.starter.property.CamundaBpmProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class MultiEngineConfiguration {
    
    private final ApplicationContext applicationContext;
    
    public MultiEngineConfiguration(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
    
    @Bean
    public CamundaBpmProperties camundaBpmProperties() {
        return new CamundaBpmProperties();
    }
    
    @Bean(name = "defaultDataSource")
    public DataSource defaultDataSource() {
        return DataSourceBuilder.create()
            .type(com.zaxxer.hikari.HikariDataSource.class)
            .driverClassName("org.h2.Driver")
            .url("jdbc:h2:mem:default;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE")
            .username("sa")
            .password("")
            .build();
    }
    
    @Bean(name = "tenant1DataSource")
    public DataSource tenant1DataSource() {
        return DataSourceBuilder.create()
            .type(com.zaxxer.hikari.HikariDataSource.class)
            .driverClassName("org.h2.Driver")
            .url("jdbc:h2:mem:tenant1;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE")
            .username("sa")
            .password("")
            .build();
    }

    @Bean(name = "tenant2DataSource")
    public DataSource tenant2DataSource() {
        return DataSourceBuilder.create()
            .type(com.zaxxer.hikari.HikariDataSource.class)
            .driverClassName("org.h2.Driver")
            .url("jdbc:h2:mem:tenant2;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE")
            .username("sa")
            .password("")
            .build();
    }

    @Bean(name = "tenant3DataSource")
    public DataSource tenant3DataSource() {
        return DataSourceBuilder.create()
            .type(com.zaxxer.hikari.HikariDataSource.class)
            .driverClassName("org.h2.Driver")
            .url("jdbc:h2:mem:tenant3;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE")
            .username("sa")
            .password("")
            .build();
    }

    @Bean(name = "defaultTransactionManager")
    public PlatformTransactionManager defaultTransactionManager(@Qualifier("defaultDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
    
    @Bean(name = "tenant1TransactionManager")
    public PlatformTransactionManager tenant1TransactionManager(@Qualifier("tenant1DataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "tenant2TransactionManager")
    public PlatformTransactionManager tenant2TransactionManager(@Qualifier("tenant2DataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "tenant3TransactionManager")
    public PlatformTransactionManager tenant3TransactionManager(@Qualifier("tenant3DataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
    
    @Bean(name = "processEngineDefault")
    @Primary
    public ProcessEngine processEngineDefault(
            @Qualifier("defaultDataSource") DataSource tenant1DataSource,
            @Qualifier("defaultTransactionManager") PlatformTransactionManager transactionManager) {
        SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();
        config.setApplicationContext(applicationContext);
        config.setDataSource(tenant1DataSource);
        config.setTransactionManager(transactionManager);
        config.setProcessEngineName("default");
        config.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        return config.buildProcessEngine();
    }

    @Bean(name = "processEngineTenant1")
    public ProcessEngine processEngineTenant1(
            @Qualifier("tenant1DataSource") DataSource tenant1DataSource,
            @Qualifier("tenant1TransactionManager") PlatformTransactionManager transactionManager) {
        SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();
        config.setApplicationContext(applicationContext);
        config.setDataSource(tenant1DataSource);
        config.setTransactionManager(transactionManager);
        config.setProcessEngineName("engine1");
        config.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        return config.buildProcessEngine();
    }

    @Bean(name = "processEngineTenant2")
    public ProcessEngine processEngineTenant2(
            @Qualifier("tenant2DataSource") DataSource tenant2DataSource,
            @Qualifier("tenant2TransactionManager") PlatformTransactionManager transactionManager) {
        SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();
        config.setApplicationContext(applicationContext);
        config.setDataSource(tenant2DataSource);
        config.setTransactionManager(transactionManager);
        config.setProcessEngineName("engine2");
        config.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        return config.buildProcessEngine();
    }

    @Bean(name = "processEngineTenant3")
    public ProcessEngine processEngineTenant3(
            @Qualifier("tenant3DataSource") DataSource tenant3DataSource,
            @Qualifier("tenant3TransactionManager") PlatformTransactionManager transactionManager) {
        SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();
        config.setApplicationContext(applicationContext);
        config.setDataSource(tenant3DataSource);
        config.setTransactionManager(transactionManager);
        config.setProcessEngineName("engine3");
        config.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        return config.buildProcessEngine();
    }
}
