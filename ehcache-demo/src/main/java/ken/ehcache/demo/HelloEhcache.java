package ken.ehcache.demo;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;

public class HelloEhcache {

    public static void main(String[] args)
    {
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("preConfigured",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
                                        ResourcePoolsBuilder.heap(100))
                                .build())
                .build(true);

        Cache<Long, String> preConfigured
                = cacheManager.getCache("preConfigured", Long.class, String.class);

        Cache<String, String> anotherCache = cacheManager.createCache("myCache",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class,
                        ResourcePoolsBuilder.heap(10).offheap(10, MemoryUnit.MB)).build());


        for(Long i=0L;i<105;i++) {
            preConfigured.put(i, "Hello World" + i);
        }

        for(Long i=0L;i<105;i++) {
            String value = preConfigured.get(i);
            System.out.println(value);
        }

        for(Long i=0L;i<20;i++) {
            anotherCache.put("Hello World" + i, "Hello World" + i);
        }

        for(Long i=0L;i<20;i++) {
            String value = anotherCache.get("Hello World" + i);
            System.out.println(value);
        }


    }
}
