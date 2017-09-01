package com.xdps.logic.domain;

import com.xdps.basic.domain.BasicBean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 分类实体
 *
 * @autherAdmin Deram Zhao
 * @creat 2017/8/30
 */
@Entity
@Table(name = "sys_category")
public class Category extends BasicBean {
    @Id
    @Column(name = "CATEGORYID")
    private String categoryId;//分类ID

    @Column(name = "CATEGORYNAME")//分类名称
    private String categoryName;

    @Column(name = "PARENTID")//父分类ID
    private String parentId;

    @Column(name = "CATEGORYDESC")//分类备注
    private String getCategoryDesc;

    //setter 和getter方法


    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getGetCategoryDesc() {
        return getCategoryDesc;
    }

    public void setGetCategoryDesc(String getCategoryDesc) {
        this.getCategoryDesc = getCategoryDesc;
    }
}
