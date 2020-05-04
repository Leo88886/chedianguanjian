package com.platform.controller;

import com.platform.entity.GoodsSpecificationEntity;
import com.platform.entity.OrderEntity;
import com.platform.entity.OrderGoodsEntity;
import com.platform.entity.SpecificationEntity;
import com.platform.service.GoodsSpecificationService;
import com.platform.service.OrderGoodsService;
import com.platform.service.OrderService;
import com.platform.service.SpecificationService;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-13 10:41:09
 */
@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private GoodsSpecificationService goodsSpecificationService;


    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("order:list")
    public R list(@RequestParam Map<String, Object> params) {
        // 查询列表数据
        Query query = new Query(params);

        List<OrderEntity> orderList = orderService.queryList(query);
        int total = orderService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(orderList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("order:info")
    public R info(@PathVariable("id") Integer id) {
        OrderEntity order = orderService.queryObject(id);
        Map map = new HashMap();
        map.put("orderId",order.getId());
        List<OrderGoodsEntity> orderGoods = orderGoodsService.queryOrderDateals(map);
        String orderGoodsDetails = "";
        if(null != orderGoods && orderGoods.size() > 0){
            for(OrderGoodsEntity orderGood : orderGoods){
                //名称
                if(null != orderGood.getGoodsName() && !orderGood.getGoodsName().equals("")){
                    orderGoodsDetails += orderGood.getGoodsName();
                }
                //规格
                List<String> idList = Arrays.asList(orderGood.getGoodsSpecifitionIds().split("_"));
                List<GoodsSpecificationEntity> list = goodsSpecificationService.queryListByIds(idList);       //查询规格
                if(null != list && list.size() >0){
                    for(int i=0;i<list.size();i++){
                        if(null != list.get(i)){
                            orderGoodsDetails += "-"+list.get(i).getValue();
                        }
                    }
                }
                //价格和数量
                if(null != orderGood.getRetailPrice() && !orderGood.getRetailPrice().equals("")){
                    orderGoodsDetails += "-"+orderGood.getRetailPrice()+"元x"+orderGood.getNumber()+",  ";
                }
            }
            order.setOrderGoodsDetails(orderGoodsDetails.substring(0,orderGoodsDetails.length()-1));
            getWeight(orderGoodsDetails.substring(0,orderGoodsDetails.length()-1));
            order.setWeight(getWeight(orderGoodsDetails.substring(0,orderGoodsDetails.length()-1)));
            order.setOrderAddress(order.getProvince()+"("+order.getCity()+")-"+order.getDistrict()+"-"+order.getAddress());
        }
        return R.ok().put("order", order);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("order:save")
    public R save(@RequestBody OrderEntity order) {
        orderService.save(order);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("order:update")
    public R update(@RequestBody OrderEntity order) {
        orderService.update(order);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("order:delete")
    public R delete(@RequestBody Integer[] ids) {
        orderService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<OrderEntity> list = orderService.queryList(params);

        return R.ok().put("list", list);
    }

    /**
     * 总计
     */
    @RequestMapping("/queryTotal")
    public R queryTotal(@RequestParam Map<String, Object> params) {
        int sum = orderService.queryTotal(params);

        return R.ok().put("sum", sum);
    }

    /**
     * 确定收货
     *
     * @param id
     * @return
     */
    @RequestMapping("/confirm")
    @RequiresPermissions("order:confirm")
    public R confirm(@RequestBody Integer id) {
        orderService.confirm(id);

        return R.ok();
    }

    /**
     * 发货
     *
     * @param order
     * @return
     */
    @RequestMapping("/sendGoods")
    @RequiresPermissions("order:sendGoods")
    public R sendGoods(@RequestBody OrderEntity order) {
        orderService.sendGoods(order);

        return R.ok();
    }


    public String  getWeight(String str) {
        String[] arr = str.split(",");
        String number ="";
        Double weightAll = 0.0;
        for(int i=0;i<arr.length;i++){
            if(arr[i].indexOf("x") > -1){
                number = arr[i].substring(arr[i].indexOf("x")+1); //截取数量

            }
            double num = Double.parseDouble(number);
            double weight = getNumber(arr[i]);  //截取规格（重量）
            if(weight != 0) {
                weightAll += num * weight;
            }
        }
        return weightAll+"L";
    }

    /**
     * 截取字符转中2个字符之间的内容
     * @param str
     * @return
     */
    public double  getNumber(String str) {
        if(str.indexOf("2*20") > -1 || str.indexOf("2x20") > -1){
            return 40;
        }
        String regex="-(.*?)-";
        Pattern p=Pattern.compile(regex);
        Matcher m=p.matcher(str);
        while(m.find()){
            return getNumber2(m.group(1));
        }
        return 0.0;
    }

    /**
     * 截取字符串中的数据
     * @param str
     * @return
     */
    public double  getNumber2(String str) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        if(str.indexOf("ML") > -1 || str.indexOf("mL") > -1  || str.indexOf("Ml") > -1  || str.indexOf("ml") > -1 ){
            return Double.parseDouble(m.replaceAll("").trim()) / 1000;
        }else{
            return Double.parseDouble(m.replaceAll("").trim());
        }

    }

}
