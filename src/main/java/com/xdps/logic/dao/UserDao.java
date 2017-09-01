package com.xdps.logic.dao;

import com.xdps.logic.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户接口
 *
 * @autherAdmin Deram Zhao
 * @creat 2017/8/25
 */
public interface UserDao extends CrudRepository<User, Integer> {

    /**
     * 通过用户ID查询用户信息
     *
     * @param userId
     * @return User
     */
    public User findByUserId(String userId);

    /**
     * 通过用户ID删除用户信息
     *
     * @param userId
     */
    @Modifying
    @Transactional
    public void deleteByUserId(String userId);


    /**
     * 分页获取用户信息（可以根据keyword进行模糊查询）
     *
     * @param keyword
     * @param pageable
     * @return
     */
    @Query("select t from  User t where t.userName like %?1% or t.userId like %?1% or t.userDesc like %?1% or t.phone like %?1% or t.address like %?1%")
    public Page<User> findAllByKeyword(String keyword, Pageable pageable);
    @Query("select count (*) from  User t where t.userName like %?1% or t.userId like %?1% or t.userDesc like %?1% or t.phone like %?1% or t.address like %?1%")
    public long countAllByKeyword(String keyword);

}
