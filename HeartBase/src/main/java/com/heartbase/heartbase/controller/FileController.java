package com.heartbase.heartbase.controller;

import com.heartbase.heartbase.service.CommandService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dir")
public class FileController {

    private final CommandService commandService;

    public CommandController(CommandService commandService) {this.commandService = commandService;}

    @GetMapping("/ls")
    public String runCommand() {
        return commandService.runCommand("ls");
    }

    @GetMapping("/cd/{where}")
    public String changeDirectory(@PathVariable String where) {
        commandService.runCommand("cd "+where);
        return commandService.runCommand("ls");
    }

    @GetMapping("/rm/{what}")
    public String removeX(@PathVariable String what) {
        return commandService.runCommand("rm -rf "+what);
    }
}