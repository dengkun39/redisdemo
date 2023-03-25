package ken.proxy.demo;

import net.sf.cglib.proxy.Enhancer;
import org.springframework.boot.autoconfigure.security.SecurityProperties;

import java.lang.reflect.Proxy;

public class Main {
    public static void main(String[] args) {

        CGLibProxy cgLibProxy = new CGLibProxy();

//        Enhancer enhancer = new Enhancer();
//        enhancer.setSuperclass(HelloWorld.class);  // 设置超类，cglib是通过继承来实现的
//        enhancer.setCallback(cgLibProxy);
//
//        HelloWorld helloWorld =  (HelloWorld)enhancer.create();
//        helloWorld.say();


        HelloWorld helloWorld = cgLibProxy.getProxy(HelloWorld.class);
        helloWorld.say();

        //DynmicProxy();



    }

    private static void  DynmicProxy(){
        UserService userService = new UserService();
        DynamicProxy dynamicProxy = new DynamicProxy(userService);
        UserService userServiceProxy = dynamicProxy.getProxy();
        userServiceProxy.send();
//
//        IUserService userService1 = (IUserService) Proxy.newProxyInstance(userService.getClass().getClassLoader(),
//                userService.getClass().getInterfaces(), dynamicProxy);
//
//        userService1.send();

//        IHelloWorld helloWorld = new HelloWorld();
//        DynamicProxy dynamicProxy1 = new DynamicProxy(helloWorld);

//        IHelloWorld helloWorld1 = (IHelloWorld) Proxy.newProxyInstance(helloWorld.getClass().getClassLoader(),
//                helloWorld.getClass().getInterfaces(), dynamicProxy1);
//
//        String hello = helloWorld1.say();
//        System.out.println("this is return " + hello);
//
//        IHelloWorld helloWorld2 = dynamicProxy1.getProxy();
//        helloWorld2.say();

    }
}
