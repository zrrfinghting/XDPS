package com.xdps.logic.dao;

import com.xdps.logic.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 分类接口
 *
 * @autherAdmin Deram Zhao
 * @creat 2017/8/30
 */
public interface CategoryDao extends CrudRepository<Category, Integer> {

    /**
     * 根据分类ID获取分类信息
     *
     * @param categoryId
     * @return
     */
    public Category findByCategoryId(String categoryId);

    /**
     * 根据分类ID删除分类信息
     *
     * @param categoryId
     */
    @Modifying
    @Transactional
    public void deleteByCategoryId(String categoryId);

    /**
     * 分页获取分类信息
     *
     * @param keyword
     * @param pageable
     * @return
     */
    @Query("select t from Category  t where t.categoryName like %?1% or t.categoryId like %?1% or t.categoryDesc like %?1% ")
    public Page<Category> findAllByKeyword(String keyword, Pageable pageable);

    @Query("select count (*) from Category  t where t.categoryName like %?1% or t.categoryId like %?1%  or t.categoryDesc like %?1% ")
    public long countAllByKeyword(String keyword);

    /**
     * 获取所有分类信息
     *
     * @return
     */
    @Query("select t from  Category t ")
    public List<Category> findAll();

}
