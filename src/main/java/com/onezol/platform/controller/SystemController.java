package com.onezol.platform.controller;

import com.onezol.platform.manager.SystemInformation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system")
public class SystemController {

    @GetMapping("/info")
    public SystemInformation getSystemInfo() {
        return SystemInformation.currentSystemInfo();
    }

}
