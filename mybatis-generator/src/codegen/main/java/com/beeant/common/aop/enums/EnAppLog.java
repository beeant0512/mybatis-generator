package com.beeant.common.aop.enums;

/**
 * Created by Beeant on 2016/4/24.
 */
public enum EnAppLog {
    LOGIN("login","登录"),
    STATUS_SUCCESS("success","成功"),
    STATUS_FAILED("failed","失败");

    private String code;

    private String description;

    EnAppLog(String code, String description) {
        this.description = description;
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
