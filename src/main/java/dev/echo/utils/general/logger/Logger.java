package dev.echo.utils.general.logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger extends LoggerColor {

    public static void log(LogLevel level, String msg) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

        LocalDateTime now = LocalDateTime.now();

        if (level == LogLevel.INFO)
            System.out.println(ANSI_CYAN + " (" + level.name() + ") " + ANSI_RESET + msg);
        else if (level == LogLevel.WARNING)
            System.out.println(ANSI_YELLOW + " (" + level.name() + ") " + ANSI_RESET + msg);
        else if (level == LogLevel.ERROR)
            System.out.println(ANSI_RED + " (" + level.name() + ") " + ANSI_RESET + msg);
    }


}


