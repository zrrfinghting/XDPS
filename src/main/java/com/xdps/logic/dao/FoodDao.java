package com.xdps.logic.dao;

import com.xdps.logic.domain.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * food接口
 *
 * @autherAdmin Deram Zhao
 * @creat 2017/8/31
 */
public interface FoodDao extends CrudRepository<Food, Integer> {
    /**
     * 根据foodID获取信息
     *
     * @param foodId
     * @return
     */
    public Food findByFoodId(String foodId);

    /**
     * 根据ID删除
     *
     * @param foodId
     */
    @Modifying
    @Transactional
    public void deleteByFoodId(String foodId);


    /**
     * 分页获取食物信息（可以根据keyword进行模糊查询）
     *
     * @param keyword
     * @param pageable
     * @return
     */
    @Query("select t from Food t where t.foodName like %?1% or t.createUser like %?1% or t.foodDesc like %?1% or t.foodId like %?1% or t.source like %?1%")
    public Page<Food> findAllByKeyword(String keyword, Pageable pageable);

    @Query("select count (*) from Food t  where t.foodName like %?1% or t.createUser like %?1% or t.foodDesc like %?1% or t.foodId like %?1% or t.source like %?1%")
    public long countAllByKeyword(String keyword);


    /**
     * 根据分类ID分页获取食物信息（可以根据keyword在这个分类中进行模糊查询）
     *
     * @param categoryId
     * @param keyword
     * @param pageable
     * @return
     */
    @Query("select t from Food t where t.categoryId=?1 and (t.foodName like %?2% or t.createUser like %?2% or t.foodDesc like %?2% or t.foodId like %?2% or t.source like %?2%)")
    public Page<Food> findByCategoryId(String categoryId, String keyword, Pageable pageable);

    @Query("select count (*) from Food t where t.categoryId=?1 and (t.foodName like %?2% or t.createUser like %?2% or t.foodDesc like %?2% or t.foodId like %?2% or t.source like %?2%)")
    public long countByCategoryId(String categoryId, String keyword);
}
