package com.suns.lottery.tball.controller;

import com.suns.lottery.tball.bean.User;
import com.suns.lottery.tball.bean.vo.ThirdCallBack;
import com.suns.lottery.tball.bean.vo.UserVo;
import com.suns.lottery.tball.common.JsonResult;
import com.suns.lottery.tball.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @title: lottery-tball
 * @description:
 * @author: sunxiaobo
 * @create: 2020-07-29 18:25
 **/
@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @ResponseBody
    @PostMapping("/login/{username}/{password}")
    public JsonResult login(@PathVariable("username") String username, @PathVariable("password")String password){
        User user = accountService.checkPassword(username,password);
        return user!=null?JsonResult.success(UserVo.from(user)):JsonResult.fail("用户名或密码不正确");
    }

    @ResponseBody
    @PostMapping("/logout/{uid}")
    public JsonResult logout(@PathVariable("uid") String uid){
        User user = accountService.findUser(uid);
        //TODO 删除用户登录状态
        return JsonResult.success();
    }


    @ResponseBody
    @PostMapping("/register")
    public JsonResult register(User user){
        accountService.addUser(user);
        return JsonResult.success();
    }


    /**
    * @Description: 登录或注册
    * @Param:  [type, user]
    * @Return:  com.suns.lottery.tball.common.JsonResult
    * @Author:  sunxiaobo
    * @Date:  19:06 2020/7/29
    **/
    @ResponseBody
    @PostMapping("/thirdAuth/{type}")
    public JsonResult thirdAuth(@PathVariable("type") String type, ThirdCallBack callBack){
        User check = accountService.findUser(callBack.getUserNo());
        if(check!=null){//登录
            return JsonResult.success(UserVo.from(check));
        }else{//第三方授权回调,注册并登录
            User user = User.builder()

                    .build();
            accountService.addUser(user);
            return JsonResult.success(UserVo.from(user));
        }
    }

    /**
     * @Description: 登录或注册
     * @Param:  [type, user]
     * @Return:  com.suns.lottery.tball.common.JsonResult
     * @Author:  sunxiaobo
     * @Date:  19:06 2020/7/29
     **/
    @ResponseBody
    @PostMapping("/resetPwd")
    public JsonResult resetPwd(String uid,String npwd,String validCode){
        accountService.updateUser(User.builder()
                .uid(uid)
                .password(npwd)
                .build());
        return JsonResult.success();
    }

}
