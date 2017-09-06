package com.xdps.logic.controller;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.xdps.logic.dao.FoodDao;
import com.xdps.logic.dao.LogDao;
import com.xdps.logic.dao.OrderDao;
import com.xdps.logic.dao.OrderFoodDao;
import com.xdps.logic.domain.Food;
import com.xdps.logic.domain.Log;
import com.xdps.logic.domain.Order;
import com.xdps.logic.domain.OrderFood;
import com.xdps.logic.util.DateUtil;
import com.xdps.logic.util.JsonUtil;
import com.xdps.logic.util.SendMessage;
import com.xdps.logic.util.TableUtil;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 订单对外发布类
 *
 * @autherAdmin Deram Zhao
 * @creat 2017/9/1
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderFoodDao orderFoodDao;
    @Autowired
    private FoodDao foodDao;
    @Autowired
    private LogDao logDao;

    @RequestMapping(value = "addOrder", method = RequestMethod.POST)
    @ApiOperation(value = "新增订单", notes = "新增订单")
    @Transactional//开启事务
    public String addOrder(@RequestParam Object obj) {
        try {
            //String orderId = JsonUtil.getString("orderId", obj);
            String orderId = DateUtil.formatDate(DateUtil.FORMAT2, new Date()) + System.currentTimeMillis();
            String foods = JsonUtil.getString("foods", obj);
            String receiverPhone = JsonUtil.getString("receiverPhone", obj);
            String endTime = JsonUtil.getString("endTime", obj);

            Order order = new Order();
            order.setOrderId(orderId);
            order.setReceiver(JsonUtil.getString("receiver", obj));
            order.setReceiverPhone(receiverPhone);
            order.setAddress(JsonUtil.getString("address", obj));
            order.setDelivery(JsonUtil.getString("delivery", obj));
            order.setDeliveryPhone(JsonUtil.getString("deliveryPhone", obj));
            order.setStarTime(DateUtil.parse(DateUtil.FORMAT2, JsonUtil.getString("starTime", obj)));
            order.setEndTime(DateUtil.parse(DateUtil.FORMAT2, endTime));
            order.setState(Integer.parseInt(JsonUtil.getString("state", obj)));//-1--取消订单，0--未付款，1--未配送，2--配送中，3--配送完成
            order.setCreateUser("");
            order.setCreateDate(new Date());
            orderDao.save(order);

            List<Object> food_list = JsonUtil.getList(foods);
            List<OrderFood> orderFoods = new ArrayList<>();
            for (Object object : food_list) {
                OrderFood orderFood = new OrderFood();
                orderFood.setOrderId(orderId);
                orderFood.setFoodId(JsonUtil.getString("foodId", object));
                orderFood.setNumber(Integer.parseInt(JsonUtil.getString("number", object)));
                orderFood.setCreateUser("");
                orderFood.setCreateDate(new Date());
                orderFoods.add(orderFood);
            }
            orderFoodDao.save(orderFoods);
            //发送短信通知下单成功
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", "VIP客户");
            jsonObject.put("time", endTime);
            jsonObject.put("phone", "18300072030");
            SendSmsResponse response = SendMessage.sendSms(receiverPhone, "归兰海科技", "SMS_89665019", jsonObject.toString());
            //记录日志
            logDao.save(new Log("", 2, "新增订单:" + orderId + "成功", new Date()));

            return JsonUtil.returnStr(JsonUtil.SUCCESS, "新增订单成功");
        } catch (Exception e) {
            e.printStackTrace();
            //因为sping 默认trycatch是不进行事务回滚的，可以在手动设置事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return JsonUtil.returnStr(JsonUtil.FAIL, "订单提交失败");
        }
    }

    /**
     * 修改订单（向订单里添加或者删除货物  operate为add或者remove）
     *
     * @param obj
     * @return
     */
    @RequestMapping(value = "editOrder", method = RequestMethod.POST)
    @ApiOperation(value = "修改订单", notes = "修改订单信息")
    public String editOrder(@RequestParam Object obj) {
        try {
            String orderId = JsonUtil.getString("orderId", obj);
            String foodId = JsonUtil.getString("foodId", obj);
            String number = JsonUtil.getString("number", obj);
            String operate = JsonUtil.getString("oprate", obj);
            if ("add".equals(operate)) {
                OrderFood orderFood = new OrderFood();
                orderFood.setOrderId(orderId);
                orderFood.setFoodId(foodId);
                orderFood.setNumber(Integer.parseInt(number));
                orderFoodDao.save(orderFood);
                //记录日志
                logDao.save(new Log("", 3, "订单:" + orderId + "新增货物" + foodId + ":" + number + "kg成功", new Date()));
            } else if ("remove".equals(operate)) {
                orderFoodDao.deleteByFoodId(foodId);
                //记录日志
                logDao.save(new Log("", 4, "删除订单:" + orderId + "的编号为" + foodId + "的货物成功", new Date()));
            } else if ("edit".equals(operate)) {
                OrderFood orderFood = orderFoodDao.findByOrderIdAndFoodId(orderId, foodId);
                orderFood.setNumber(Integer.parseInt(number));
                orderFood.setUpdateDate(new Date());
                orderFoodDao.save(orderFood);
                //记录日志
                logDao.save(new Log("", 3, "修改订单:" + orderId + "的编号为" + foodId + "的货物" + orderFood.getNumber() + "kg为" + number + "kg成功", new Date()));
            } else {
                return JsonUtil.returnStr(JsonUtil.FAIL, "请选择修改订单的operate类型，修改失败！");
            }
            return JsonUtil.returnStr(JsonUtil.SUCCESS, "修改订单成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL, "修改订单失败");
        }
    }

    /**
     * 修改订单的状态  打印订单后就将订单状态改为2 配送中
     * -1--取消订单，0--未付款，1--未配送，2--配送中，3--配送完成
     *
     * @param obj
     * @return
     */
    @RequestMapping(value = "changState", method = RequestMethod.GET)
    @ApiOperation(value = "改变订单的状态", notes = "改变订单的状态")
    public String changState(@RequestParam Object obj) {
        try {
            String orderId = JsonUtil.getString("orderId", obj);
            int state = Integer.parseInt(JsonUtil.getString("state", obj));
            Order order = orderDao.findByOrderId(orderId);
            if (state == -1) {
                if (order.getState() == 1) {
                    order.setState(state);
                    order.setUpdateDate(new Date());
                    orderDao.save(order);
                    //记录日志
                    logDao.save(new Log("", 3, "修改订单:" + orderId + "状态" + order.getState() + "为：" + state + "成功", new Date()));
                    return JsonUtil.returnStr(JsonUtil.SUCCESS, "您的订单已经取消成功，订单退款正在处理中请耐心等待");
                } else if (order.getState() == 2) {
                    return JsonUtil.returnStr(JsonUtil.FAIL, "您的订单已经在配送中取消失败，请联系商家进行处理");
                }
            } else {
                order.setState(state);
                order.setUpdateDate(new Date());
                orderDao.save(order);
                //记录日志
                logDao.save(new Log("", 3, "修改订单:" + orderId + "状态" + order.getState() + "为：" + state + "成功", new Date()));
                return JsonUtil.returnStr(JsonUtil.SUCCESS, "修改订单状态成功");
            }
            return JsonUtil.returnStr(JsonUtil.FAIL, "修改订单状态失败");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL, "修改订单状态失败");
        }
    }

    /**
     * 根据ID删除订单信息 （状态为未付款的 state ==0）
     *
     * @param orderId
     * @return
     */
    @RequestMapping(value = "deleteById", method = RequestMethod.DELETE)
    @ApiOperation(value = "根据ID删除订单", notes = "根据ID删除订单信息")
    public String deleteById(@RequestParam String orderId) {
        try {
            orderDao.deleteByOrderId(orderId);
            //记录日志
            logDao.save(new Log("", 4, "删除订单:" + orderId + "成功", new Date()));
            return JsonUtil.returnStr(JsonUtil.SUCCESS, "删除订单成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL, "删除订单失败");
        }
    }

    @RequestMapping(value = "deleteByIds", method = RequestMethod.DELETE)
    @ApiOperation(value = "根据ID批量删除订单", notes = "根据ID批量删除订单信息")
    public String deleteByIds(@RequestParam String[] orderIds) {
        try {
            String deleteIds = "";
            String unDeleteIds = "";
            List<Order> orders = new ArrayList<>();
            for (int i = 0; i < orderIds.length; i++) {
                Order order = orderDao.findByOrderId(orderIds[i]);
                if (order.getState() == 0) {
                    orders.add(order);
                    deleteIds += orderIds[i] + ",";
                } else {
                    unDeleteIds += orderIds[i];
                }
            }
            orderDao.delete(orders);
            if ("".equals(deleteIds)) {
                return JsonUtil.returnStr(JsonUtil.FAIL, "批量删除订单失败");
            } else if ("".equals(unDeleteIds)) {
                //记录日志
                logDao.save(new Log("", 4, "删除订单:" + deleteIds + "成功", new Date()));
                return JsonUtil.returnStr(JsonUtil.SUCCESS, "批量删除订单信息成功");
            } else {
                //记录日志
                logDao.save(new Log("", 4, "删除订单:" + deleteIds + "成功，订单" + unDeleteIds + "不存在未能删除", new Date()));
                return JsonUtil.returnStr(JsonUtil.SUCCESS, "删除订单:" + deleteIds + "成功，订单" + unDeleteIds + "不存在未能删除");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL, "批量删除订单失败");
        }
    }

    @RequestMapping(value = "getOrderById", method = RequestMethod.GET)
    @ApiOperation(value = "根据Id获取订单信息", notes = "根据Id获取订单信息")
    public String getOrderById(@RequestParam String orderId) {
        try {
            Order order = orderDao.findByOrderId(orderId);
            JSONObject json = JSONObject.fromObject(order);
            List<OrderFood> orderFoods = orderFoodDao.findByOrderId(orderId);
            List<Object> foods = new ArrayList<>();
            double orderPrice = 0;//订单金额
            for (OrderFood orderFood : orderFoods) {
                String foodId = orderFood.getFoodId();
                Food food = foodDao.findByFoodId(foodId);
                double price = food.getPrice();
                double number = orderFood.getNumber();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("foodId", foodId);
                jsonObject.put("foodName", food.getFoodName());
                jsonObject.put("price", price);
                jsonObject.put("number", number);
                foods.add(jsonObject);
                orderPrice += price * number;
            }
            JSONArray foods_arr = JSONArray.fromObject(foods);
            json.put("price", orderPrice);
            json.put("foods", foods_arr);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL, "获取订单信息失败");
        }
    }


    /**
     * 分页获取订单状态
     *
     * @param createUser 用户ID
     * @param keyword    关键字
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param state      状态  state==-2表示查全部状态
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "getByPage", method = RequestMethod.POST)
    @ApiOperation(value = "根据Id获取订单信息", notes = "根据Id获取订单信息")
    public String getByPage(
            @RequestParam String createUser,
            @RequestParam String keyword,
            @RequestParam String startTime,
            @RequestParam String endTime,
            @RequestParam int state,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize) {
        try {

            List<Order> list = new ArrayList<>();
            Page<Order> orders = null;
            long total = 0;
            long totalPage = 0;
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            Pageable pageable = new PageRequest(pageNum - 1, pageSize, sort);
            //根据keyword分页查询
            if ("".equals(createUser) && "".equals(startTime) && "".equals(endTime) && state == -2) {
                orders = orderDao.findAllByKeyword(keyword, pageable);
                total = orderDao.countAllByKeyword(keyword);
            }
            //根据createUser 、keyword分页查询
            if (!"".equals(createUser) && "".equals(startTime) && "".equals(endTime) && state == -2) {
                orders = orderDao.findAllByKeywordAnAndCreateUser(keyword, createUser, pageable);
                total = orderDao.countAllByKeywordAndCreateUser(keyword, createUser);
            }
            //根据createUser 、keyword、state分页查询
            if (!"".equals(createUser) && "".equals(startTime) && "".equals(endTime)) {
                orders = orderDao.findAllByKeywordAnAndCreateUserAndState(keyword, createUser, state, pageable);
                total = orderDao.countAllByKeywordAndCreateUserAndState(keyword, createUser, state);
            }
            //根据createUser 、startTime、endTime分页查询
            if (!"".equals(createUser) && !"".equals(startTime) && !"".equals(endTime) && state == -2) {
                orders = orderDao.findAllByCreateUserAndStarTimeAndEndTime(createUser, startTime, endTime, pageable);
                total = orderDao.countAllByCreateUserAndStarTimeAndEndTime(createUser, startTime, endTime);
            }
            //根据createUser 、startTime、endTime、state分页查询
            if (!"".equals(createUser) && !"".equals(startTime) && !"".equals(endTime)) {
                orders = orderDao.findAllByCreateUserAndStarTimeAndEndTimeAndState(createUser, startTime, endTime, state, pageable);
                total = orderDao.countAllByCreateUserAndStarTimeAndEndTimeaAndState(createUser, startTime, endTime, state);
            }

            for (Order order : orders) {
                list.add(order);
            }
            totalPage = total / pageSize == 0 ? total / pageSize : total / pageSize + 1;
            return TableUtil.createTableDate(list, total, pageNum, totalPage, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL, "分页获取订单失败");
        }
    }
}
