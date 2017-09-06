package com.xdps.logic.dao;

import com.xdps.logic.domain.OrderFood;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * OrderFood接口
 *
 * @autherAdmin Deram Zhao
 * @creat 2017/9/1
 */
public interface OrderFoodDao extends CrudRepository<OrderFood, Integer> {

    /**
     * 根据orderId获取orderFood信息
     *
     * @param orderId
     * @return
     */
    public List<OrderFood> findByOrderId(String orderId);

    /**
     * 根据orderId和foodId获取orderFood信息
     *
     * @param orderId
     * @param foodId
     * @return
     */
    public OrderFood findByOrderIdAndFoodId(String orderId, String foodId);

    /**
     * 根据foodId删除food信息
     *
     * @param foodId
     */
    @Modifying
    @Transactional
    public void deleteByFoodId(String foodId);
}
