package com.beeant.mvc.controller;

import com.beeant.common.aop.annotation.LoginLog;
import com.beeant.common.utils.PropertiesUtil;
import com.beeant.common.utils.UserNamePassword;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Beeant on 2016/2/26.
 */
@Controller
@RequestMapping("app")
public class AppController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static int passwordRetry = Integer.parseInt(new PropertiesUtil("classpath:/app.properties").getProperty("shiro.passwordRetry"));

    @Autowired
    CacheManager cacheManager;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    @Qualifier(value = "shiroFilter")
    private ShiroFilterFactoryBean shiroFilter;

    @RequestMapping("login")
    @LoginLog(username = "username")
    public String login(Model model, UserNamePassword user, HttpServletRequest request, HttpServletResponse response) {
        model.addAttribute("user",user);
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return "redirect:/".concat(shiroFilter.getSuccessUrl());
        }

        String action = request.getParameter("action");
        if(null != action && "login".equals(action)){
            if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
                if (StringUtils.isEmpty(user.getPassword())) {
                    model.addAttribute("password", "密码不能为空");
                }
                if (StringUtils.isEmpty(user.getUsername())) {
                    model.addAttribute("username", "用户名不能为空");
                }
                return shiroFilter.getLoginUrl();
            }

            UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(),
                    user.getPassword(), request.getRemoteHost());

            token.setRememberMe(user.isRememberMe());
            try {
                subject.login(token);
                if (subject.isAuthenticated()) {
                    return "redirect:/".concat(shiroFilter.getSuccessUrl());
                }
            } catch (IncorrectCredentialsException e) {
                logger.debug("登录密码错误. " + token.getPrincipal());
                Cache<String, AtomicInteger> passwordRetryCache = cacheManager.getCache("passwordRetryCache");
                AtomicInteger retryCount = passwordRetryCache.get(user.getUsername());
                if (null != retryCount){
                    model.addAttribute("password", "登录密码错误，剩余 ".concat(Integer.toString(passwordRetry - retryCount.intValue())).concat(" 次"));
                }
            } catch (ExcessiveAttemptsException e) {
                Cache<String, AtomicInteger> passwordRetryCache = cacheManager.getCache("passwordRetryCache");
                AtomicInteger retryCount = passwordRetryCache.get(user.getUsername());
                logger.debug("登录失败次数过多，请稍后重试");
                model.addAttribute("password", "登录失败次数过多，请稍后重试");
            } catch (LockedAccountException e) {
                logger.debug("帐号已被锁定. " + token.getPrincipal());
                model.addAttribute("password", "帐号已被锁定");
            } catch (DisabledAccountException e) {
                logger.debug("帐号已被禁用. " + token.getPrincipal());
                model.addAttribute("password", "帐号已被禁用");
            } catch (ExpiredCredentialsException e) {
                logger.debug("帐号已过期. " + token.getPrincipal());
                model.addAttribute("password", "帐号已过期");
            } catch (UnknownAccountException e) {
                logger.debug("帐号不存在. " + token.getPrincipal());
                model.addAttribute("username", "帐号不存在");
            } catch (UnauthorizedException e) {
                logger.debug("您没有得到相应的授权！" + e.getMessage());
                model.addAttribute("username", "您没有得到相应的授权");
            }
        }

        return shiroFilter.getLoginUrl();
    }


    /**
     * 注销
     * @return
     */
    @RequestMapping("logout")
    public ModelAndView logout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            subject.logout(); // session 会销毁，在SessionListener监听session销毁，清理权限缓存
        }

        return new ModelAndView(shiroFilter.getSuccessUrl());
    }

    @RequestMapping("noPermission")
    public ModelAndView noPermission() {
        return new ModelAndView("common/error-page/noPermission");
    }

    @RequestMapping("unauthorized")
    public ModelAndView unauthorized() {
        return new ModelAndView("common/error-page/unauthorized");
    }

    @RequestMapping("index")
    public ModelAndView index() {
        return new ModelAndView("app/index");
    }
}
