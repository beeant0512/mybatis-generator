package com.beeant.common.utils;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import org.springframework.web.servlet.ModelAndView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Beeant on 2016/1/15.
 */
public class DataTables<Dto> implements Serializable {

    private static final long serialVersionUID = -4075970355312644161L;
    /**
     * Draw counter. This is used by DataTables to ensure that the Ajax returns from server-side processing requests are
     * drawn in sequence by DataTables (Ajax requests are asynchronous and thus can return out of sequence). This is used
     * as part of the draw return parameter (see below).
     * <p/>
     * The draw counter that this object is a response to - from the draw parameter sent as part of the data request.
     * Note that it is strongly recommended for security reasons that you cast this parameter to an integer, rather than
     * simply echoing back to the client what it sent in the draw parameter, in order to prevent Cross Site Scripting
     * (XSS) attacks.
     */
    private long draw;
    /**
     * Paging first record indicator. This is the start point in the current data set (0 index based - i.e. 0 is the first record).
     */
    private int start;
    /**
     * Number of records that the table can display in the current draw. It is expected that the number of records returned
     * will be equal to this number, unless the server has fewer records to return. Note that this can be -1 to indicate
     * that all records should be returned (although that negates any benefits of server-side processing!)
     */
    private int length;

    private List<String> orderColumn = new ArrayList<String>();

    private List<String> orderDir = new ArrayList<String>();

    private PageBounds pageBounds = new PageBounds();

    /**
     * Total records, before filtering (i.e. the total number of records in the database)
     */
    private long recordsTotal;
    /**
     * Total records, after filtering (i.e. the total number of records after filtering has been applied - not just the
     * number of records being returned for this page of data).
     */
    private long recordsFiltered;
    /**
     * The data to be displayed in the table. This is an array of data source objects, one for each row, which will be
     * used by DataTables. Note that this parameter's name can be changed using the ajax option's dataSrc property.
     */
    private List<Dto> data = new ArrayList<Dto>();
    /**
     * Optional: If an error occurs during the running of the server-side processing script, you can inform the user of
     * this error by passing back the error message to be displayed using this parameter. Do not include if there is no error.
     */
    private String error;

    private String DT_RowId;

    private String DT_RowClass;

    private String DT_RowData;

    public long getDraw() {
        return draw;
    }

    public void setDraw(long draw) {
        this.draw = draw;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public List<String> getOrderColumn() {
        return orderColumn;
    }

    public void setOrderColumn(List<String> orderColumn) {
        this.orderColumn = orderColumn;
    }

    public List<String> getOrderDir() {
        return orderDir;
    }

    public void setOrderDir(List<String> orderDir) {
        this.orderDir = orderDir;
    }

    public PageBounds getPageBounds() {
        return pageBounds;
    }

    public void setPageBounds(PageBounds pageBounds) {
        this.pageBounds = pageBounds;
    }

    public long getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public long getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(long recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public List<Dto> getData() {
        return data;
    }

    public void setData(List<Dto> data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getDT_RowId() {
        return DT_RowId;
    }

    public void setDT_RowId(String DT_RowId) {
        this.DT_RowId = DT_RowId;
    }

    public String getDT_RowClass() {
        return DT_RowClass;
    }

    public void setDT_RowClass(String DT_RowClass) {
        this.DT_RowClass = DT_RowClass;
    }

    public String getDT_RowData() {
        return DT_RowData;
    }

    public void setDT_RowData(String DT_RowData) {
        this.DT_RowData = DT_RowData;
    }

    public void setReturn(ModelAndView mav) {
        //mav.addObject("data", this.data);
        mav.addObject("recordsTotal", this.recordsFiltered);
        mav.addObject("recordsFiltered", this.recordsFiltered);
        mav.addObject("draw", this.draw);
    }
}
