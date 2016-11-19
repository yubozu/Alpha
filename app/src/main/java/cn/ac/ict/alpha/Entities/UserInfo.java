package cn.ac.ict.alpha.Entities;

/**
 * Author: saukymo
 * Date: 11/17/16
 */

public class UserInfo {
    private String phoneNumber;
    private String password;
    private Integer age;
    private Boolean gender;

    public UserInfo(String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.age = null;
        this.gender = null;
    }

    public Integer getAge() {
        return age;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
