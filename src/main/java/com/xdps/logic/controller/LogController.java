package com.xdps.logic.controller;

import com.xdps.logic.dao.LogDao;
import com.xdps.logic.domain.Log;
import com.xdps.logic.util.JsonUtil;
import com.xdps.logic.util.TableUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日志对外发布类
 *
 * @autherAdmin Deram Zhao
 * @creat 2017/8/28
 */
@RestController
@RequestMapping("/log")
public class LogController {
    @Autowired
    private LogDao logDao;

    @RequestMapping(value = "getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "根据操作用户编号 操作类型 日志产生时间范围 分页获取日志信息", notes = "根据操作用户编号 操作类型 日志产生时间范围 分页获取日志信息")
    public String getByPage(
            @RequestParam int type, @RequestParam Date startTime, @RequestParam Date endTime, @RequestParam String userCode,
            @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize) {
        try {
            if (startTime.equals("") || null == startTime) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(2017, 8, 28, 8, 8, 8);
                startTime = calendar.getTime();
            }
            if (endTime.equals("")) {
                endTime = new Date();
            }
            long total = 0;
            Page<Log> logs = null;
            Sort sort = new Sort(Sort.Direction.DESC, "actionTime");
            Pageable pageable = new PageRequest(pageNum - 1, pageSize);
            if (type == -1 && userCode != "") {//指定用户  所有操作类型
               // logs = logDao.getByPage(userCode, startTime, endTime, pageable);
            } else if (userCode != "") {//指定用户 指定操作类型
                logs = logDao.getByPage(userCode, type, startTime, endTime, pageable);
                total = logDao.count(userCode, type, startTime, endTime);
            } else if (userCode.equals("") && type != -1) {//指定操作类型
              //  logs = logDao.getByPage(type, startTime, endTime, pageable);
               // total = logDao.count(type, startTime, endTime);
            } else if (userCode.equals("") && type == -1) {//获取所有日志信息
               // logs = logDao.getByPage(startTime, endTime, pageable);
              //  total = logDao.count(startTime, endTime);
            }
            List<Log> list = new ArrayList<>();
            for (Log log : logs) {
                list.add(log);
            }

            long totalPage = total / pageSize == 0 ? total / pageSize : total / pageSize + 1;
            return TableUtil.createTableDate(list, total, pageNum, totalPage, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL, "获取日志信息失败");
        }
    }
}
