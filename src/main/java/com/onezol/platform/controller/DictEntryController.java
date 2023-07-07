package com.onezol.platform.controller;

import com.onezol.platform.annotation.ControllerService;
import com.onezol.platform.model.dto.DictEntry;
import com.onezol.platform.model.entity.DictEntryEntity;
import com.onezol.platform.model.param.DictEntryParam;
import com.onezol.platform.service.DictEntryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dict-key")
@ControllerService(service = DictEntryService.class, retClass = DictEntry.class)
public class DictEntryController extends GenericController<DictEntryEntity, DictEntryParam> {
}
