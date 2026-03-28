package com.heartbase.heartbase.controller;

import com.heartbase.heartbase.service.CommandService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController("/control")
public class CommandController {
    private final CommandService commandService;

    public CommandController(CommandService commandService) {this.commandService = commandService;}

    @PostMapping("/command/{type}")
    public String runCommand(@PathVariable String type) {

        return switch (type) {
            case "uptime" -> commandService.runCommand("uptime");
            case "memory" -> commandService.runCommand("free -h");
            case "disk" -> commandService.runCommand("df -h");
            default -> "Invalid command";
        };
    }
}