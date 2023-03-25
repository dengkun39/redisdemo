package ken.ehcache.demo;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@State(Scope.Thread)
@Fork(1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
public class JmhEhCacheServiceRead {
    private EhCacheService ehCacheService = new EhCacheService();

    private Cache<Long,String> guavaCache = CacheBuilder.newBuilder()
            .concurrencyLevel(4)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .maximumSize(100)
            .build();

    @Setup
    public void init()
    {
        for (Long i = 0L; i < 100; i++) {
            ehCacheService.addHeapCache(i, "HelloWorld" + i);
            ehCacheService.addOffHeapCache(i, "HelloWorld" + i);
            ehCacheService.addMultipleTierCache(i, "HelloWorld" + i);
            guavaCache.put(i, "HelloWorld" + i);
        }
    }

    @Benchmark
    public void getGuavaCache() {
        for (Long i = 0L; i < 100; i++) {
            guavaCache.getIfPresent(i);
        }
    }

    @Benchmark
    public void getHeap() {
        for (Long i = 0L; i < 100; i++) {
            ehCacheService.getHeapCache(i);
        }
    }

    @Benchmark
    public void getOffHeap() {
        for (Long i = 0L; i < 100; i++) {
            ehCacheService.getOffHeapCache(i);
        }
    }

    @Benchmark
    public void getMultipleTierCache() {
        for (Long i = 0L; i < 100; i++) {
            ehCacheService.getMultipleTierCache(i);
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JmhEhCacheServiceRead.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }

}
