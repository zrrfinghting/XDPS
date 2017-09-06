package com.xdps.logic.controller;

import com.xdps.logic.dao.LogDao;
import com.xdps.logic.dao.UserDao;
import com.xdps.logic.domain.Log;
import com.xdps.logic.domain.User;
import com.xdps.logic.util.DateUtil;
import com.xdps.logic.util.JsonUtil;
import com.xdps.logic.util.MD5Util;
import com.xdps.logic.util.TableUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户逻辑实现、对外暴露接口类
 *
 * @autherAdmin Deram Zhao
 * @creat 2017/8/25
 */

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserDao userDao;
    @Autowired
    private LogDao logDao;
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "addUser", method = RequestMethod.POST)
    @ApiOperation(value = "新增用户", notes = "新增用户")
    public String addUser(@RequestBody User user) {
        try {
            user.setCreateDate(new Date());
            userDao.save(user);
            //记录日志
            logDao.save(new Log("", 2, "新增用户成功", new Date()));
            return JsonUtil.returnStr(JsonUtil.SUCCESS, "新增用户" + user.getUserName() + "成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL, "新增用户失败");
        }
    }

    @RequestMapping(value = "eidtUserInfo", method = RequestMethod.POST)
    @ApiOperation(value = "修改用户信息", notes = "修改用户信息")
    public String eidtUserInfo(@RequestBody User user) {
        try {
            User oldUser = userDao.findByUserId(user.getUserId());
            if (null == oldUser)
                return JsonUtil.returnStr(JsonUtil.FAIL, "修改用户不存在");
            user.setUpdateDate(new Date());
            userDao.save(user);
            //记录日志
            logDao.save(new Log("", 3, "修改用户信息成功", new Date()));
            return JsonUtil.returnStr(JsonUtil.SUCCESS, "修改用户信息成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL, "修改用户信息失败");
        }
    }

    @RequestMapping(value = "getUserById", method = RequestMethod.GET)
    @ApiOperation(value = "通过用户ID获取用户信息", notes = "通过用户ID获取用户信息")
    public String getUserById(@RequestParam String userId) {
        try {
            User user = userDao.findByUserId(userId);
            return JsonUtil.fromObject(user);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL, "获取用户信息失败");
        }
    }

    @RequestMapping(value = "deleteById", method = RequestMethod.DELETE)
    @ApiOperation(value = "通过用户ID删除用户信息", notes = "通过用户ID删除用户信息")
    public String deleteById(@RequestParam String userId) {
        try {
            userDao.deleteByUserId(userId);
            //记录日志
            logDao.save(new Log("", 2, "删除用户成功", new Date()));
            return JsonUtil.returnStr(JsonUtil.FAIL, "删除用户信息成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL, "删除用户信息失败");
        }
    }

    @RequestMapping(value = "deleteByIds", method = RequestMethod.DELETE)
    @ApiOperation(value = "通过用户ID删除用户信息", notes = "通过用户ID删除用户信息")
    public String deleteByIds(@RequestParam String[] userIds) {
        try {
            List<User> users = new ArrayList<>();
            String deleteIds = "";
            String unDeleteIds = "";
            for (int i = 0; i < userIds.length; i++) {
                User user = userDao.findByUserId(userIds[i]);
                if (null == user) {
                    unDeleteIds += userIds[i] + ",";
                } else {
                    users.add(user);
                    deleteIds += userIds[i] + ",";
                }
            }
            userDao.delete(users);
            if (unDeleteIds.equals("") && deleteIds.length() > 0) {
                //记录日志
                logDao.save(new Log("", 4, "批量删除用户成功", new Date()));
                return JsonUtil.returnStr(JsonUtil.SUCCESS, "批量删除用户" + deleteIds + "信息成功");
            } else {
                //记录日志
                logDao.save(new Log("", 4, "编号是" + deleteIds + "的用户已经删除，编号为" + unDeleteIds + "的用户不存在删除失败", new Date()));
                return JsonUtil.returnStr(JsonUtil.SUCCESS, "编号是" + deleteIds + "的用户已经删除成功，编号为" + unDeleteIds + "的用户不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL, "批量删除用户信息失败");
        }
    }

    @RequestMapping(value = "getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取用户", notes = "分页获取用户（可以根据keyword进行模糊查询）")
    public String getByPage(
            @RequestParam String keyword,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize) {
        try {
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            Pageable pageable = new PageRequest(pageNum - 1, pageSize, sort);
            Page<User> users = userDao.findAllByKeyword(keyword, pageable);
            List<User> list = new ArrayList<>();
            for (User user : users) {
                list.add(user);
            }
            long total = userDao.countAllByKeyword(keyword);
            long totalPage = total / pageSize == 0 ? total / pageSize : total / pageSize + 1;
            return TableUtil.createTableDate(list, total, pageNum, totalPage, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL, "获取用户信息失败");
        }

    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ApiOperation(value = "登录系统", notes = "登录系统")
    public String login(@RequestParam Object obj) {
        try {
            String userId = JsonUtil.getString("userId", obj);
            String password = JsonUtil.getString("password", obj);
            User oldUser = userDao.findByUserId(userId);
            if (null == oldUser)
                return JsonUtil.returnStr(JsonUtil.FAIL, "用户不存在！");
            if (oldUser.getState() == 0)
                return JsonUtil.returnStr(JsonUtil.FAIL, "用户已经停用不能登录系统");
            if (oldUser.getPassword().equals(MD5Util.getMd5(userId, password))) {
                //记录日志
                logDao.save(new Log("", 5, "登录成功", new Date()));
                return JsonUtil.returnStr(JsonUtil.SUCCESS, "登录成功");
            } else {
                if (oldUser.getErrorTime() > 6) {
                    oldUser.setState(0);//停用用户
                    userDao.save(oldUser);
                    return JsonUtil.returnStr(JsonUtil.FAIL, "连续输错密码六次用户已经被停用");
                } else {
                    oldUser.setErrorTime(oldUser.getErrorTime() + 1);//记录输错密码次数
                    userDao.save(oldUser);
                }
                return JsonUtil.returnStr(JsonUtil.FAIL, "密码错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL, "登录失败");
        }
    }

    @RequestMapping(value = "editPassword", method = RequestMethod.POST)
    @ApiOperation(value = "修改密码", notes = "修改密码")
    public String editPassword(@RequestBody Object obj) {
        try {
            String userId = JsonUtil.getString("userId", obj);
            String password = JsonUtil.getString("password", obj);
            String newPassword = JsonUtil.getString("newPassword", obj);

            User user = userDao.findByUserId(userId);
            if (user.getPassword().equals(MD5Util.getMd5(userId, password))) {
                user.setPassword(MD5Util.getMd5(userId, newPassword));
                //记录日志
                logDao.save(new Log("", 3, "修改密码成功", new Date()));
                return JsonUtil.returnStr(JsonUtil.SUCCESS, "修改密码成功");
            } else {
                return JsonUtil.returnStr(JsonUtil.FAIL, "旧密码不正确，修改密码失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL, "修改密码失败");
        }
    }

    @RequestMapping(value = "restPassword", method = RequestMethod.POST)
    @ApiOperation(value = "找回密码", notes = "找回密码")
    public String restPassword(@RequestBody Object object) {
        try {
            String userId = JsonUtil.getString("userId", object);
            String password = JsonUtil.getString("password", object);
            String verifyCode = JsonUtil.getString("verifyCode", object);
            User user = userDao.findByUserId(userId);
            if (null == user)
                return JsonUtil.returnStr(JsonUtil.FAIL, "用户不存在");
            String oldVerifyCode = redisTemplate.opsForValue().get(userId).toString();
            if ("".equals(oldVerifyCode))
                return JsonUtil.returnStr(JsonUtil.FAIL, "验证码失效");
            if (verifyCode.equals(oldVerifyCode)) {
                //重置密码
                user.setPassword(MD5Util.getMd5(userId, password));
                userDao.save(user);
                //记录日志
                logDao.save(new Log("", 3, "重置密码成功", new Date()));
                return JsonUtil.returnStr(JsonUtil.SUCCESS, "重置密码成功，可重新登录");
            } else {
                return JsonUtil.returnStr(JsonUtil.FAIL, "验证码错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL, "找回密码失败");
        }
    }

    @RequestMapping(value = "registerUser", method = RequestMethod.POST)
    @ApiOperation(value = "注册用户", notes = "注册用户")
    public String registerUser(@RequestBody User user) {
        try {
            //用户的注册后端就不校验验证码了
            user.setCreateDate(new Date());
            userDao.save(user);
            //记录日志
            logDao.save(new Log("", 3, "注册用户成功", new Date()));
            return JsonUtil.returnStr(JsonUtil.SUCCESS, "注册用户成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL, "注册用户失败");
        }
    }
}
