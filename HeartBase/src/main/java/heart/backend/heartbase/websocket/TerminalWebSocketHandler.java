package heart.backend.heartbase.websocket;

import tools.jackson.databind.ObjectMapper;
import heart.backend.heartbase.terminal.TerminalMessage;
import heart.backend.heartbase.service.TerminalService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class TerminalWebSocketHandler extends TextWebSocketHandler {

    private final TerminalService terminalService;
    private final ObjectMapper objectMapper;

    public TerminalWebSocketHandler(TerminalService terminalService, ObjectMapper objectMapper) {
        this.terminalService = terminalService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        session.sendMessage(new TextMessage("Connected to HeartBase terminal."));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            TerminalMessage terminalMessage =
                    objectMapper.readValue(message.getPayload(), TerminalMessage.class);

            String command = terminalMessage.getCommand();

            if (command == null || command.trim().isEmpty()) {
                session.sendMessage(new TextMessage("No command received."));
                return;
            }

            String output = terminalService.executeCommand(command);

            if (output == null || output.isBlank()) {
                output = "[no output]";
            }

            session.sendMessage(new TextMessage(output));

        } catch (Exception e) {
            session.sendMessage(new TextMessage("Command failed: " + e.getMessage()));
        }
    }
}