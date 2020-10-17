package me.pruivo;

import org.infinispan.Cache;
import org.infinispan.distribution.ch.KeyPartitioner;
import org.infinispan.manager.DefaultCacheManager;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static me.pruivo.Commons.*;

/**
 * Puts entries cache every 5 seconds. The cache key contains enums.
 *
 */
public class Node1 {

   public static void main(String[] args) throws IOException, InterruptedException {
      try (DefaultCacheManager cacheManager = new DefaultCacheManager(defaultGlobalConfiguration())) {
         Cache<MyKey, String> cache = cache(cacheManager);
         KeyPartitioner keyPartitioner = keyPartitioner(cache);
         cache.addListener(new MyListener(keyPartitioner));

         System.out.println("\nPress ENTER to start after having th second node running");
         System.in.read();

         for (int i = 0; i < 100; i++) {
            int index = new Random().nextInt(TimeUnit.values().length);
            MyKey key = new MyKey(TimeUnit.values()[index]);
            String uuid = UUID.randomUUID().toString();
            int segment = keyPartitioner.getSegment(key);
            System.out.println(String.format("Inserting %s=%s (segment %d)", key, uuid, segment));
            cache.put(key, uuid);
            Thread.sleep(5000);
         }

         System.out.println("\nPress ENTER to exit!");
         System.in.read();
      }

      System.exit(0);
   }



}
