package com.beeant.common.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by beeant on 16/4/29.
 */
public class TableSearch extends BodyTagSupport {
    
    private String id;

    private String table;
    
    private List<TableSearchColumn> columns;
    
    public void addColumn(TableSearchColumn column){
        columns.add(column);
    }

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

    @Override
    public int doStartTag() throws JspException {
        columns = new ArrayList<TableSearchColumn>();
        return super.doStartTag();
    }

    @Override
    public int doEndTag() throws JspException {
        JspWriter out = pageContext.getOut();
        
        StringBuilder sb = new StringBuilder();
        
        sb.append("<div class='ui celled grid' id='");
        sb.append("' style='display: none'><form class='ui form fluid container padding'>");
        int columnSize = columns.size();
        for(int i=0; i<columnSize; i++){
            if(i == 0){
                sb.append("<div class='four inline fields'>");
            }
            sb.append("<div class='four wide field'><label>");
            sb.append(columns.get(i).getLabel());
            sb.append("</label><input class='");
            sb.append(columns.get(i).getClasz());
            sb.append("' name='");
            sb.append(columns.get(i).getColumn());
            sb.append("' type='text' placeholder='");
            sb.append(columns.get(i).getPlaceholder());
            sb.append("'></div>");
            if(i % 4 == 0){
                sb.append("</div>");
            }
        }
        
        sb.append("<div class='field '><button class='ui primary button right aligned reload' data-table='");
        sb.append(table);
        sb.append("'>搜索</button></div>");
        sb.append("</form></div>");
        return super.doEndTag();
    }
}
