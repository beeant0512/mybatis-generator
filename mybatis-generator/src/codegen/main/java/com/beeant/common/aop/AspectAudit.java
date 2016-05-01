package com.beeant.common.aop;

import com.beeant.common.aop.annotation.LoginLog;
import com.beeant.common.aop.enums.EnAppLog;
import com.beeant.dto.AppLogAudit;
import com.beeant.dto.AppLogAuditDetail;
import com.beeant.dto.AppUser;
import com.beeant.service.IAppLogAuditDetailService;
import com.beeant.service.IAppLogAuditService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;


/**
 * 日志审计
 * <p>
 * Created by Beeant on 2016/4/24.
 */

@Aspect
@Component
public class AspectAudit {
    @Autowired
    private IAppLogAuditService logAuditService;

    @Autowired
    private IAppLogAuditDetailService logAuditDetailsService;

    //本地异常日志记录对象
    private static final Logger logger = LoggerFactory.getLogger(AspectAudit.class);

    //Controller层切点
    @Pointcut("@annotation(com.beeant.common.aop.annotation.AuditLog)")
    public void auditLogPointcut() {
    }

    @Pointcut("@annotation(com.beeant.common.aop.annotation.LoginLog)")
    public void loginPointcut() {
    }

    private String getFunctionName(JoinPoint joinPoint) {
        StringBuilder sb = new StringBuilder();
        sb.append(joinPoint.getTarget().getClass().getName());
        sb.append(".");
        sb.append(joinPoint.getSignature().getName());
        return sb.toString();
    }

    @After("auditLogPointcut()")
    public void doBefore(JoinPoint joinPoint) {

    }

    /**
     * 登录日志
     *
     * @param joinPoint
     */
    @After("loginPointcut()")
    public void doLoginPointcut(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String username = request.getParameter(getAnnotationUsernameParam(joinPoint));
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        if (null != session) {
            AppUser user = (AppUser) subject.getSession().getAttribute("user");
            if (!StringUtils.isEmpty(username)) {
                AppLogAudit appLogAudit = new AppLogAudit();
                appLogAudit.setLogId(UUID.randomUUID().toString());
                appLogAudit.setActionTime(new Date());
                appLogAudit.setActorIp(request.getRemoteAddr());
                appLogAudit.setActorName(username);
                appLogAudit.setFunctionName(getFunctionName(joinPoint));
                appLogAudit.setFunctionDescription(getLoginLogAnnotationDescription(joinPoint));
                appLogAudit.setActionStatus(EnAppLog.STATUS_FAILED.getCode());
                if (null != user) {
                    appLogAudit.setActorId(user.getUserId());
                    appLogAudit.setActionStatus(EnAppLog.STATUS_SUCCESS.getCode());
                }


                AppLogAuditDetail logAuditDetail = new AppLogAuditDetail();
                logAuditDetail.setLogId(appLogAudit.getLogId());
                logAuditDetail.setFunctionParams(JSONObject.valueToString(request.getParameterMap()));
                StringBuilder sb = new StringBuilder();
                for (Object object : joinPoint.getArgs()) {
                    if (null != object && StringUtils.pathEquals(object.getClass().getSimpleName(), "BindingAwareModelMap")) {
                        BindingAwareModelMap map = (BindingAwareModelMap) object;
                        Iterator iter = map.entrySet().iterator();
                        String key = "";
                        while (iter.hasNext()) {
                            Map.Entry entry = (Map.Entry) iter.next();
                            key = (String) entry.getKey();
                            if (StringUtils.pathEquals(key, "password") || StringUtils.pathEquals(key, "username")) {
                                appLogAudit.setActionStatus(EnAppLog.STATUS_FAILED.getCode());
                                sb.append(JSONObject.valueToString(entry.getValue()));
                            }
                        }
                    }
                }

                logAuditService.create(appLogAudit);

                logAuditDetail.setFunctionReturn(sb.toString());
                logAuditDetailsService.create(logAuditDetail);
            }
        }
    }

    /**
     * 获取被切的方法名称
     *
     * @param joinPoint
     * @return
     * @throws Exception
     */
    public static String getLoginLogAnnotationDescription(JoinPoint joinPoint) {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        EnAppLog enAppLog = null;
        try {
            Class targetClass = Class.forName(targetName);
            Method[] methods = targetClass.getMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    Class[] clazzs = method.getParameterTypes();
                    if (clazzs.length == arguments.length) {
                        enAppLog = method.getAnnotation(LoginLog.class).code();
                        break;
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            logger.error("日志切面获取注解描述错误", e);
        }

        return enAppLog.getDescription();
    }

    /**
     * 获取登录用户名参数名称
     *
     * @param joinPoint
     * @return
     * @throws Exception
     */
    public static String getAnnotationUsernameParam(JoinPoint joinPoint) {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        String usernameParam = null;
        try {
            Class targetClass = Class.forName(targetName);
            Method[] methods = targetClass.getMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    Class[] clazzs = method.getParameterTypes();
                    if (clazzs.length == arguments.length) {
                        usernameParam = method.getAnnotation(LoginLog.class).username();
                        break;
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            logger.error("日志切面:获取登录用户名参数错误", e);
        }

        return usernameParam;
    }


    public void getHardwareAddress() {

    }
}
