package com.beeant.common.shiro;

import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Beeant on 2016/3/2.
 */
public class SimpleAuthenticationInfoExt extends SimpleAuthenticationInfo {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public SimpleAuthenticationInfoExt(Object principal, Object hashedCredentials, ByteSource credentialsSalt, String realmName) {
        this.principals = new SimplePrincipalCollection(principal, realmName);
        this.credentials = hashedCredentials;
        this.credentialsSalt = credentialsSalt;
    }


}
