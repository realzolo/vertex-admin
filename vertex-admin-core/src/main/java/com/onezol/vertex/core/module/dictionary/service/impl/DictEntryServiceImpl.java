package com.onezol.vertex.core.module.dictionary.service.impl;

import com.onezol.vertex.core.common.service.impl.GenericServiceImpl;
import com.onezol.vertex.core.module.dictionary.mapper.DictEntryMapper;
import com.onezol.vertex.core.module.dictionary.model.entity.DictEntryEntity;
import com.onezol.vertex.core.module.dictionary.service.DictEntryService;
import org.springframework.stereotype.Service;

@Service
public class DictEntryServiceImpl extends GenericServiceImpl<DictEntryMapper, DictEntryEntity> implements DictEntryService {
}
