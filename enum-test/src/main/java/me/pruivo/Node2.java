package me.pruivo;

import org.infinispan.Cache;
import org.infinispan.distribution.ch.KeyPartitioner;
import org.infinispan.manager.DefaultCacheManager;

import java.io.IOException;

import static me.pruivo.Commons.*;

/**
 * Node 2. Just receives entries and listen for events
 */
public class Node2 {

   public static void main(String[] args) throws IOException, InterruptedException {
      try (DefaultCacheManager cacheManager = new DefaultCacheManager(defaultGlobalConfiguration())) {
         Cache<MyKey, String> cache = cache(cacheManager);
         KeyPartitioner keyPartitioner = keyPartitioner(cache);
         cache.addListener(new MyListener(keyPartitioner));

         System.out.println("\nNode 2 started. Press ENTER to exit!");
         System.in.read();
      }

      System.exit(0);
   }

}
