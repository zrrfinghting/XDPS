package com.xdps.logic.controller;

import com.xdps.logic.dao.CategoryDao;
import com.xdps.logic.dao.LogDao;
import com.xdps.logic.domain.Category;
import com.xdps.logic.domain.Log;
import com.xdps.logic.domain.User;
import com.xdps.logic.util.JsonUtil;
import com.xdps.logic.util.TableUtil;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
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
 * 分类对外发布类
 *
 * @autherAdmin Deram Zhao
 * @creat 2017/8/30
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private LogDao logDao;

    /**
     * level新增分类等级：-1--顶级(父节点为空)，0--下级，1--同级
     * bookType对象里的parent字段是前端勾选的分类节点的categoryId
     *
     * @param category
     * @param level
     * @return
     */
    @RequestMapping(value = "/addCategory", method = RequestMethod.POST)
    @ApiOperation(value = "新增分类", notes = "新增分类")
    public String addCategory(@RequestBody Category category, @RequestParam int level) {
        try {
            if (level == -1) {
                category.setCategoryId(String.valueOf(System.currentTimeMillis()));//当前系统毫秒值作为分类ID
                category.setParentId("");//因为是顶级分类所以没有父类ID
                category.setCreateDate(new Date());
            } else if (level == 0) {
                category.setCategoryId(String.valueOf(System.currentTimeMillis()));//
                category.setParentId(category.getCategoryId());//因为是下级，所以勾选分类的ID是新增分类的父分类ID
                category.setCreateDate(new Date());
            } else if (level == 1) {
                Category oldCategory = categoryDao.findByCategoryId(category.getCategoryId());
                category.setCategoryId(String.valueOf(System.currentTimeMillis()));//
                category.setParentId(oldCategory.getParentId());//因为是同级所以 父分类ID是相同的
                category.setCreateDate(new Date());
            } else {
                return JsonUtil.returnStr(JsonUtil.FAIL, "新增分类失败");
            }
            categoryDao.save(category);
            //记录日志
            logDao.save(new Log("", 2, "新增分类" + category.getCategoryName() + "", new Date()));
            return JsonUtil.returnStr(JsonUtil.SUCCESS, "新增分类成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL, "新增分类失败");
        }
    }

    @RequestMapping(value = "/editCategory", method = RequestMethod.POST)
    @ApiOperation(value = "修改分类", notes = "修改分类")
    public String editCategory(@RequestBody Category category) {
        try {
            Category oldCategory = categoryDao.findByCategoryId(category.getCategoryId());
            if (null == oldCategory) {
                return JsonUtil.returnStr(JsonUtil.FAIL, "修改分类不存在");
            }
            category.setUpdateDate(new Date());
            categoryDao.save(category);
            //记录日志
            logDao.save(new Log("", 3, "修改分类" + category.getCategoryName() + "成功", new Date()));
            return JsonUtil.returnStr(JsonUtil.SUCCESS, "修改分类成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL, "修改分类失败");
        }
    }

    @RequestMapping(value = "/deleteById", method = RequestMethod.GET)
    @ApiOperation(value = "通过ID删除分类", notes = "通过ID删除分类")
    public String deleteById(@RequestParam String categoryId) {
        try {
            categoryDao.deleteByCategoryId(categoryId);
            //记录日志
            logDao.save(new Log("", 3, "删除编号为" + categoryId + "的分类成功", new Date()));
            return JsonUtil.returnStr(JsonUtil.SUCCESS, "删除分类成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL, "删除分类失败");
        }
    }

    @RequestMapping(value = "/deleteByIds", method = RequestMethod.GET)
    @ApiOperation(value = "通过ID批量删除分类", notes = "通过ID批量删除分类")
    public String deleteByIds(@RequestParam String[] categoryIds) {
        try {
            List<Category> categories = new ArrayList<>();
            String deleteIds = "";
            String unDeleteIds = "";
            for (int i = 0; i < categoryIds.length; i++) {
                Category category = categoryDao.findByCategoryId(categoryIds[i]);
                if (null == category) {
                    unDeleteIds += categoryIds[i] + ",";
                } else {
                    categories.add(category);
                    deleteIds += categoryIds[i] + ",";
                }
            }
            categoryDao.delete(categories);
            //记录日志
            logDao.save(new Log("", 3, "分类编号是" + deleteIds + "的分类删除成功", new Date()));
            if ("".equals(unDeleteIds)) {
                return JsonUtil.returnStr(JsonUtil.SUCCESS, "删除分类成功");
            } else {
                return JsonUtil.returnStr(JsonUtil.FAIL, "分类编号是" + unDeleteIds + "的分类不存在删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL, "分类删除失败");
        }
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取分类", notes = "分页获取分类信息")
    public String getByPage(
            @RequestParam String keyword,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize) {

        try {
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            Pageable pageable = new PageRequest(pageNum - 1, pageSize, sort);
            Page<Category> categories = categoryDao.findAllByKeyword(keyword, pageable);
            List<Category> list = new ArrayList<>();
            for (Category category : categories) {
                list.add(category);
            }
            long total = categoryDao.countAllByKeyword(keyword);
            long totalPage = total / pageSize == 0 ? total / pageSize : total / pageSize + 1;
            return TableUtil.createTableDate(list, total, pageNum, totalPage, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL, "分页获取分类信息失败");
        }
    }

    /**
     * 多棵树，如果分类的parentID为空，那么这条分类就是一个根节点也就是一棵树的根节点
     *
     * @return
     */
    @RequestMapping(value = "/getTrees", method = RequestMethod.GET)
    @ApiOperation(value = "获取分类树", notes = "获取分类树")
    public String getTrees() {
        try {
            List<Category> categories = categoryDao.findAll(); //获取所有分类
            List<Category> roots = new ArrayList<>(); //找到所有根节点（parentId为空的分类）
            List<Category> list = new ArrayList<>();//除了根节点的所有节点
            List<Object> trees = new ArrayList<>();
            for (Category category : categories) {
                if ("".equals(category.getParentId()) || null == category.getParentId()) {
                    roots.add(category);
                } else {
                    list.add(category);
                }
            }
            for (Category category : roots) {
                JSONObject jsonObject = JSONObject.fromObject(category);//将根节点转换为json字符串
                String children = getChildren(list, category.getCategoryId()); //递归获子节点
                jsonObject.put("children", children);
                trees.add(jsonObject);
            }
            return JsonUtil.fromArray(trees);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL, "获取分类树失败");
        }
    }

    /**
     * 递归获取树结构
     *
     * @param list   所有子节点
     * @param rootId 根节点ID
     * @return
     * @throws Exception
     */
    public String getChildren(List<Category> list, String rootId) throws Exception {
        List<Object> result = new ArrayList<>();
        for (Category category : list) {
            JSONObject jsonObject = JSONObject.fromObject(category); //将子节点信息转换为json格式的字符串
            if (category.getParentId().equals(rootId)) {
                String children = getChildren(list, category.getCategoryId());//递归
                jsonObject.put("children", children);
                result.add(jsonObject);
            }
        }
        return JsonUtil.fromArray(result);
    }
}
