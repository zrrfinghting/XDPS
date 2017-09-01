package com.xdps.logic.domain;

import com.xdps.basic.domain.BasicBean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @autherAdmin Deram Zhao
 * @creat 2017/8/31
 */
@Entity
@Table(name = "sys_food")
public class Food extends BasicBean {
    @Id
    @Column(name = "FOODID")
    private String foodId;//ID

    @Column(name = "FOODNAME")
    private String foodName;//名称

    @Column(name = "CATEGORYID")
    private String categoryId;//分类ID

    @Column(name = "SOURCE")
    private String source;//来源

    @Column(name = "FOODDESC")
    private String foodDesc;//备注

    @Column(name = "STORAGE")
    private int storage;//库存

    @Column(name = "PRICE")
    private int price;//价格

    @Column(name = "IMAGE")
    private String image;

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getFoodDesc() {
        return foodDesc;
    }

    public void setFoodDesc(String foodDesc) {
        this.foodDesc = foodDesc;
    }

    public int getStorage() {
        return storage;
    }

    public void setStorage(int storage) {
        this.storage = storage;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
