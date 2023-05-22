package moe.caa.multilogin.logger.bridges;

import moe.caa.multilogin.logger.Level;
import moe.caa.multilogin.logger.Logger;

import java.io.PrintStream;

/**
 * 控制台日志
 */
public class ConsoleBridge implements Logger {


    @Override
    public void log(Level level, String message, Throwable throwable) {
        String format = String.format("[%s] %s", level.name(), message);

        PrintStream printStream = switch (level) {
            case TRACE, DEBUG, INFO -> System.out;
            default -> System.err;
        };
        printStream.println(format);
        if (throwable != null){
            throwable.printStackTrace(printStream);
        }
    }
}