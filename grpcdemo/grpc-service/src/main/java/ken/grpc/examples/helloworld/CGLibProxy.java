package ken.grpc.examples.helloworld;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CGLibProxy implements MethodInterceptor {
    private void before(){
        System.out.println("this is before");
    }

    private void after(){
        System.out.println("this is after");
    }

    public <T> T getProxy(Class<T> cls) {
        return (T) Enhancer.create(cls,this);
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        before();
        Object result =  methodProxy.invokeSuper(obj, args);
        after();
        return result;
    }
}
