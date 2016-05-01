package com.beeant.common.utils;

/**
 * Created by Beeant on 2016/3/4.
 */
public enum EnumStatus {
    ACTIVE("active"),
    INACTIVE("inactive");

    private String value;

    public String value() {
        return value;
    }

    EnumStatus(String key) {
        this.value = key;
    }
}
