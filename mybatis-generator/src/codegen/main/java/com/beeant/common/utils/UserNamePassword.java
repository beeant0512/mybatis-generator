package com.beeant.common.utils;

/**
 * Created by Beeant on 2016/3/4.
 */
public class UserNamePassword {
    private String username;

    private String password;

    private boolean rememberMe = false;

    /**
     * Getter for property 'password'.
     *
     * @return Value for property 'password'.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter for property 'password'.
     *
     * @param password Value to set for property 'password'.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter for property 'username'.
     *
     * @return Value for property 'username'.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for property 'username'.
     *
     * @param username Value to set for property 'username'.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter for property 'rememberMe'.
     *
     * @return Value for property 'rememberMe'.
     */
    public boolean isRememberMe() {
        return rememberMe;
    }

    /**
     * Setter for property 'rememberMe'.
     *
     * @param rememberMe Value to set for property 'rememberMe'.
     */
    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}
