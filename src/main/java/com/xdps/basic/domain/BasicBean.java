package com.xdps.basic.domain;

import com.xdps.logic.util.DateUtil;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.text.DateFormat;
import java.util.Date;

/**
 * 基础类
 *
 * @autherAdmin Deram Zhao
 * @creat 2017/8/24
 */

@MappedSuperclass
public class BasicBean {

    @Column(name = "CREATEUSER")
    private String createUser;

    @Column(name = "UPDATEUSER")
    private String updateUser;

    @Column(name = "CREATEDATE")
    private Date createDate;

    @Column(name = "UPDATEDATE")
    private Date updateDate;

    //getter和setter方法
    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getCreateDate() {
        return DateUtil.formatDate(DateUtil.FORMAT2,createDate);
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return DateUtil.formatDate(DateUtil.FORMAT2,updateDate);
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
