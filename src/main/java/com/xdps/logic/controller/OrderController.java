package com.xdps.logic.controller;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.xdps.logic.dao.OrderDao;
import com.xdps.logic.dao.OrderFoodDao;
import com.xdps.logic.domain.Order;
import com.xdps.logic.domain.OrderFood;
import com.xdps.logic.util.DateUtil;
import com.xdps.logic.util.JsonUtil;
import com.xdps.logic.util.SendMessage;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping(value = "addOrder", method = RequestMethod.POST)
    @ApiOperation(value = "新增订单", notes = "新增订单")
    @Transactional//开启事务
    public String addOrder(@RequestParam Object obj) {
        try {
            String orderId = JsonUtil.getString("orderId", obj);
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
            order.setState(Integer.parseInt(JsonUtil.getString("state", obj)));
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
            jsonObject.put("phone", "13826143050");
            SendSmsResponse response = SendMessage.sendSms(receiverPhone, "归兰海科技", "SMS_89665019", jsonObject.toString());

            return JsonUtil.returnStr(JsonUtil.SUCCESS, "新增订单成功");
        } catch (Exception e) {
            e.printStackTrace();
            //因为sping 默认trycatch是不进行事务回滚的，可以在手动设置事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return JsonUtil.returnStr(JsonUtil.FAIL, "订单提交失败");
        }
    }
}
