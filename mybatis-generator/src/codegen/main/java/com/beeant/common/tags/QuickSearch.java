package com.beeant.common.tags;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * Created by beeant on 16/4/28.
 */
public class QuickSearch extends TagSupport {
    private String id;

    private String table;

    private String column;

    private String placeholder;

    private boolean showMore;

    private String moreTarget;

    /** {@inheritDoc} */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Setter for property 'table'.
     *
     * @param table Value to set for property 'table'.
     */
    public void setTable(String table) {
        this.table = table;
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
     * Setter for property 'placeholder'.
     *
     * @param placeholder Value to set for property 'placeholder'.
     */
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    /**
     * Setter for property 'showMore'.
     *
     * @param showMore Value to set for property 'showMore'.
     */
    public void setShowMore(boolean showMore) {
        this.showMore = showMore;
    }

    /**
     * Setter for property 'moreTarget'.
     *
     * @param moreTarget Value to set for property 'moreTarget'.
     */
    public void setMoreTarget(String moreTarget) {
        this.moreTarget = moreTarget;
    }

    @Override
    public int doEndTag() throws JspException {
        JspWriter out = pageContext.getOut();

        StringBuilder sb = new StringBuilder();
        sb.append("<div class='column'><div class='ui search quick' data-table='");
        sb.append(table);
        sb.append("' data-column='");
        sb.append(column);
        if(!StringUtils.isEmpty(id)){
            sb.append("' id='");
            sb.append(id);
            sb.append("' ");
        }

        sb.append("' >");
        if(showMore){
            sb.append("<div class='ui action right primary toggle button' data-target='");
            sb.append(moreTarget);
            sb.append("'>更多条件<i class='ui caret down icon'></i></div>");
        }
        sb.append("<div class='ui action right icon input'><i class='search icon'></i><input type='text' placeholder='");
        sb.append(placeholder);
        sb.append("'><div class='ui teal button'></div></div></div></div>");

        try {
            out.println(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.doEndTag();
    }
}
