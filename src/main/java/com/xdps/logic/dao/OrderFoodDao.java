package com.xdps.logic.dao;

import com.xdps.logic.domain.OrderFood;
import org.springframework.data.repository.CrudRepository;

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
    public OrderFood findByOrderId(String orderId);

    /**
     * 根据orderId和foodId获取orderFood信息
     *
     * @param orderId
     * @param foodId
     * @return
     */
    public OrderFood findByOrderIdAndFoodId(String orderId, String foodId);
}
