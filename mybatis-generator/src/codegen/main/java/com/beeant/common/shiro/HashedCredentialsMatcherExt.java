package com.beeant.common.shiro;


import com.beeant.common.utils.PropertiesUtil;
import com.beeant.dto.AppUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 登录次数限制
 *
 * Created by Beeant on 2016/3/8.
 */
public class HashedCredentialsMatcherExt extends HashedCredentialsMatcher {
    private Cache<String, AtomicInteger> passwordRetryCache;

    private static int passwordRetry = Integer.parseInt(new PropertiesUtil("classpath:/app.properties").getProperty("shiro.passwordRetry"));

    public HashedCredentialsMatcherExt(CacheManager cacheManager) {
        passwordRetryCache = cacheManager.getCache("passwordRetryCache");
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = (String) token.getPrincipal();
        //retry count + 1
        AtomicInteger retryCount = passwordRetryCache.get(username);
        if (retryCount == null) {
            retryCount = new AtomicInteger(0);
            passwordRetryCache.put(username, retryCount);
        }
        if (retryCount.incrementAndGet() > passwordRetry) {
            throw new ExcessiveAttemptsException();
        }

        boolean matches = credentialsMatch(token, info);
        if (matches) {
            //clear retry count
            passwordRetryCache.remove(username);
        }
        //设置换成
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        session.setAttribute("user", info.getPrincipals().getPrimaryPrincipal());
        return matches;
    }

    /**
     * 自定义密码校验
     *
     * @param authToken
     * @param info
     * @return
     */
    private boolean credentialsMatch(AuthenticationToken authToken, AuthenticationInfo info) {
        AppUser appUser = (AppUser) info.getPrincipals().getPrimaryPrincipal();
        UsernamePasswordToken token = (UsernamePasswordToken) authToken;
        Md5Hash password = new Md5Hash(token.getPassword());
        return Arrays.equals(password.getBytes(), Hex.decode(appUser.getPassword()));
    }
}
