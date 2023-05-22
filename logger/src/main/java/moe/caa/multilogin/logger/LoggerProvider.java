package moe.caa.multilogin.logger;


import moe.caa.multilogin.logger.bridges.ConsoleBridge;

/**
 * 日志提供程序
 */
public class LoggerProvider {
    private static Logger logger = new ConsoleBridge();

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger logger) {
        LoggerProvider.logger = logger;
    }
}