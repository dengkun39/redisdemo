package com.ken.resilience4j.bookcustomer;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.core.functions.CheckedFunction;
import io.github.resilience4j.core.functions.CheckedRunnable;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@Slf4j
public class Resilience4jUnitTest {

    interface RemoteService {

        int process(int i);
    }

    private RemoteService service;


    @Test
    public void whenCircuitBreakerIsUsed_thenItWorksAsExpected() {
        service = mock(RemoteService.class);
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                // Percentage of failures to start short-circuit
                .failureRateThreshold(20)
                .minimumNumberOfCalls(5)
                .build();
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
        CircuitBreaker circuitBreaker = registry.circuitBreaker("my");
        Function<Integer, Integer> decorated = CircuitBreaker.decorateFunction(circuitBreaker, service::process);

        when(service.process(anyInt())).thenThrow(new RuntimeException());
        circuitBreaker.getEventPublisher()
                .onEvent(event ->
                {
                    log.info(event.toString());
                });

        for (int i = 0; i < 10; i++) {
            try {
                decorated.apply(i);
            } catch (Exception ignore) {
            }
        }



        verify(service, times(5)).process(any(Integer.class));
    }

    @Test
    public void whenBulkheadIsUsed_thenItWorksAsExpected() throws InterruptedException {
        service = mock(RemoteService.class);
        BulkheadConfig config = BulkheadConfig.custom().maxConcurrentCalls(2).build();
        BulkheadRegistry registry = BulkheadRegistry.of(config);
        Bulkhead bulkhead = registry.bulkhead("my");
        Function<Integer, Integer> decorated =  Bulkhead.decorateFunction(bulkhead, service::process);

       try {
            callAndBlock(decorated);
        }
       catch(BulkheadFullException ex)
        {
            log.error("isfull");
        }
        finally
        {
           verify(service, times(2)).process(any(Integer.class));
        }

    }

    private void callAndBlock(Function<Integer, Integer> decoratedService) throws InterruptedException {
        when(service.process(anyInt())).thenAnswer(invocation -> {
            log.info("service called");
            return null;
        });

        ArrayList<Integer> numberList = new ArrayList<Integer>();
        for(int i = 0;i<10;i++)
        {
            numberList.add(i);
        }

        numberList.parallelStream().forEach((i)->{
            try {
                decoratedService.apply(i);
            }
            catch (Exception ex)
            {
                log.error("meet error " + ex.getMessage());
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void whenRateLimiterInUse_thenItWorksAsExpected() throws InterruptedException {
        service = mock(RemoteService.class);

        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitRefreshPeriod(Duration.ofMillis(1000))
                .limitForPeriod(4)
                .timeoutDuration(Duration.ofMillis(25))
                .build();

        RateLimiterRegistry rateLimiterRegistry = RateLimiterRegistry.of(config);

        RateLimiter rateLimiter = rateLimiterRegistry
                .rateLimiter("name1");

        CheckedFunction<Integer, Integer> decorated = RateLimiter
                .decorateCheckedFunction(rateLimiter, service::process);

        try {
            callAndBlock(decorated);
        }
        catch(Exception ex)
        {
            log.error("isfull");
        }
        finally
        {
            verify(service, times(4)).process(any(Integer.class));
        }

    }

    private void callAndBlock(CheckedFunction<Integer, Integer> decoratedService) throws InterruptedException {
        when(service.process(anyInt())).thenAnswer(invocation -> {
            log.info("service called");
            return null;
        });

        ArrayList<Integer> numberList = new ArrayList<Integer>();
        for(int i = 0;i<10;i++)
        {
            numberList.add(i);
        }

        numberList.parallelStream().forEach((i)->{
            try {
                decoratedService.apply(i);
            }
            catch (Exception ex)
            {
                log.error("meet error " + ex.getMessage());
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
    }


    @Test
    public void whenRetryIsUsed_thenItWorksAsExpected() {
        service = mock(RemoteService.class);
        RetryConfig config = RetryConfig.custom().maxAttempts(2).build();
        RetryRegistry registry = RetryRegistry.of(config);
        Retry retry = registry.retry("my");
        Function<Integer, Void> decorated = Retry.decorateFunction(retry, (Integer s) -> {
            service.process(s);
            return null;
        });

        when(service.process(anyInt())).thenThrow(new RuntimeException());
        try {
            decorated.apply(1);
            fail("Expected an exception to be thrown if all retries failed");
        } catch (Exception e) {
            verify(service, times(2)).process(any(Integer.class));
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void whenTimeLimiterIsUsed_thenItWorksAsExpected() throws Exception {
        long ttl = 1;
        TimeLimiterConfig config = TimeLimiterConfig.custom().timeoutDuration(Duration.ofMillis(ttl)).build();
        TimeLimiter timeLimiter = TimeLimiter.of(config);

        Future futureMock = mock(Future.class);
        Callable restrictedCall = TimeLimiter.decorateFutureSupplier(timeLimiter, () -> futureMock);
        restrictedCall.call();

        verify(futureMock).get(ttl, TimeUnit.MILLISECONDS);
    }
}