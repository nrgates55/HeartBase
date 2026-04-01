package com.heartbase.heartbase.controller;

import com.heartbase.heartbase.service.CommandService;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/command")
public class CommandController {
    private final CommandService commandService;

    public CommandController(CommandService commandService) {this.commandService = commandService;}

    @PostMapping("/{type}")
    public String runCommand(@PathVariable String type) {

        return switch (type) {
            case "uptime" -> commandService.runCommand("uptime");
            case "memory" -> commandService.runCommand("free -h");
            case "disk" -> commandService.runCommand("df -h");
            default -> "Invalid command";
        };
    }
}