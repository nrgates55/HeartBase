package heart.backend.heartbase.terminal;

public class TerminalMessage {

    private String command;

    public TerminalMessage() {
    }

    public TerminalMessage(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}