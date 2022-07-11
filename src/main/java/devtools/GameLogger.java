package devtools;

import java.util.Arrays;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public final class GameLogger {

    private static final Logger INSTANCE = new GameLogger().logger;
    
    private Logger logger;
    private FileHandler fileHandler;
    
    private static SimpleFormatter FORMATTER = new SimpleFormatter(){
        private final String format = "[%1$tF %1$tT] [%2$-7s] [%3$-7s] %4$s %n";
        @Override
        public synchronized String format(LogRecord lr) {
            return String.format(format,
                    new Date(lr.getMillis()),
                    lr.getLevel().getLocalizedName(),
                    lr.getSourceClassName()+" "+lr.getSourceMethodName(),
                    lr.getMessage()
            );
        }
    };
    
    private GameLogger() {
        logger = Logger.getLogger("PROJECT_LOGGER");
        ConsoleHandler consoleHandler = new ConsoleHandler();
        try {
            fileHandler = new FileHandler("build/logs/log");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // logger.addHandler(fileHandler);
        logger.addHandler(consoleHandler);
        // Arrays.asList(logger.getHandlers()).forEach(h -> h.setFormatter(FORMATTER));
    }

    public static final Logger get() {
        GameLogger.setFormatter(FORMATTER, true);
        return INSTANCE;
    }

    public static final void addHandler(String path) {
        FileHandler fh;
        try {
            fh = new FileHandler(path);
            fh.setFormatter(FORMATTER);
            INSTANCE.addHandler(fh);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final void setFormatter(SimpleFormatter formatter, boolean all) {
        // if change all of them or not
        FORMATTER = formatter;
        if (all) {
            Arrays.asList(INSTANCE.getHandlers()).forEach(h -> h.setFormatter(FORMATTER));
        }
    }


    
}
