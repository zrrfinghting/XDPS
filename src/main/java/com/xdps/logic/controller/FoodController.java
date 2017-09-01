package com.xdps.logic.controller;

import com.xdps.logic.dao.FoodDao;
import com.xdps.logic.dao.LogDao;
import com.xdps.logic.domain.Food;
import com.xdps.logic.domain.Log;
import com.xdps.logic.util.JsonUtil;
import com.xdps.logic.util.TableUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 对外发布类
 *
 * @autherAdmin Deram Zhao
 * @creat 2017/8/31
 */
@RestController
@RequestMapping("/food")
public class FoodController {
    @Autowired
    private FoodDao foodDao;
    @Autowired
    private LogDao logDao;

    @RequestMapping(value = "/addFood", method = RequestMethod.POST)
    @ApiOperation(value = "新增", notes = "新增")
    public String addFood(@RequestBody Food food) {
        try {
            food.setFoodId(String.valueOf(System.currentTimeMillis()));//系统毫秒值作为ID
            food.setCreateDate(new Date());
            foodDao.save(food);
            //记录日志
            logDao.save(new Log("", 2, "新增" + food.getFoodName() + "成功", new Date()));
            return JsonUtil.returnStr(JsonUtil.SUCCESS, "新增" + food.getFoodName() + "成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL, "新增失败");
        }
    }

    @RequestMapping(value = "/editFood", method = RequestMethod.POST)
    @ApiOperation(value = "修改", notes = "修改")
    public String editFood(@RequestBody Food food) {
        try {
            Food oldFood = foodDao.findByFoodId(food.getFoodId());
            if (null == oldFood) {
                return JsonUtil.returnStr(JsonUtil.FAIL, "修改的食物不存在");
            }
            food.setUpdateDate(new Date());
            foodDao.save(food);
            //记录日志
            logDao.save(new Log("", 3, "修改成功" + food.getFoodName() + "成功", new Date()));
            return JsonUtil.returnStr(JsonUtil.SUCCESS, "修改成功" + food.getFoodName() + "成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL, "修改失败");
        }
    }

    @RequestMapping(value = "/deleteById", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除", notes = "删除")
    public String deleteById(@RequestParam String foodId) {
        try {
            foodDao.deleteByFoodId(foodId);
            //记录日志
            logDao.save(new Log("", 4, "删除成功", new Date()));
            return JsonUtil.returnStr(JsonUtil.SUCCESS, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL, "删除失败");
        }
    }

    @RequestMapping(value = "/deleteByIds", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除", notes = "删除")
    public String deleteByIds(@RequestParam String[] foodIds) {
        try {
            List<Food> foods = new ArrayList<>();
            String deleteIds = "";
            String undeleteIds = "";
            for (int i = 0; i < foodIds.length; i++) {
                Food food = foodDao.findByFoodId(foodIds[i]);
                if (null == food) {
                    undeleteIds += foodIds[i] + ",";
                } else {
                    foods.add(food);
                    deleteIds += foodIds[i];
                }
            }
            foodDao.delete(foods);
            if ("".equals(undeleteIds)) {
                //记录日志
                logDao.save(new Log("", 4, "食物编号为" + deleteIds + "的删除成功", new Date()));
                return JsonUtil.returnStr(JsonUtil.SUCCESS, "删除成功");
            } else {
                //记录日志
                logDao.save(new Log("", 4, "编号为" + deleteIds + "已经删除,编号为" + undeleteIds + "的食物不存在删除失败", new Date()));
                return JsonUtil.returnStr(JsonUtil.SUCCESS, "编号为" + deleteIds + "已经删除成功,编号为" + undeleteIds + "的食物不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL, "删除失败");
        }
    }

    @RequestMapping(value = "/getFoodById", method = RequestMethod.GET)
    @ApiOperation(value = "根据ID获取", notes = "根据ID获取")
    public String getFoodById(@RequestParam String foodId) {
        try {
            Food food = foodDao.findByFoodId(foodId);
            return JsonUtil.fromObject(food);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL, "获取食物信息失败");
        }
    }

    @RequestMapping(value = "getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取食物", notes = "分页获取食物（可以根据keyword进行模糊查询）")
    public String getByPage(
            @RequestParam String keyword,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize) {
        try {
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            Pageable pageable = new PageRequest(pageNum - 1, pageSize, sort);
            Page<Food> foods = foodDao.findAllByKeyword(keyword, pageable);
            List<Food> list = new ArrayList<>();
            for (Food food : foods) {
                list.add(food);
            }
            long total = foodDao.countAllByKeyword(keyword);
            long totalPage = total / pageSize == 0 ? total / pageSize : total / pageSize + 1;
            return TableUtil.createTableDate(list, total, pageNum, totalPage, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL, "分页获取食物信息失败");
        }
    }

    @RequestMapping(value = "getByCategoryIdAndKeyword", method = RequestMethod.GET)
    @ApiOperation(value = "根据所属分类分页获取食物", notes = "根据所属分类or（在该分类中根据keyword关键查询）分页获取食物信息")
    public String getByCategoryIdAndKeyword(
            @RequestParam String categoryId,
            @RequestParam String keyword,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize) {

        try {
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            Pageable pageable = new PageRequest(pageNum - 1, pageSize, sort);
            Page<Food> foods = foodDao.findByCategoryId(categoryId, keyword, pageable);
            List<Food> list = new ArrayList<>();
            for (Food food : foods) {
                list.add(food);
            }
            long total = foodDao.countByCategoryId(categoryId, keyword);
            long totalPage = total / pageSize == 0 ? total / pageSize : total / pageSize + 1;
            return TableUtil.createTableDate(list,total,pageNum,totalPage,pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL, "据所属分类分页获取食物失败");
        }
    }
}
