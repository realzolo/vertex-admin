package com.onezol.platform.controller;

import com.onezol.platform.annotation.ControllerService;
import com.onezol.platform.model.dto.DictKey;
import com.onezol.platform.model.entity.DictKeyEntity;
import com.onezol.platform.model.param.DictKeyParam;
import com.onezol.platform.service.DictKeyService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dict-key")
@ControllerService(service = DictKeyService.class, retClass = DictKey.class)
public class DictKeyController extends GenericController<DictKeyEntity, DictKeyParam> {
}
