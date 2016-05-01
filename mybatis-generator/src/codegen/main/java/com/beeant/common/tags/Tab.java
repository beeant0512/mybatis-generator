package com.beeant.common.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Created by beeant on 16/4/28.
 */
public class Tab extends BodyTagSupport {

    private String tab;

    private String title;

    private String description;

    private String active;

    private String icon;


    /**
     * Setter for property 'tab'.
     *
     * @param tab Value to set for property 'tab'.
     */
    public void setTab(String tab) {
        this.tab = tab;
    }

    /**
     * Setter for property 'title'.
     *
     * @param title Value to set for property 'title'.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Setter for property 'description'.
     *
     * @param description Value to set for property 'description'.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Setter for property 'active'.
     *
     * @param active Value to set for property 'active'.
     */
    public void setActive(String active) {
        this.active = active;
    }

    /**
     * Setter for property 'icon'.
     *
     * @param icon Value to set for property 'icon'.
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * Getter for property 'tab'.
     *
     * @return Value for property 'tab'.
     */
    public String getTab() {
        return tab;
    }

    /**
     * Getter for property 'title'.
     *
     * @return Value for property 'title'.
     */
    public String getTitle() {
        return title;
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
     * Getter for property 'active'.
     *
     * @return Value for property 'active'.
     */
    public String getActive() {
        return active;
    }

    /**
     * Getter for property 'icon'.
     *
     * @return Value for property 'icon'.
     */
    public String getIcon() {
        return icon;
    }

    @Override
    public int doEndTag() throws JspException {
        Steps step = (Steps) getParent();
        Tab stepTab = new Tab();
        stepTab.setActive(this.getActive());
        stepTab.setTab(this.getTab());
        stepTab.setDescription(this.getDescription());
        stepTab.setIcon(this.getIcon());
        stepTab.setTitle(this.getTitle());
        stepTab.setBodyContent(this.getBodyContent());
        step.addTab(stepTab);

        return super.doEndTag();
    }
}
