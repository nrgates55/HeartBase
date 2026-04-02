package heart.backend.heartbase.service;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TerminalService {

    private ShellSession shellSession;

    public TerminalService() {
        try {
            this.shellSession = new ShellSession();
        } catch (IOException e) {
            throw new RuntimeException("Failed to start shell session.", e);
        }
    }

    public synchronized String executeCommand(String command) {
        try {
            if (shellSession == null || !shellSession.isAlive()) {
                shellSession = new ShellSession();
            }
            return shellSession.executeCommand(command);
        } catch (Exception e) {
            return "Command execution failed: " + e.getMessage();
        }
    }

    public synchronized String closeSession() {
        if (shellSession != null) {
            shellSession.close();
            shellSession = null;
        }
        return "Shell session closed.";
    }
}