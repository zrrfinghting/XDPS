package com.xdps.logic.domain;/**
 * Created by Admin on 2017/8/28.
 */

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 日志
 *
 * @autherAdmin Deram Zhao
 * @creat 2017/8/28
 */
@Entity
@Table(name = "sys_log")
public class Log {

    // 操作类型常量
    public static final String ADD = "新增";
    public static final String UPDATE = "修改";
    public static final String DELETE = "删除";
    public static final String LOGIN = "登录";
    public static final String STOP = "停用";
    public static final String START = "启用";
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "LOGID")
    private String logId;//日志ID

    @Column(name = "USERCODE")
    private String userCode;//操作用户名称

    @Column(name = "TYPE")
    private int type;//操作类型-1--全部类型 0--停用，1--启用，2--新增，3--修改，4--删除，5--登录，6--文件上传

    @Column(name = "CONTENT")
    private String content;//操作内容

    @Column(name = "ACTIONTIME")
    private Date actionTime;//操作时间

    //构造方法
    public Log(){}

    public Log(String userCode,int type,String content,Date actionTime){
        this.userCode=userCode;
        this.type=type;
        this.content=content;
        this.actionTime=actionTime;
    }

    //getter 和setter方法
    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getActionTime() {
        return actionTime;
    }

    public void setActionTime(Date actionTime) {
        this.actionTime = actionTime;
    }
}
