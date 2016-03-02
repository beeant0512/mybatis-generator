package com.beeant.common.base;

import com.beeant.common.Message;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by Beeant on 2016/2/17.
 */
public abstract class BaseServiceImpl<Obj, Example> implements IBaseService<Obj, Example> {


    /**
     * get count by condition
     *
     * @param example condition
     * @return number
     */
    public Message<Integer> countByCondition(Example example) {
        return null;
    }

    /**
     * delete by condition
     *
     * @param example condition
     * @return number
     */
    public Message<Integer> deleteByCondition(Example example) {
        return null;
    }

    /**
     * delete by primary key
     *
     * @param id key
     * @return number
     */
    public Message<Integer> deleteByPrimaryKey(String id) {
        return null;
    }

    /**
     * insert all
     *
     * @param record record
     * @return number
     */
    public Message<Integer> insert(Obj record) {
        Message<Integer> msg = new Message<Integer>();
        setKey(record);
        int num = getDao().insert(record);
        if (num == 0){
            msg.setMsg("数据创建错误，请重试。");
            return msg;
        }
        msg.setSuccess(true);
        msg.setMsg("数据创建成功");
        msg.setData(num);
        return msg;
    }

    /**
     * insert selective
     *
     * @param record record
     * @return number
     */
    public Message<Integer> insertSelective(Obj record) {
        return null;
    }

    /**
     * select by condition by row bounds
     *
     * @param example   condition
     * @param rowBounds pager info
     * @return list
     */
    public Message<List<Obj>> selectByConditionWithRowbounds(Example example, RowBounds rowBounds) {
        Message<List<Obj>> msg = new Message<List<Obj>>();
        List<Obj> result = getDao().selectByConditionWithRowbounds(example, rowBounds);
        if (result.size() == 0){
            msg.setMsg("您要查找的数据不能存在");
            return msg;
        }

        msg.setSuccess(true);
        msg.setData(result);
        return msg;
    }

    /**
     * select by condition
     *
     * @param example condition
     * @return list
     */
    public Message<List<Obj>> selectByCondition(Example example) {
        Message<List<Obj>> msg = new Message<List<Obj>>();

        List<Obj> result = getDao().selectByCondition(example);
        if (result.size() == 0){
            return msg;
        }
        msg.setSuccess(true);
        msg.setData(result);
        return msg;
    }

    /**
     * select by primary key
     *
     * @param id key
     * @return data
     */
    public Message<Obj> selectByPrimaryKey(String id) {
        return null;
    }

    /**
     * update by condition by selective
     *
     * @param record  updated value
     * @param example condition
     * @return number
     */
    public Message<Integer> updateByConditionSelective(Obj record, Example example) {
        return null;
    }

    /**
     * update all by condition
     * if not set the object value, it will set to null or default value defined in database
     *
     * @param record  updated value
     * @param example condition
     * @return number
     */
    public Message<Integer> updateByCondition(Obj record, Example example) {
        return null;
    }

    /**
     * update by primary key by selective
     *
     * @param record updated value
     * @return number
     */
    public Message<Integer> updateByPrimaryKeySelective(Obj record) {
        return null;
    }

    /**
     * update by primary
     * if not set the object value, it will set to null or default value defined in database
     *
     * @param record updated value
     * @return number
     */
    public Message<Integer> updateByPrimaryKey(Obj record) {
        return null;
    }
}
