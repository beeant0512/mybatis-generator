package com.beeant.common.base;

import com.beeant.common.Message;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by Beeant on 2016/2/17.
 */
public interface IBaseService<Obj, Example> {

    IBaseDao<Obj, Example> getDao();

    /**
     * set primary key
     */
    void setKey(Obj record);

    /**
     * get count by condition
     *
     * @param example condition
     * @return number
     */
    Message<Integer> countByCondition(Example example);

    /**
     * delete by condition
     *
     * @param example condition
     * @return number
     */
    Message<Integer> deleteByCondition(Example example);

    /**
     * delete by primary key
     *
     * @param id key
     * @return number
     */
    Message<Integer> deleteByPrimaryKey(String id);

    /**
     * insert all
     *
     * @param record record
     * @return number
     */
    Message<Integer> insert(Obj record);

    /**
     * insert selective
     *
     * @param record record
     * @return number
     */
    Message<Integer> insertSelective(Obj record);

    /**
     * select by condition by row bounds
     *
     * @param example   condition
     * @param rowBounds pager info
     * @return list
     */
    Message<List<Obj>> selectByConditionWithRowbounds(Example example, RowBounds rowBounds);

    /**
     * select by condition
     *
     * @param example condition
     * @return list
     */
    Message<List<Obj>> selectByCondition(Example example);

    /**
     * select by primary key
     *
     * @param id key
     * @return data
     */
    Message<Obj> selectByPrimaryKey(String id);

    /**
     * update by condition by selective
     *
     * @param record  updated value
     * @param example condition
     * @return number
     */
    Message<Integer> updateByConditionSelective(Obj record, Example example);

    /**
     * update all by condition
     * if not set the object value, it will set to null or default value defined in database
     *
     * @param record  updated value
     * @param example condition
     * @return number
     */
    Message<Integer> updateByCondition(Obj record, Example example);

    /**
     * update by primary key by selective
     *
     * @param record updated value
     * @return number
     */
    Message<Integer> updateByPrimaryKeySelective(Obj record);

    /**
     * update by primary
     * if not set the object value, it will set to null or default value defined in database
     *
     * @param record updated value
     * @return number
     */
    Message<Integer> updateByPrimaryKey(Obj record);
}
