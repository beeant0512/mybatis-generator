package com.beeant.common.base;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Beeant on 2016/2/25.
 */
public interface IBaseDao<Dto> {

    Dto getByPrimaryKey(String key);

    PageList<Dto> getAllByParamsByPager(@Param("item") Dto record, @Param("pageBounds") PageBounds pageBounds);

    int batchCreate(List<Dto> records);

    int create(Dto record);

    int batchDelete(List<String> keys);

    int delete(String key);

    int updateByPrimaryKeySelective(Dto record);

    int batchUpdateSelective(List<Dto> records);

    int update(Dto record);

    int batchUpdate(List<Dto> records);

}
