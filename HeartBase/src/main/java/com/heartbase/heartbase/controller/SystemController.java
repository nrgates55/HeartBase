package com.heartbase.heartbase.controller;

import com.heartbase.heartbase.service.SystemService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
@RestController()
@RequestMapping("/api")
public class SystemController {

    private final SystemService systemService;

    public SystemController(SystemService systemService) {
        this.systemService = systemService;
    }

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        return systemService.getSystemStats();
    }
}