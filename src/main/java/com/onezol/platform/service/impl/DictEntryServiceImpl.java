package com.onezol.platform.service.impl;

import com.onezol.platform.mapper.DictEntryMapper;
import com.onezol.platform.model.entity.DictEntryEntity;
import com.onezol.platform.service.DictEntryService;
import org.springframework.stereotype.Service;

@Service
public class DictEntryServiceImpl extends GenericServiceImpl<DictEntryMapper, DictEntryEntity> implements DictEntryService {
}
