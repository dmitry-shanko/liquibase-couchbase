package org.liquibase.ext.couchbase.starter.configuration;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;

import liquibase.Liquibase;
import liquibase.Scope;
import liquibase.configuration.ConfiguredValue;
import liquibase.database.Database;
import liquibase.ext.couchbase.database.CouchbaseLiquibaseDatabase;
import liquibase.integration.commandline.LiquibaseCommandLineConfiguration;
import liquibase.integration.spring.SpringResourceAccessor;
import liquibase.logging.Logger;
import lombok.Setter;

public class CouchbaseLiquibase implements InitializingBean, BeanNameAware, ResourceLoaderAware {

    private Logger log = Scope.getCurrentScope().getLog(getClass());

    @Setter
    private ResourceLoader resourceLoader;
    @Setter
    private String beanName;

    @Override
    public void afterPropertiesSet() throws Exception {
        final ConfiguredValue<Boolean> shouldRunProperty = LiquibaseCommandLineConfiguration.SHOULD_RUN.getCurrentConfiguredValue();

        if (!shouldRunProperty.getValue()) {
            log.info("Liquibase did not run because " + shouldRunProperty.getProvidedValue().describe() + " was set to false");
            return;
        }

        try (Liquibase liquibase = createLiquibase()) {
            liquibase.update("classpath:config/liquibase/master.xml");
        }
    }

    private Liquibase createLiquibase() {
        SpringResourceAccessor resourceOpener = createResourceOpener();
        Database db = new CouchbaseLiquibaseDatabase();
        return new Liquibase("classpath:config/liquibase/master.xml", resourceOpener, db);
    }

    protected SpringResourceAccessor createResourceOpener() {
        return new SpringResourceAccessor(resourceLoader);
    }
}