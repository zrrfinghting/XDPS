package com.xdps.logic.domain;

import com.xdps.basic.domain.BasicBean;
import com.xdps.logic.util.DateUtil;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 订单
 *
 * @autherAdmin Deram Zhao
 * @creat 2017/9/1
 */
@Entity
@Table(name = "sys_order")
public class Order extends BasicBean {
    @Id
    @Column(name = "ORDERID")
    private String orderId;

    @Column(name = "RECEIVER")
    private String receiver;//收货人

    @Column(name = "RECEIVERPHONE")
    private String receiverPhone;//收货人电话

    @Column(name = "ADDRESS")
    private String address;//收货地址

    @Column(name = "DELIVERY")
    private String delivery;//配送人

    @Column(name = "DELIVERYPHONE")
    private String deliveryPhone;//配送人电话

    @Column(name = "STARTIME")
    private Date starTime;//配送时间（如多少点可以送过去）

    @Column(name = "ENDTIME")
    private Date endTime;//配送时间（多少时间前必须送到）

    @Column(name = "ORDERDESC")
    private String orderDesc;//备注

    @Column(name = "STATE")
    private int state;//-1--取消订单，0--未付款，1--未配送，2--配送中，3--配送完成


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getDeliveryPhone() {
        return deliveryPhone;
    }

    public void setDeliveryPhone(String deliveryPhone) {
        this.deliveryPhone = deliveryPhone;
    }

    public String getStarTime() {
        return DateUtil.formatDate(DateUtil.FORMAT2, starTime);
    }

    public void setStarTime(Date starTime) {
        this.starTime = starTime;
    }

    public String getEndTime() {
        return DateUtil.formatDate(DateUtil.FORMAT2, endTime);
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
