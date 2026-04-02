package heart.backend.heartbase.controller;

import heart.backend.heartbase.service.TerminalService;
import org.springframework.web.bind.annotation.*;
import heart.backend.heartbase.terminal.TerminalMessage;

@RestController
@RequestMapping("/terminal")
public class TerminalController {

    private final TerminalService terminalService;

    public TerminalController(TerminalService terminalService) {
        this.terminalService = terminalService;
    }

    @PostMapping("/exec")
    public String execute(@RequestBody TerminalMessage message) {
        return terminalService.executeCommand(message.getCommand());
    }

    @PostMapping("/close")
    public String close() {
        return terminalService.closeSession();
    }
}