package com.suns.lottery.tball.bean.vo;

import com.suns.lottery.tball.bean.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @title: lottery-tball
 * @description:
 * @author: sunxiaobo
 * @create: 2020-07-29 18:39
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVo {
    private String uid;
    private String username;
    private String wechart;
    private String userGroup;
    private String authorityId;
    private String mobile;
    private String avatar;
    private Integer status;
    private Integer vip;


    public static UserVo from(User user){
        return UserVo.builder()
                .uid(user.getUid())
                .username(user.getUsername())
                .wechart(user.getWechart())
                .userGroup(user.getUserGroup())
                .authorityId(user.getAuthorityId())
                .mobile(user.getMobile())
                .avatar(user.getAvatar())
                .vip(user.getVip())
                .status(user.getStatus())
                .build();
    }


    public User to(){
        return User.builder()
                .uid(this.getUid())
                .username(this.getUsername())
                .wechart(this.getWechart())
                .userGroup(this.getUserGroup())
                .authorityId(this.getAuthorityId())
                .mobile(this.getMobile())
                .avatar(this.getAvatar())
                .build();
    }

}
