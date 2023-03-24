package ibf2022.batch2.ssf.frontcontroller.model;

public class LoginForm {
    private Captcha captcha;
    private User user;

    public Captcha getCaptcha() {
        return captcha;
    }
    public void setCaptcha(Captcha captcha) {
        this.captcha = captcha;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public LoginForm() {
        this.user = new User();
    }
    
    
}
