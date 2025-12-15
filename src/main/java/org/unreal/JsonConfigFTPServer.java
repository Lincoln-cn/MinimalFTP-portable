package org.unreal;

import com.guichaguri.minimalftp.FTPServer;
import org.unreal.auth.JsonAuthenticator;
import org.unreal.config.FtpConfig;

import java.io.IOException;
import java.util.logging.*;

/**
 * Hello world!
 *
 */
public class JsonConfigFTPServer {
    private static final Logger logger = Logger.getLogger(JsonConfigFTPServer.class.getName());


    public static void setupFileLogging(String logFile) throws IOException {
        // 清除默认控制台处理器
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        for (Handler handler : handlers) {
            if (handler instanceof ConsoleHandler) {
                rootLogger.removeHandler(handler);
                handler.close();
            }
        }

        // 创建文件处理器
        FileHandler fileHandler = getFileHandler(logFile);

        rootLogger.addHandler(fileHandler);
        rootLogger.setLevel(Level.ALL);
        logger.info("Logging to file: " + logFile);
    }

    private static FileHandler getFileHandler(String logFile) throws IOException {
        FileHandler fileHandler = new FileHandler(logFile, true); // true = append
        fileHandler.setFormatter(new SimpleFormatter() {
            private static final String FORMAT = "%1$tF %1$tT [%4$-7s] %5$s %n";

            @Override
            public synchronized String format(LogRecord lr) {
                return String.format(FORMAT,
                        lr.getMillis(),
                        lr.getLevel(),
                        lr.getSourceClassName(),
                        lr.getLevel(),
                        lr.getMessage()
                );
            }
        });
        return fileHandler;
    }

    public static void main(String[] args) throws Exception {
        String configFile = System.getProperty("ftp.config", "ftp-config.json");
        String logFile = System.getProperty("ftp.log", "ftp-server.log");

        setupFileLogging(logFile);


        FtpConfig config = FtpConfig.load(configFile);
        JsonAuthenticator auth = new JsonAuthenticator(config);
        try (FTPServer server = new FTPServer(auth)) {
            logger.info("Starting FTP server on port " + config.port);
            server.listenSync(config.port);
        } catch (Exception e) {
            logger.severe("FTP server encountered an error: " + e.getMessage());
            logger.throwing(JsonConfigFTPServer.class.getName(), "main", e);
            throw e;
        }
    }
}