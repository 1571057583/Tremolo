package com.it.controller;

import com.it.pojo.Users;
import com.it.pojo.VO.UsersVO;
import com.it.service.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.it.utils.MD5Utils;
import com.it.utils.ResultVO;

import java.util.List;
import java.util.UUID;


@RestController
@Api(value = "用户接口")
public class RegistLoginController extends BaseController{
    @Autowired
    private UsersService usersService;

    @ApiOperation(value = "用户注册",notes = "用户注册接口")
    @RequestMapping(value ="/regist",method = RequestMethod.POST)
    public ResultVO regist(@RequestBody Users users) throws Exception{

        if(StringUtils.isBlank( users.getUsername())||StringUtils.isBlank(users.getPassword())){
            return ResultVO.errorMsg("账号密码不能为空");
        }
        List<Users> username = usersService.findByUsername(users.getUsername());

        if(username.size()!=0){
            return ResultVO.errorMsg("账号已存在");
        }else{
            users.setNickname(users.getUsername());
            users.setPassword(MD5Utils.getMD5Str(users.getPassword()));
            users.setUsername(users.getUsername());
            usersService.save(users);
        }
        users.setPassword("");
        UsersVO usersVO = usersRedis(users);
        return ResultVO.ok(usersVO);
    }

    @ApiModelProperty(value = "用户登录",notes = "用户登录接口")
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ResultVO login(@RequestBody Users users) throws Exception{

        String username=users.getUsername();
        String password=users.getPassword();

        if(StringUtils.isBlank(username)||StringUtils.isBlank(password)){
            return ResultVO.errorMsg("账号密码不能为空");
        }
        List<Users> users2 = usersService.findByUsername(username);
        if(users2==null){
            return ResultVO.errorMsg("账号不存在");
        }else {
            users.setPassword(MD5Utils.getMD5Str(password));
            Users users1 = usersService.findByUsernameAndPassword(username, users.getPassword());
            if(users1==null){
                return ResultVO.errorMsg("登录失败");
            }else {
                users1.setPassword("");
                UsersVO usersVO = usersRedis(users1);
                return  ResultVO.ok(usersVO);
            }
        }
    }

    @ApiModelProperty(value = "用户注销",notes = "用户注销接口")
    @ApiImplicitParam(value = "用户id",name = "userId",dataType = "String",paramType = "query")
    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    public ResultVO logout(String userId) throws Exception{

            redis.del(USER_REDIS_SESSION+":"+userId);
                return  ResultVO.ok("注销成功");
            }

    public  UsersVO usersRedis(Users users){

        String Token = UUID.randomUUID().toString();
        redis.set(USER_REDIS_SESSION + ":" + users.getId(), Token, 1000 * 60 * 30);
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(users,usersVO);
        usersVO.setUserToken(Token);

        return usersVO;
    }
}
