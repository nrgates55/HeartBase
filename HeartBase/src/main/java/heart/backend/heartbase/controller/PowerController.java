package heart.backend.heartbase.controller;

import heart.backend.heartbase.service.PowerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/power")
public class PowerController{
    private final PowerService powerService;

    public PowerController(PowerService service){
        this.powerService=service;
    }

    @PostMapping("/on")
    public String powerOn(){
        return powerService.powerOn();
    }

    @PostMapping("/off")
    public String powerOff(){
        return powerService.powerOff();
    }

    @PostMapping("/reset")
    public String reset() {
        return powerService.reset();
    }
}