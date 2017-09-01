package com.xdps.logic.dao;

import com.xdps.logic.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.Column;

/**
 * 订单接口
 *
 * @autherAdmin Deram Zhao
 * @creat 2017/9/1
 */
public interface OrderDao extends CrudRepository<Order, Integer> {

    /**
     * 根据in获取订单信息
     *
     * @param orderId
     * @return
     */
    public Order findByOrderId(String orderId);


    /**
     * 分页获取订单信息（可根据keyword进行模糊查询）
     *
     * @param keyword  关键字
     * @param pageable
     * @return
     */
    @Query("select t from Order t where t.createUser like %?1% or t.address like %?1% or t.orderDesc like %?1% or " +
            "t.delivery like %?1% or t.deliveryPhone like %?1% or t.receiver like %?1% or t.receiverPhone like %?1%")
    public Page<Order> findAllByKeyword(String keyword, Pageable pageable);

    @Query("select count (*) from Order t where t.createUser like %?1% or t.address like %?1% or t.orderDesc like %?1% or " +
            "t.delivery like %?1% or t.deliveryPhone like %?1% or t.receiver like %?1% or t.receiverPhone like %?1%")
    public long countAllByKeyword(String keyword);


    /**
     * 分页获取订单信息（根据keyword和state）
     *
     * @param keyword  关键字
     * @param state    状态
     * @param pageable
     * @return
     */
    @Query("select t from Order t where t.state=?2 and (t.createUser like %?1% or t.address like %?1% or t.orderDesc like %?1% or " +
            "t.delivery like %?1% or t.deliveryPhone like %?1% or t.receiver like %?1% or t.receiverPhone like %?1%)")
    public Page<Order> findAllByKeywordAndState(String keyword, int state, Pageable pageable);

    @Query("select count (*) from Order t where t.state=?2 and (t.createUser like %?1% or t.address like %?1% or t.orderDesc like %?1% or " +
            "t.delivery like %?1% or t.deliveryPhone like %?1% or t.receiver like %?1% or t.receiverPhone like %?1%)")
    public long countAllByKeywordAndState(String keyword, int state);


    /**
     * 分页获取订单信息（根据keyword和creteUser）
     *
     * @param keyword
     * @param createUser 订单创建人
     * @return
     */
    @Query("select t from Order t where t.createUser=?2 and (t.createUser like %?1% or t.address like %?1% or t.orderDesc like %?1% or " +
            "t.delivery like %?1% or t.deliveryPhone like %?1% or t.receiver like %?1% or t.receiverPhone like %?1%)")
    public Page<Order> findAllByKeywordAnAndCreateUser(String keyword, String createUser, Pageable pageable);

    @Query("select count (*) from Order t where t.createUser=?2 and (t.createUser like %?1% or t.address like %?1% or t.orderDesc like %?1% or " +
            "t.delivery like %?1% or t.deliveryPhone like %?1% or t.receiver like %?1% or t.receiverPhone like %?1%)")
    public long countAllByKeywordAndCreateUser(String keyword, String createUser);


    /**
     * 分页获取订单信息（根据keyword和creteUser和state）
     *
     * @param keyword
     * @param createUser
     * @param state
     * @param pageable
     * @return
     */
    @Query("select t from Order t where t.createUser=?2 and t.state=?3 and (t.createUser like %?1% or t.address like %?1% or t.orderDesc like %?1% or " +
            "t.delivery like %?1% or t.deliveryPhone like %?1% or t.receiver like %?1% or t.receiverPhone like %?1%)")
    public Page<Order> findAllByKeywordAnAndCreateUserAndState(String keyword, String createUser, int state, Pageable pageable);

    @Query("select count (*) from Order t where t.createUser=?2 and t.state=?3 and (t.createUser like %?1% or t.address like %?1% or t.orderDesc like %?1% or " +
            "t.delivery like %?1% or t.deliveryPhone like %?1% or t.receiver like %?1% or t.receiverPhone like %?1%)")
    public long countAllByKeywordAndCreateUserAndState(String keyword, String createUser, int state);

}
