package app_chapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Created by Igor Konovalov on 20.11.2017.
 */
public class LogFormatter extends Formatter {

    private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

    @Override
    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder(1000);
        builder.append(df.format(new Date(record.getMillis()))).append(" - ");
        builder.append("(").append(record.getSourceClassName().substring(record.getSourceClassName().indexOf('.'))).append(") - ");
        builder.append("{").append(record.getSourceMethodName()).append("} - ");
        builder.append("[").append(record.getLevel()).append("] -- ");
        builder.append(formatMessage(record)).append("\r\n");
        return builder.toString();
    }

}
