package com.beeant.common.tags.enums;

/**
 * Created by beeant on 16/4/28.
 */
public enum  Defaults {
    TABLE_CLASS("ui celled table"),
    TABLE_WIDTH("100%");

    private String value;

    /**
     * Getter for property 'value'.
     *
     * @return Value for property 'value'.
     */
    public String getValue() {
        return value;
    }

    /**
     * Setter for property 'value'.
     *
     * @param value Value to set for property 'value'.
     */
    public void setValue(String value) {
        this.value = value;
    }

    Defaults(String value) {
        this.value = value;
    }
}
