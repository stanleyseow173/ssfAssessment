package ibf2022.batch2.ssf.frontcontroller.model;

import java.io.Serializable;

import jakarta.validation.constraints.Size;

public class User implements Serializable{

    @Size(min=2, message="Must be at least 2 characters in length")
    private String username;

    @Size(min=2, message="Must be at least 2 characters in length")
    private String password;

    private boolean authenticated = false;

    private int attempts;

    private String errorMsg;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public boolean isAuthenticated() {
        return authenticated;
    }
    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
    public int getAttempts() {
        return attempts;
    }
    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public void addAttempts(){
        this.attempts +=1;
    }
    
    
}
