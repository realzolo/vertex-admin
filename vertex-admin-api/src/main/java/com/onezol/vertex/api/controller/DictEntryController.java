package com.onezol.vertex.api.controller;

import com.onezol.vertex.core.annotation.ControllerService;
import com.onezol.vertex.core.model.dto.DictEntry;
import com.onezol.vertex.core.model.entity.DictEntryEntity;
import com.onezol.vertex.core.model.param.DictEntryParam;
import com.onezol.vertex.core.service.DictEntryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dict-key")
@ControllerService(service = DictEntryService.class, retClass = DictEntry.class)
public class DictEntryController extends GenericController<DictEntryEntity, DictEntryParam> {
}
