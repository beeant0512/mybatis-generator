package com.beeant.common.enums;

/**
 * Created by Beeant on 2016/4/30.
 */
public enum EnErrorCode {

    DATE_NON_EXIST("1","数据不存在"),
    CREATE_ERROR("2","数据创建失败"),
    DELETE_ERROR("3","数据删除失败"),
    UPDATE_ERROR("4","数据更新失败");

    private String code;

    private String description;

    /**
     * Getter for property 'code'.
     *
     * @return Value for property 'code'.
     */
    public String getCode() {
        return code;
    }

    /**
     * Setter for property 'code'.
     *
     * @param code Value to set for property 'code'.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Getter for property 'description'.
     *
     * @return Value for property 'description'.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for property 'description'.
     *
     * @param description Value to set for property 'description'.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    EnErrorCode(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
