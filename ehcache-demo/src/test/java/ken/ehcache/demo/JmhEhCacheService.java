package ken.ehcache.demo;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.AverageTime)
@State(Scope.Thread)
@Fork(1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
public class JmhEhCacheService {
    private EhCacheService ehCacheService = new EhCacheService();

    private Cache<Long,String> guavaCache = CacheBuilder.newBuilder()
            .concurrencyLevel(4)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .maximumSize(100)
            .build();

    @Benchmark
    public void addGuava() {
        for (Long i = 0L; i < 100; i++) {
            guavaCache.put(i, "HelloWorld" + i);
        }
    }

    @Benchmark
    public void addHeap() {
        for (Long i = 0L; i < 100; i++) {
            ehCacheService.addHeapCache(i, "HelloWorld" + i);
        }
    }

    @Benchmark
    public void addOffHeap() {
        for (Long i = 0L; i < 100; i++) {
            ehCacheService.addOffHeapCache(i, "HelloWorld" + i);
        }
    }

    @Benchmark
    public void addMultipleTierCache() {
        for (Long i = 0L; i < 100; i++) {
            ehCacheService.addMultipleTierCache(i, "HelloWorld" + i);
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JmhEhCacheService.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }

}
