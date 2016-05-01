package com.beeant.common.utils;

import java.util.List;

/**
 * Created by Beeant on 2016/4/14.
 */
public class JqTreeResponse {

    private String id;

    private String pid;

    private String label;

    private String en;

    private List<JqTreeResponse> children;

    /**
     * Getter for property 'id'.
     *
     * @return Value for property 'id'.
     */
    public String getId() {
        return id;
    }

    /**
     * Setter for property 'id'.
     *
     * @param id Value to set for property 'id'.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter for property 'pid'.
     *
     * @return Value for property 'pid'.
     */
    public String getPid() {
        return pid;
    }

    /**
     * Setter for property 'pid'.
     *
     * @param pid Value to set for property 'pid'.
     */
    public void setPid(String pid) {
        this.pid = pid;
    }

    /**
     * Getter for property 'label'.
     *
     * @return Value for property 'label'.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Setter for property 'label'.
     *
     * @param label Value to set for property 'label'.
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Getter for property 'en'.
     *
     * @return Value for property 'en'.
     */
    public String getEn() {
        return en;
    }

    /**
     * Setter for property 'en'.
     *
     * @param en Value to set for property 'en'.
     */
    public void setEn(String en) {
        this.en = en;
    }

    /**
     * Getter for property 'children'.
     *
     * @return Value for property 'children'.
     */
    public List<JqTreeResponse> getChildren() {
        return children;
    }

    /**
     * Setter for property 'children'.
     *
     * @param children Value to set for property 'children'.
     */
    public void setChildren(List<JqTreeResponse> children) {
        this.children = children;
    }
}
