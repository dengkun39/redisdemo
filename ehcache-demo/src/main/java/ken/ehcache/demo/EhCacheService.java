package ken.ehcache.demo;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;

public class EhCacheService {
    private Cache<Long, String> heapCache;
    private Cache<Long, String> offHeapCache;

    private Cache<Long, String> multipleTierCache;

    public EhCacheService()
    {
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true);

        CacheConfiguration<Long, String> heapCacheConfig =  CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
                ResourcePoolsBuilder.heap(100)).build();

        CacheConfiguration<Long, String> offheapCacheConfig =  CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
                ResourcePoolsBuilder.newResourcePoolsBuilder().offheap(10, MemoryUnit.MB)).build();

        CacheConfiguration<Long, String> multipleCacheConfig =  CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
                ResourcePoolsBuilder.heap(100).offheap(10, MemoryUnit.MB)).build();



        heapCache = cacheManager.createCache("heapCache", heapCacheConfig);
        offHeapCache = cacheManager.createCache("offHeapCache", offheapCacheConfig);
        multipleTierCache = cacheManager.createCache("multipleTierCache", multipleCacheConfig);

    }

    public void addHeapCache(Long l, String v)
    {
        heapCache.put(l,v);
    }

    public String getHeapCache(Long l)
    {
        return heapCache.get(l);
    }

    public void addOffHeapCache(Long l, String v)
    {
        offHeapCache.put(l,v);
    }

    public String getOffHeapCache(Long l)
    {
        return offHeapCache.get(l);
    }

    public void addMultipleTierCache(Long l, String v)
    {
        multipleTierCache.put(l,v);
    }

    public String getMultipleTierCache(Long l)
    {
        return multipleTierCache.get(l);
    }


}
