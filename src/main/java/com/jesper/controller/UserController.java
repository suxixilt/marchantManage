package com.jesper.controller;

import javax.servlet.http.HttpSession;

import com.jesper.mapper.UserMapper;
import com.jesper.model.User;

import com.jesper.util.GetIpAddress;
import com.jesper.util.MailUtils;
import com.jesper.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;



import java.util.Date;

/**
 * 用户管理
 */
@Controller
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private JavaMailSender mailSender; //自动注入的Bean

    @Value("${spring.mail.username}")
    private String Sender; //读取配置文件中的参数

    /**
     * 登录跳转
     *
     * @param model
     * @return
     */
    @GetMapping("/user/login")
    public String loginGet(Model model) {
        return "login";
    }

    /**
     * 登录
     *
     * @param
     * @param model
     * @param
     * @return
     */
    @PostMapping("/user/login")
    public String loginPost(User user, Model model) {
        User user1 = userMapper.selectByNameAndPwd(user);
        System.out.println(user);
        // 用户存在 并且账户已激活
        if (user1 != null && user1.getState() == 1) {
            // 存入session中
            httpSession.setAttribute("user", user1);
            User name = (User) httpSession.getAttribute("user");
            return "redirect:dashboard";
        } else if (user.getState() == 0) {
            model.addAttribute("error", "您的账户未激活，请登陆邮箱激活！");
            return "login";
        } else {
            model.addAttribute("error", "用户名或密码错误，请重新登录！");
            return "login";
        }
    }

    /**
     * 注册
     *
     * @param model
     * @return
     */
    @GetMapping("/user/register")
    public String register(Model model) {
        return "register";
    }

    /**
     * 注册
     *
     * @param model
     * @return
     */
    @PostMapping("/user/register")
    public String registerPost(User user, Model model) {
        System.out.println("用户名" + user.getUserName());
        try {
            userMapper.selectIsName(user);
            model.addAttribute("error", "该账号已存在！");
        } catch (Exception e) {
            Date date = new Date();
            user.setAddDate(date);
            user.setUpdateDate(date);
            user.setCode(UuidUtil.getUuid());
            userMapper.insert(user);
            System.out.println("注册成功");
            model.addAttribute("error", "请登录邮箱激活您的账号");
            String msg = "<a href='http://" + GetIpAddress.getRealIP() + ":8080/user/check?code=" + user.getCode() + "'>点击激活</a>你好，这是一封测试邮件，无需回复";
            MailUtils.sendMail(user.getEmail(), msg,"测试邮件");
            return "login";
        }
        // 如果注册失败 就重新注册
        return "register";
    }

    /**
     * 通过code码 激活用户账号
     * @param code
     * @return
     */
    @GetMapping("/user/check")
    public String check(String code,Model model){
        model.addAttribute("ipAddress",GetIpAddress.getRealIP());
        System.out.println(code);
        userMapper.updateStateByCode(code);
        // 进跳转
        return "check";
    }


    /**
     * 登录跳转
     *
     * @param model
     * @return
     */
    @GetMapping("/user/forget")
    public String forgetGet(Model model) {
        return "forget";
    }

    /**
     * 登录
     *
     * @param
     * @param model
     * @param
     * @return
     */
    @PostMapping("/user/forget")
    public String forgetPost(User user, Model model) {
        String password = userMapper.selectPasswordByName(user);
        if (password == null) {
            model.addAttribute("error", "帐号不存在或邮箱不正确！");
        } else {
            String email = user.getEmail();
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(Sender);
            message.setTo(email); //接收者邮箱
            message.setSubject("YX后台信息管理系统-密码找回");
            message.setText(user.getUserName() + "用户您好！您的注册密码是：" + password + "。感谢您使用YX信息管理系统！");
            mailSender.send(message);
            model.addAttribute("error", "密码已发到您的邮箱,请查收！");
        }
        return "forget";

    }

    @GetMapping("/user/userManage")
    public String userManageGet(Model model) {
        User user = (User) httpSession.getAttribute("user");
        User user1 = userMapper.selectByNameAndPwd(user);
        model.addAttribute("user", user1);
        return "user/userManage";
    }

    @PostMapping("/user/userManage")
    public String userManagePost(Model model, User user, HttpSession httpSession) {
        Date date = new Date();
        user.setUpdateDate(date);
        int i = userMapper.update(user);
        httpSession.setAttribute("user",user);
        return "redirect:userManage";
    }

}
