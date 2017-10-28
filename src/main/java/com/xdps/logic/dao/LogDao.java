package com.xdps.logic.dao;

import com.xdps.logic.domain.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

/**
 * 日志接口
 *
 * @autherAdmin Deram Zhao
 * @creat 2017/8/28
 */
public interface LogDao extends CrudRepository<Log, Integer> {


    /**
     * 根据用户编号 和操作类型 和 日志产生时间范围进行分页获取日志信息
     *
     * @param userCode
     * @param type
     * @param startTime
     * @param endTime
     * @param pageable
     * @return
     */
    @Query("select t from Log t where t.userCode=?1 and t.type=?2 and t.actionTime between ?3 and ?4")
    Page<Log> getByPage(String userCode, int type, Date startTime, Date endTime, Pageable pageable);

    @Query("select count (*) from Log t where t.userCode=?1 and t.type=?2 and t.actionTime between ?3 and ?4")
    public long count(String userCode, int type, Date startTime, Date endTime);


    /**
     * 根据用户编号 和 日志产生时间范围进行分页获取日志信息
     *
     * @param userCode
     * @param startTime
     * @param endTime
     * @param pageable
     * @return
     */
    @Query("select t from Log t where t.userCode=?1 and t.actionTime between ?2 and ?3")
    Page<Log> getByPage(String userCode, Date startTime, Date endTime, Pageable pageable);

    @Query("select count (*) from Log t where t.userCode=?1 and t.actionTime between ?2 and ?3")
    public long count(String userCode, Date startTime, Date endTime);


    /**
     * 根据日志产生时间范围获取日志信息
     *
     * @param startTime
     * @param endTime
     * @param pageable
     * @return
     */
    @Query("select t from Log t where t.actionTime between ?1 and ?2")
    Page<Log> getByPage(Date startTime, Date endTime, Pageable pageable);

    @Query("select count (*) from Log t where t.actionTime between ?1 and ?2")
    public long count(Date startTime, Date endTime);


    /**
     * 指定操作类型 获取日志信息
     *
     * @param type
     * @param startTime
     * @param endTime
     * @param pageable
     * @return
     */
    @Query("select t from Log t where t.type=?1 and t.actionTime between ?2 and ?3")
    Page<Log> getByPage(int type, Date startTime, Date endTime, Pageable pageable);

    @Query("select count (*) from Log t where t.type=?1 and t.actionTime between ?2 and ?3")
    public long count(int type, Date startTime, Date endTime);


    /**
     * 根据关键字查询日志信息
     * @param keyword
     * @param pageable
     * @return
     */
    @Query("select t from Log t where t.content like %?1% or t.type like %?1% or t.userCode like %?1%")
    Page<Log> getAllByKeyword(String keyword,Pageable pageable);

    @Query("select count (*) from Log t where t.content like %?1% or t.type like %?1% or t.userCode like %?1%")
    public long countAllByKeyword(String keyword);
}
