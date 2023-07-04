package com.onezol.platform.controller;

import com.onezol.platform.annotation.ControllerService;
import com.onezol.platform.model.dto.DictValue;
import com.onezol.platform.model.entity.DictValueEntity;
import com.onezol.platform.model.param.DictValueParam;
import com.onezol.platform.service.DictValueService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dict-value")
@ControllerService(service = DictValueService.class, retClass = DictValue.class)
public class DictValueController extends GenericController<DictValueEntity, DictValueParam> {
}
