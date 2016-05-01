package com.beeant.common.utils;

import org.dozer.DozerBeanMapper;

/**
 * Created by Beeant on 2016/4/30.
 */
public final class BeanMapper {
    private static DozerBeanMapper beanMapper = new DozerBeanMapper();

    public static <T> T map(Object source, Class<T> destinationClass){
        return beanMapper.map(source, destinationClass);
    }
}
