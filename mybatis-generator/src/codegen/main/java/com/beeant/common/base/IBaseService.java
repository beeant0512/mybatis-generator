package com.beeant.common.base;

import com.beeant.common.Message;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

import java.util.List;

/**
 * Created by Beeant on 2016/2/17.
 */
public interface IBaseService<Dto> {

    IBaseDao<Dto> getDao();

    void setDefaults(Dto record);

    Message<Dto> getByPrimaryKey(String key);

    Message<PageList<Dto>> getAllByParamsByPager(Dto record, PageBounds pageBounds);

    Message<List<Dto>> batchCreate(List<Dto> records);

    Message<Dto> create(Dto record);

    Message<Integer> batchDelete(List<String> keys);

    Message<Integer> delete(String key);

    Message<Integer> updateByPrimaryKeySelective(Dto record);

    Message<Integer> batchUpdateSelective(List<Dto> records);

    Message<Integer> update(Dto record);

    Message<Integer> batchUpdate(List<Dto> records);

    Message<List<Dto>> getAllByParams(Dto record);

    Message<Dto> getByParams(Dto record);
}
