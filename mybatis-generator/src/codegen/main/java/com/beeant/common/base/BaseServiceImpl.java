package com.beeant.common.base;

import com.beeant.common.Message;
import com.beeant.common.enums.EnErrorCode;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Beeant on 2016/2/17.
 */
public abstract class BaseServiceImpl<Obj> implements IBaseService<Obj> {
    @Override
    public Message<Obj> getByPrimaryKey(String key) {
        Message<Obj> msg = new Message<Obj>();
        Obj record = getDao().getByPrimaryKey(key);
        if (null == record) {
            msg.setCode(EnErrorCode.DATE_NON_EXIST);
            return msg;
        }

        msg.setSuccess(true);
        msg.setData(record);
        return msg;
    }

    @Override
    public Message<PageList<Obj>> getAllByParamsByPager(Obj record, PageBounds pageBounds) {
        Message<PageList<Obj>> msg = new Message<PageList<Obj>>();
        PageList<Obj> records = getDao().getAllByParamsByPager(record, pageBounds);
        if (0 == records.size()) {
            msg.setCode(EnErrorCode.DATE_NON_EXIST);
            return msg;
        }

        msg.setSuccess(true);
        msg.setData(records);

        return msg;
    }

    @Override
    public Message<List<Obj>> batchCreate(List<Obj> records) {
        Message<List<Obj>> msg = new Message<List<Obj>>();

        for (Obj record : records) {
            setDefaults(record);
        }

        int num = getDao().batchCreate(records);
        if (0 == num) {
            msg.setCode(EnErrorCode.CREATE_ERROR);
            return msg;
        }

        msg.setSuccess(true);
        msg.setData(records);

        return msg;
    }

    @Override
    public Message<Obj> create(Obj record) {
        Message<Obj> msg = new Message<Obj>();
        setDefaults(record);
        int num = getDao().create(record);
        if (0 == num) {
            msg.setCode(EnErrorCode.CREATE_ERROR);
            return msg;
        }

        msg.setSuccess(true);
        msg.setData(record);
        return msg;
    }

    @Override
    public Message<Integer> batchDelete(List<String> keys) {
        Message<Integer> msg = new Message<Integer>();
        int num = getDao().batchDelete(keys);
        if (0 == num) {
            msg.setCode(EnErrorCode.DELETE_ERROR);
            return msg;
        }

        msg.setSuccess(true);
        msg.setData(num);
        return msg;
    }

    @Override
    public Message<Integer> delete(String key) {
        Message<Integer> msg = new Message<Integer>();
        int num = getDao().delete(key);
        if (0 == num) {
            msg.setCode(EnErrorCode.DELETE_ERROR);
            return msg;
        }

        msg.setSuccess(true);
        msg.setData(num);
        return msg;
    }

    @Override
    public Message<Integer> updateByPrimaryKeySelective(Obj record) {
        Message<Integer> msg = new Message<Integer>();
        int num = getDao().updateByPrimaryKeySelective(record);
        if (0 == num) {
            msg.setCode(EnErrorCode.UPDATE_ERROR);
            return msg;
        }

        msg.setSuccess(true);
        msg.setData(num);
        return msg;
    }

    @Override
    public Message<Integer> batchUpdateSelective(List<Obj> records) {
        Message<Integer> msg = new Message<Integer>();
        int num = getDao().batchUpdateSelective(records);
        if (0 == num) {
            msg.setCode(EnErrorCode.UPDATE_ERROR);
            return msg;
        }

        msg.setSuccess(true);
        msg.setData(num);
        return msg;
    }

    @Override
    public Message<Integer> update(Obj record) {
        Message<Integer> msg = new Message<Integer>();
        int num = getDao().update(record);
        if (0 == num) {
            msg.setCode(EnErrorCode.UPDATE_ERROR);
            return msg;
        }

        msg.setSuccess(true);
        msg.setData(num);
        return msg;
    }

    @Override
    public Message<Integer> batchUpdate(List<Obj> records) {
        Message<Integer> msg = new Message<Integer>();
        int num = getDao().batchUpdate(records);
        if (0 == num) {
            msg.setCode(EnErrorCode.UPDATE_ERROR);
            return msg;
        }

        msg.setSuccess(true);
        msg.setData(num);
        return msg;
    }

    @Override
    public Message<List<Obj>> getAllByParams(Obj record) {
        Message<List<Obj>> msg = new Message<List<Obj>>();
        List<Obj> lists = new ArrayList<Obj>();
        PageBounds defaultLimit = new PageBounds(3000);
        PageList<Obj> records = getDao().getAllByParamsByPager(record, defaultLimit);

        if (0 == records.size()) {
            msg.setCode(EnErrorCode.DATE_NON_EXIST);
            return msg;
        }
        while(0 != records.size()){
            lists.addAll(records);
            records = getDao().getAllByParamsByPager(record, defaultLimit);
        }
        msg.setSuccess(true);
        msg.setData(lists);

        return msg;
    }

    @Override
    public Message<Obj> getByParams(Obj record) {
        Message<Obj> msg = new Message<Obj>();
        PageList<Obj> records = getDao().getAllByParamsByPager(record, new PageBounds());

        if (0 == records.size()){
            msg.setCode(EnErrorCode.DATE_NON_EXIST);
            return msg;
        }

        msg.setData(records.get(0));
        msg.setSuccess(true);
        return msg;
    }
}
