package com.xdps.logic.domain;

import com.xdps.basic.domain.BasicBean;
import com.xdps.logic.util.DateUtil;

import javax.persistence.*;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * 用户实体
 *
 * @autherAdmin Deram Zhao
 * @creat 2017/8/24
 */
@Entity
@Table(name = "sys_user")
public class User extends BasicBean {
    @Id
    @Column(name = "USERID")
    private String userId;

    @Column(name = "USERNAME")
    private String userName;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "USERDESC")
    private String userDesc;

    //用户状态  0--为停用；1--为启用
    @Column(name = "STATE")
    private int state;

    @Column(name = "LICENSEIMG")
    @ElementCollection
    private List<String> licenseImg;//营业执照

    @Column(name = "ERRORTIME")
    private int errorTime;//密码输入错误次数


    //getter和setter方法

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserDesc() {
        return userDesc;
    }

    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<String> getLicenseImg() {
        return licenseImg;
    }

    public void setLicenseImg(List<String> licenseImg) {
        this.licenseImg = licenseImg;
    }

    public int getErrorTime() {
        return errorTime;
    }

    public void setErrorTime(int errorTime) {
        this.errorTime = errorTime;
    }
}
