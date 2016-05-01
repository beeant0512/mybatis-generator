package com.beeant.common.tags;

import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Created by beeant on 16/4/29.
 */
public class TableSearchColumn extends BodyTagSupport {
    private String id;

    private String column;

    private String label;

    private String placeholder;

    private String type;

    private String clasz;

    /** {@inheritDoc} */
    @Override
    public String getId() {
        return id;
    }

    /** {@inheritDoc} */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter for property 'column'.
     *
     * @return Value for property 'column'.
     */
    public String getColumn() {
        return column;
    }

    /**
     * Setter for property 'column'.
     *
     * @param column Value to set for property 'column'.
     */
    public void setColumn(String column) {
        this.column = column;
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
     * Getter for property 'placeholder'.
     *
     * @return Value for property 'placeholder'.
     */
    public String getPlaceholder() {
        return placeholder;
    }

    /**
     * Setter for property 'placeholder'.
     *
     * @param placeholder Value to set for property 'placeholder'.
     */
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    /**
     * Getter for property 'type'.
     *
     * @return Value for property 'type'.
     */
    public String getType() {
        return type;
    }

    /**
     * Setter for property 'type'.
     *
     * @param type Value to set for property 'type'.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter for property 'clasz'.
     *
     * @return Value for property 'clasz'.
     */
    public String getClasz() {
        return clasz;
    }

    /**
     * Setter for property 'clasz'.
     *
     * @param clasz Value to set for property 'clasz'.
     */
    public void setClasz(String clasz) {
        this.clasz = clasz;
    }
}
