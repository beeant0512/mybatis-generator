package com.beeant.common.base;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by Beeant on 2016/2/25.
 */
public interface IBaseDao<Dto, Example> {

    int countByCondition(Example example);

    int deleteByCondition(Example example);

    int deleteByPrimaryKey(String roleId);

    int insert(Dto record);

    int insertSelective(Dto record);

    List<Dto> selectByConditionWithRowbounds(Example example, RowBounds rowBounds);

    List<Dto> selectByCondition(Example example);

    Dto selectByPrimaryKey(String roleId);

    int updateByConditionSelective(@Param("record") Dto record, @Param("example") Example example);

    int updateByCondition(@Param("record") Dto record, @Param("example") Example example);

    int updateByPrimaryKeySelective(Dto record);

    int updateByPrimaryKey(Dto record);
}
