package com.beeant.common.utils;

import org.apache.ibatis.session.RowBounds;

/**
 * Created by Beeant on 2016/2/27.
 */
public class Pager {

    private int limit = 3000;

    private int offset = 0;

    private int total;

    /**
     * Getter for property 'limit'.
     *
     * @return Value for property 'limit'.
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Setter for property 'limit'.
     *
     * @param limit Value to set for property 'limit'.
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * Getter for property 'offset'.
     *
     * @return Value for property 'offset'.
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Setter for property 'offset'.
     *
     * @param offset Value to set for property 'offset'.
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }

    public RowBounds getRowBounds(){
        return new RowBounds(this.offset, this.limit);
    }
}
