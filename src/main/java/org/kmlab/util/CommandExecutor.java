package org.kmlab.util;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CommandExecutor {
    private static final Logger logger = LogManager.getLogger(CommandExecutor.class);

    public static void execute(String command) {
        execute(command, null);
    }

    /**
     * Executes a system command and optionally logs the output to a specified log file.
     *
     * @param command the command to execute. This is passed to the {@link ProcessBuilder} to start the process.
     * @param logFile the file to which the standard output and error streams should be logged. If this parameter is null or empty, logging is skipped.
     *
     * @throws RuntimeException if an IOException or InterruptedException occurs during the command execution or logging process.
     *                          Specific scenarios include:
     *                          <ul>
     *                              <li>If the command execution fails due to an IO issue.</li>
     *                              <li>If the command execution is interrupted.</li>
     *                              <li>If there is an issue writing to the log file.</li>
     *                          </ul>
     */
    public static void execute(String command, String logFile) {
        logger.info("Executing command: {}", command);
        ProcessBuilder processBuilder = new ProcessBuilder(command);

        try {
            Process process = processBuilder.start();

            if (logFile != null && !logFile.isEmpty()) {
                try (PrintWriter stdLogWriter = new PrintWriter(new FileWriter(logFile))) {
                    Thread stdOutThread = new LogThread(process.getInputStream(), stdLogWriter, "stdout");
                    Thread stdErrThread = new LogThread(process.getErrorStream(), stdLogWriter, "stderr");
                    stdOutThread.start();
                    stdErrThread.start();

                    stdOutThread.join();
                    stdErrThread.join();
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException("Log writing or thread processing is exceptional: " + e.getMessage());
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new InterruptedException("Command execution failure, exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Command execution exception: " + e.getMessage());
        }
    }

    private static class LogThread extends Thread {
        private final InputStream inputStream;
        private final PrintWriter writer;
        private final String logPrefix;

        public LogThread(InputStream inputStream, PrintWriter writer, String logPrefix) {
            this.inputStream = inputStream;
            this.writer = writer;
            this.logPrefix = logPrefix;
        }

        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    writer.println(logPrefix + ": " + line);
                }
            } catch (IOException e) {
                throw new RuntimeException("The read command output is exceptional" + e.getMessage());
            }
        }
    }
}
