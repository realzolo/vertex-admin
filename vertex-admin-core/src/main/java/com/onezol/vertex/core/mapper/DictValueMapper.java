package com.onezol.vertex.core.mapper;

import com.onezol.vertex.core.common.mapper.BaseMapper;
import com.onezol.vertex.core.model.entity.DictValueEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DictValueMapper extends BaseMapper<DictValueEntity> {
    @Select("SELECT " +
            "DE.ENTRY_KEY,DV.DICT_KEY,DV.CODE,DV.VALUE " +
            "FROM PF_DICT_VALUE DV LEFT JOIN PF_DICT_ENTRY DE ON DV.ENTRY_ID = DE.ID " +
            "WHERE DV.DELETED = FALSE"
    )
    List<Map<String, Object>> getDictionary();
}
