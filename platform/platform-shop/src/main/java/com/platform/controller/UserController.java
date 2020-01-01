package com.platform.controller;

import com.platform.entity.UserEntity;
import com.platform.service.SaleService;
import com.platform.service.UserService;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;
import com.platform.utils.StringUtils;
import com.platform.utils.excel.ExcelExport;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-16 15:02:28
 */
@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private SaleService saleService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("user:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<UserEntity> userList = userService.queryList(query);
        int total = userService.queryTotal(query);

        if (CollectionUtils.isEmpty(userList)) {
            PageUtils pageUtil = new PageUtils(userList, total, query.getLimit(), query.getPage());
            return R.ok().put("page", pageUtil);
        }

        List<UserEntity> AllSalerNumList = new ArrayList<>();
        List<UserEntity> MonthSalerNumList = new ArrayList();
        try {
            Calendar cale = null;
            cale = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String firstday, lastday;
            // 获取本月的第一天
            cale = Calendar.getInstance();
            cale.add(Calendar.MONTH, 0);
            cale.set(Calendar.DAY_OF_MONTH, 1);
            firstday = format.format(cale.getTime());
            // 获取本月的最后一天
            cale = Calendar.getInstance();
            cale.add(Calendar.MONTH, 1);
            cale.set(Calendar.DAY_OF_MONTH, 0);
            lastday = format.format(cale.getTime());
            AllSalerNumList = userService.querySalesAllBySaler();
            MonthSalerNumList = userService.querySalesMonBySaler(firstday, lastday);
        } catch (Exception e) {
            System.out.println("查询销售全量、当月数量出错" + e);
            PageUtils pageUtil = new PageUtils(userList, total, query.getLimit(), query.getPage());
            return R.ok().put("page", pageUtil);
        }

        try {
            // key:salerId value:user实体
            Map<String, List<UserEntity>> salerIdMap =
                    userList.stream().collect(Collectors.groupingBy(UserEntity::getSalerId));
            //为销售设置全量推广用户数
            if (CollectionUtils.isNotEmpty(AllSalerNumList)) {
                for (UserEntity userAllNum : AllSalerNumList) {
                    if(StringUtils.isNullOrEmpty(userAllNum.getSalerId())){
                        continue;
                    }
                    List<UserEntity> userEntities = salerIdMap.get(userAllNum.getSalerId());
                    for (UserEntity user: userEntities) {
                        user.setSalesAll(userAllNum.getSalesAll());
                    }
                }
            }

            //为销售设置当月推广用户数
            if (CollectionUtils.isNotEmpty(MonthSalerNumList)) {
                for (UserEntity userMonthNum : MonthSalerNumList) {
                    if(StringUtils.isNullOrEmpty(userMonthNum.getSalerId())){
                        continue;
                    }
                    List<UserEntity> userEntities = salerIdMap.get(userMonthNum.getSalerId());
                    for (UserEntity user: userEntities) {
                        user.setSalesMon(userMonthNum.getSalesMon());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("为销售设置分销用户数出错" + e);
            PageUtils pageUtil = new PageUtils(userList, total, query.getLimit(), query.getPage());
            return R.ok().put("page", pageUtil);
        }

        PageUtils pageUtil = new PageUtils(userList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("user:info")
    public R info(@PathVariable("id") Integer id) {
        UserEntity user = userService.queryObject(id);
        if(null != user.getSalerId() && !"".equals(user.getSalerId())){
            user.setSalerIdFlag("1");            //是销售
        }else {
            user.setSalerIdFlag("2");            //不是销售
        }
        return R.ok().put("user", user);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("user:save")
    public R save(@RequestBody UserEntity user) {
        userService.save(user);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("user:update")
    public R update(@RequestBody UserEntity user) {
        if(user.getSalerIdFlag().equals("1")){  //是否为销售 1为是 2为否
            saleService.save(user.getWeixinOpenid());
        }else{
            saleService.delete(user.getWeixinOpenid());
        }
        userService.update(user);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("user:delete")
    public R delete(@RequestBody Integer[] ids) {
        userService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<UserEntity> userList = userService.queryList(params);

        return R.ok().put("list", userList);
    }

    /**
     * 总计
     */
    @RequestMapping("/queryTotal")
    public R queryTotal(@RequestParam Map<String, Object> params) {
        int sum = userService.queryTotal(params);

        return R.ok().put("userSum", sum);
    }

    /**
     * 导出会员
     */
    @RequestMapping("/export")
    @RequiresPermissions("user:export")
    public R export(@RequestParam Map<String, Object> params, HttpServletResponse response) {

        List<UserEntity> userList = userService.queryList(params);

        ExcelExport ee = new ExcelExport("会员列表");

        String[] header = new String[]{"会员名称", "性别", "会员级别", "手机号码"};

        List<Map<String, Object>> list = new ArrayList<>();

        if (userList != null && userList.size() != 0) {
            for (UserEntity userEntity : userList) {
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("USERNAME", userEntity.getUsername());
                map.put("GENDER", userEntity.getGender() == 1 ? "男" : (userEntity.getGender() == 2 ? "女" : "未知"));
                map.put("LEVEL_NAME", userEntity.getLevelName());
                map.put("MOBILE", userEntity.getMobile());
                list.add(map);
            }
        }

        ee.addSheetByMap("会员", list, header);
        ee.export(response);
        return R.ok();
    }
}
