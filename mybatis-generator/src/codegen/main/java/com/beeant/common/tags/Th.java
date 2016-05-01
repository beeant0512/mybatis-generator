package com.beeant.common.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Created by beeant on 16/4/26.
 */
public class Th extends TagSupport {

    private String width;

    private String column;

    private boolean checkbox;

    private String description;

    /**
     * Setter for property 'width'.
     *
     * @param width Value to set for property 'width'.
     */
    public void setWidth(String width) {
        this.width = width;
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
     * Setter for property 'checkbox'.
     *
     * @param checkbox Value to set for property 'checkbox'.
     */
    public void setCheckbox(boolean checkbox) {
        this.checkbox = checkbox;
    }

    /**
     * Setter for property 'description'.
     *
     * @param description Value to set for property 'description'.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int doEndTag() throws JspException {
        StringBuilder sb = new StringBuilder();
        sb.append("<th ");
        sb.append(">");
        if (checkbox) {
            sb.append("<div class='ui master checkbox'><input type='checkbox' value='' tabindex='0' class='hidden'></div>");
        } else {
            sb.append(description);
        }

        sb.append("</th>");

        Table parent = (Table) this.getParent();
        parent.addTh(sb.toString());

        return super.doStartTag();
    }
}
