package ken.proxy.demo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxy implements InvocationHandler {

    private Object target;
    public DynamicProxy(Object target){
        this.target = target;
    }

    private void before(){
        System.out.println("this is before");
    }

    private void after(){
        System.out.println("this is after");
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        before();
        Object result = method.invoke(this.target, args);
        after();
        return result;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy()
    {
        return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(), this);
    }
}
