package com.xdps.logic.key;/**
 * Created by Admin on 2017/9/1.
 */

import java.io.Serializable;

/**
 * OrderFood的复合主键
 *
 * @autherAdmin Deram Zhao
 * @creat 2017/9/1
 */

public class OrderFoodPK implements Serializable {
    private String orderId;//订单ID
    private String foodId;//食物id

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
}
