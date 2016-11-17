package com.polycom.honeycomb.rm.utils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import java.util.List;

/**
 * Created by weigao on 22/9/2016.
 */
public class BeanMapper {

    private static MapperFacade mapper = null;

    static {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder()
                .build();
        mapper = mapperFactory.getMapperFacade();

    }

    /**
     * 基于Dozer转换对象的类型.
     */
    public static <S, D> D map(S source, Class<D> destinationClass) {
        return mapper.map(source, destinationClass);
    }

    /**
     * 基于Dozer转换Collection中对象的类型.
     */
    public static <S, D> List<D> mapList(Iterable<S> sourceList,
            Class<D> destinationClass) {
        return mapper.mapAsList(sourceList, destinationClass);
    }

}
