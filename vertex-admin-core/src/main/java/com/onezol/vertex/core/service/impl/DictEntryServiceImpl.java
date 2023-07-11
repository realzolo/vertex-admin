package com.onezol.vertex.core.service.impl;

import com.onezol.vertex.core.common.service.impl.GenericServiceImpl;
import com.onezol.vertex.core.mapper.DictEntryMapper;
import com.onezol.vertex.core.model.entity.DictEntryEntity;
import com.onezol.vertex.core.service.DictEntryService;
import org.springframework.stereotype.Service;

@Service
public class DictEntryServiceImpl extends GenericServiceImpl<DictEntryMapper, DictEntryEntity> implements DictEntryService {
}
