package heart.backend.heartbase.service;

import org.springframework.stereotype.Service;

@Service
public class PowerService {

    public String powerOn() {
        try {
            return "Wake handled externally (WOL packet)";
        } catch (Exception e) {
            return "Failed to wake: " + e.getMessage();
        }
    }

    public String powerOff() {
        try {
            ProcessBuilder pb = new ProcessBuilder("systemctl", "suspend");
            pb.start();
            return "Sleep command sent.";
        } catch (Exception e) {
            return "Failed to put system to sleep: " + e.getMessage();
        }
    }

    public String reset() {
        try {
            ProcessBuilder pb = new ProcessBuilder("systemctl", "reboot");
            pb.start();
            return "Restart command sent.";
        } catch (Exception e) {
            return "Failed to restart system: " + e.getMessage();
        }
    }
}