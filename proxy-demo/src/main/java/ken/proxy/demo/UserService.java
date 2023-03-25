package ken.proxy.demo;

public class UserService implements  IUserService{

    @Override
    public void send() {
        System.out.println("this is send method from UserService");
    }
}
