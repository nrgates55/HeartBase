package heart.backend.heartbase.terminal;

import java.io.*;

public class ShellSession {

    private final Process process;
    private final BufferedWriter writer;
    private final BufferedReader stdoutReader;
    private final BufferedReader stderrReader;

    public ShellSession() throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash");
        processBuilder.redirectErrorStream(false);
        this.process = processBuilder.start();

        this.writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        this.stdoutReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        this.stderrReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
    }

    public synchronized String executeCommand(String command) throws IOException {
        if (!isAlive()) {
            throw new IllegalStateException("Shell session is no longer running.");
        }

        String marker = "__HEARTBASE_CMD_DONE__";

        writer.write(command);
        writer.newLine();
        writer.write("echo " + marker);
        writer.newLine();
        writer.flush();

        StringBuilder output = new StringBuilder();
        String line;

        while ((line = stdoutReader.readLine()) != null) {
            if (line.equals(marker)) {
                break;
            }
            output.append(line).append(System.lineSeparator());
        }

        while (stderrReader.ready()) {
            output.append(stderrReader.readLine()).append(System.lineSeparator());
        }

        return output.toString().trim();
    }

    public synchronized boolean isAlive() {
        return process.isAlive();
    }

    public synchronized void close() {
        try {
            writer.write("exit");
            writer.newLine();
            writer.flush();
        } catch (IOException ignored) {
        }

        try {
            writer.close();
        } catch (IOException ignored) {
        }

        try {
            stdoutReader.close();
        } catch (IOException ignored) {
        }

        try {
            stderrReader.close();
        } catch (IOException ignored) {
        }

        process.destroy();
    }
}