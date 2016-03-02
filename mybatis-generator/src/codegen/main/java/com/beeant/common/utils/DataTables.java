package com.beeant.common.utils;

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
    /**
     * orders
     * e.g 'id asc, name desc'
     */
    private String orders;
    /**
     * search value
     */
    private String search;
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


    /**
     * Getter for property 'draw'.
     *
     * @return Value for property 'draw'.
     */
    public long getDraw() {
        return draw;
    }

    /**
     * Setter for property 'draw'.
     *
     * @param draw Value to set for property 'draw'.
     */
    public void setDraw(long draw) {
        this.draw = draw;
    }

    /**
     * Getter for property 'start'.
     *
     * @return Value for property 'start'.
     */
    public int getStart() {
        return start;
    }

    /**
     * Setter for property 'start'.
     *
     * @param start Value to set for property 'start'.
     */
    public void setStart(int start) {
        this.start = start;
    }

    /**
     * Getter for property 'length'.
     *
     * @return Value for property 'length'.
     */
    public int getLength() {
        return length;
    }

    /**
     * Setter for property 'length'.
     *
     * @param length Value to set for property 'length'.
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * Getter for property 'orders'.
     *
     * @return Value for property 'orders'.
     */
    public String getOrders() {
        return orders;
    }

    /**
     * Setter for property 'orders'.
     *
     * @param orders Value to set for property 'orders'.
     */
    public void setOrders(String orders) {
        this.orders = orders;
    }

    /**
     * Getter for property 'search'.
     *
     * @return Value for property 'search'.
     */
    public String getSearch() {
        return search;
    }

    /**
     * Setter for property 'search'.
     *
     * @param search Value to set for property 'search'.
     */
    public void setSearch(String search) {
        this.search = search;
    }

    /**
     * Getter for property 'recordsTotal'.
     *
     * @return Value for property 'recordsTotal'.
     */
    public long getRecordsTotal() {
        return recordsTotal;
    }

    /**
     * Setter for property 'recordsTotal'.
     *
     * @param recordsTotal Value to set for property 'recordsTotal'.
     */
    public void setRecordsTotal(long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    /**
     * Getter for property 'recordsFiltered'.
     *
     * @return Value for property 'recordsFiltered'.
     */
    public long getRecordsFiltered() {
        return recordsFiltered;
    }

    /**
     * Setter for property 'recordsFiltered'.
     *
     * @param recordsFiltered Value to set for property 'recordsFiltered'.
     */
    public void setRecordsFiltered(long recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    /**
     * Getter for property 'data'.
     *
     * @return Value for property 'data'.
     */
    public List<Dto> getData() {
        return data;
    }

    /**
     * Setter for property 'data'.
     *
     * @param data Value to set for property 'data'.
     */
    public void setData(List<Dto> data) {
        this.data = data;
    }

    /**
     * Getter for property 'error'.
     *
     * @return Value for property 'error'.
     */
    public String getError() {
        return error;
    }

    /**
     * Setter for property 'error'.
     *
     * @param error Value to set for property 'error'.
     */
    public void setError(String error) {
        this.error = error;
    }

    public void setReturn(ModelAndView mav) {
        //mav.addObject("data", this.data);
        mav.addObject("recordsTotal", this.recordsFiltered);
        mav.addObject("recordsFiltered", this.recordsFiltered);
        mav.addObject("draw", this.draw);
    }
}
