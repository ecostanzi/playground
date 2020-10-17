package me.pruivo;

import org.infinispan.Cache;
import org.infinispan.commons.api.CacheContainerAdmin;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.distribution.ch.KeyPartitioner;
import org.infinispan.factories.ComponentRegistry;
import org.infinispan.jboss.marshalling.core.JBossUserMarshaller;
import org.infinispan.manager.DefaultCacheManager;

public class Commons {

    static KeyPartitioner keyPartitioner(Cache<MyKey, String> cache) {
        ComponentRegistry cr = cache.getAdvancedCache().getComponentRegistry();
        return cr.getComponent(KeyPartitioner.class);
    }

    static Cache<MyKey, String> cache(DefaultCacheManager cacheManager) {
        return cacheManager.administration()
                .withFlags(CacheContainerAdmin.AdminFlag.VOLATILE)
                .getOrCreateCache("test", cacheConfiguration());
    }

    static GlobalConfiguration defaultGlobalConfiguration() {
        //by default, IP multicast is used to find the nodes.
        GlobalConfigurationBuilder builder = new GlobalConfigurationBuilder().clusteredDefault();
        //use jboss-marshaller to use java.io.Serializable interface
        builder.serialization().marshaller(new JBossUserMarshaller());
        builder.serialization().whiteList().addClass(MyKey.class.getName());
        return builder.build();
    }

    static Configuration cacheConfiguration() {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.clustering().cacheMode(CacheMode.REPL_ASYNC);
        return builder.build();
    }
}
