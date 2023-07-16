package com.onezol.vertex.core.module.dictionary.mapper;

import com.onezol.vertex.core.base.mapper.BaseMapper;
import com.onezol.vertex.core.module.dictionary.model.entity.DictValueEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DictValueMapper extends BaseMapper<DictValueEntity> {
    @Select("SELECT " +
            "DE.ENTRY_KEY,DV.DICT_KEY,DV.CODE,DV.VALUE " +
            "FROM SYS_DICT_VALUE DV LEFT JOIN SYS_DICT_ENTRY DE ON DV.ENTRY_ID = DE.ID " +
            "WHERE DV.DELETED = FALSE"
    )
    List<Map<String, Object>> getDictionary();
}
