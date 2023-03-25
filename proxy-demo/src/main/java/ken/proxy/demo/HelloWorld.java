package ken.proxy.demo;

public class HelloWorld implements  IHelloWorld{
    @Override
    public String say() {
        System.out.println("this is helloworld");
        return "Hello World";
    }
}
