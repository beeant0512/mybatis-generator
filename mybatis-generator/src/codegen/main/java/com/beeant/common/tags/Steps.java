package com.beeant.common.tags;

import org.springframework.util.StringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by beeant on 16/4/28.
 */
public class Steps extends TagSupport {

    private String id;

    private String number;

//    private List<String> tabs;

    private List<Tab> tabs;

//    public void setTab(String tab){
//        tabs.add(tab);
//    }

    public void addTab(Tab tab) {
        tabs.add(tab);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Setter for property 'number'.
     *
     * @param number Value to set for property 'number'.
     */
    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public int doStartTag() throws JspException {
        tabs = new ArrayList<Tab>();
        return EVAL_PAGE;
    }

    @Override
    public int doEndTag() throws JspException {
        JspWriter out = pageContext.getOut();

        StringBuilder sb = new StringBuilder();

        sb.append("<div id='");
        sb.append(id);
        sb.append("' class='ui ");
        sb.append(number);
        sb.append(" top attached tablet steps'>\n");
        for (Tab tab : tabs) {
            sb.append("<div class='step ");
            if (!StringUtils.isEmpty(tab.getActive())) {
                sb.append(tab.getActive());
            }

            sb.append("' data-tab='");
            sb.append(tab.getTab());
            sb.append("'>\n");
            if (!StringUtils.isEmpty(tab.getIcon())) {
                sb.append("<i class='");
                sb.append(tab.getIcon());
                sb.append("'></i>\n");
            }
            sb.append("<div class='content'>\n");
            sb.append("<div class='title'>\n");
            sb.append(tab.getTitle());
            sb.append("</div>\n");

            if (!StringUtils.isEmpty(tab.getDescription())) {
                sb.append("<div class='description'>\n");
                sb.append(tab.getDescription());
                sb.append("</div>\n");
            }
            sb.append("</div>\n");
            sb.append("</div>\n");
        }

        sb.append("</div>\n");

        for (Tab tab : tabs) {
            sb.append("<div class='ui center aligned attached tab segment ");
            if (!StringUtils.isEmpty(tab.getActive())) {
                sb.append(tab.getActive());
            }
            sb.append(" ' data-tab='");
            sb.append(tab.getTab());
            sb.append("'>\n");
            if (null != tab.getBodyContent()) {
                sb.append(tab.getBodyContent().getString());
            }
            sb.append("</div>\n");
        }

        try {
            out.println(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.doEndTag();
    }

    @Override
    public int doAfterBody() throws JspException {

        return super.doAfterBody();
    }
}
