package cn.ac.ict.alpha.Entities;

/**
 * Author: saukymo
 * Date: 11/17/16
 */

public class UserInfoEntity extends BaseEntity{
    private Integer id;
    private String phone_number;
    private String password;
    private Integer age;
    private Boolean gender;

    // 注册新用户时使用
    public UserInfoEntity(String phone_number, String password) {
        this.phone_number = phone_number;
        this.password = password;
        this.age = 0;
        this.gender = false;
    }

    // 更新用户信息时使用
    public UserInfoEntity(String phone_number, Integer age, Boolean gender) {
        this.phone_number = phone_number;
        this.password = null;
        this.age = age;
        this.gender = gender;
    }

    // 更新密码时使用
    public UserInfoEntity(String password) {
        this.phone_number = null;
        this.password = password;
        this.age = null;
        this.gender = null;
    }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

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

    public String getPhone_number() {
        return phone_number;
    }

    public String getPassword() {
        return password;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
