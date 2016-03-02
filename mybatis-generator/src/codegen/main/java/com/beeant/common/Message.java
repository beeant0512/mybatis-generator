package com.beeant.common;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Beeant on 2016/1/11.
 */
public class Message<T> implements Serializable {

    private static final long serialVersionUID = -1034850830819604687L;

    /**
     * 消息列表
     */
    private List<String> msgs;

    /**
     * 消息
     */
    private String msg;

    /**
     * 错误码
     */
    private int code = 0;

    /**
     * 状态
     */
    private boolean success = false;

    /**
     * 数据
     */
    private T data;

    /**
     * Getter for property 'msgs'.
     *
     * @return Value for property 'msgs'.
     */
    public List<String> getMsgs() {
        return msgs;
    }

    /**
     * Setter for property 'msgs'.
     *
     * @param msgs Value to set for property 'msgs'.
     */
    public void setMsgs(List<String> msgs) {
        this.msgs = msgs;
    }

    /**
     * Getter for property 'msg'.
     *
     * @return Value for property 'msg'.
     */
    public String getMsg() {
        return msg;
    }

    /**
     * Setter for property 'msg'.
     *
     * @param msg Value to set for property 'msg'.
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * Getter for property 'code'.
     *
     * @return Value for property 'code'.
     */
    public int getCode() {
        return code;
    }

    /**
     * Setter for property 'code'.
     *
     * @param code Value to set for property 'code'.
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * Getter for property 'success'.
     *
     * @return Value for property 'success'.
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Setter for property 'success'.
     *
     * @param success Value to set for property 'success'.
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Getter for property 'data'.
     *
     * @return Value for property 'data'.
     */
    public T getData() {
        return data;
    }

    /**
     * Setter for property 'data'.
     *
     * @param data Value to set for property 'data'.
     */
    public void setData(T data) {
        this.data = data;
    }
}
