package com.xdps.logic.domain;

import com.sun.javafx.beans.IDProperty;
import com.xdps.basic.domain.BasicBean;
import com.xdps.logic.key.OrderFoodPK;

import javax.persistence.*;

/**
 * Order和Food的中间表实体OrderFood
 *
 * @autherAdmin Deram Zhao
 * @creat 2017/9/1
 */

@Entity
@IdClass(OrderFoodPK.class)
@Table(name = "sys_order_food")
public class OrderFood extends BasicBean {
    @Id
    @Column(name = "ORDERID")
    private String orderId;

    @Id
    @Column(name = "FOODID")
    private String foodId;

    @Column(name = "NUMBER")
    private int number;

    @Column(name = "STATE")
    private int state;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
