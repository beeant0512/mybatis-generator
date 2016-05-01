package com.beeant.common.tags;

import com.beeant.common.tags.enums.Defaults;
import org.springframework.util.StringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by beeant on 16/4/26.
 */
public class Table extends TagSupport {

    private String id;

    private String clasz;

    private String width;

    private List<String> ths;

    /**
     * {@inheritDoc}
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Setter for property 'clasz'.
     *
     * @param clasz Value to set for property 'clasz'.
     */
    public void setClasz(String clasz) {
        this.clasz = clasz;
    }

    /**
     * Setter for property 'width'.
     *
     * @param width Value to set for property 'width'.
     */
    public void setWidth(String width) {
        this.width = width;
    }

    /**
     * Setter for property 'ths'.
     *
     * @param ths Value to set for property 'ths'.
     */
    public void setThs(List<String> ths) {
        this.ths = ths;
    }

    public void addTh(String th) {
        ths.add(th);
    }

    @Override
    public int doStartTag() throws JspException {
        ths = new ArrayList<String>();
        return EVAL_PAGE;
    }

    @Override
    public int doEndTag() throws JspException {
        JspWriter out = this.pageContext.getOut();

        StringBuilder sb = new StringBuilder();
        sb.append("<table id='");
        sb.append(id);
        sb.append("' class='");
        if (StringUtils.isEmpty(clasz)) {
            clasz = Defaults.TABLE_CLASS.getValue();
        }
        sb.append(clasz);
        sb.append("' cellspacing='0'");
        sb.append(" style='width:");
        if (StringUtils.isEmpty(width)) {
            width = Defaults.TABLE_WIDTH.getValue();
        }
        sb.append(width);
        sb.append("'><thead>");
        for (String th : ths) {
            sb.append(th);
        }
        sb.append("</thead></table>");
        try {
            out.println(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return super.doEndTag();
    }
}
