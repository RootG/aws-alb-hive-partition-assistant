import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import picocli.CommandLine;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PartitionAssistant {
    private static final Logger LOGGER = LogManager.getLogger(PartitionAssistant.class);

    public static void main(String[] args) throws SQLException, ClassNotFoundException, FileNotFoundException {
        CommandLine.Help.Ansi ansi = CommandLine.Help.Ansi.AUTO;
        CommandLine.AbstractParseResultHandler<List<Object>> abstractParseResultHandler = new CommandLine.RunLast().useOut(System.out).useAnsi(ansi);
        CommandLine.DefaultExceptionHandler<List<Object>> defaultExceptionHandler = new CommandLine.DefaultExceptionHandler<List<Object>>()
                .useErr(System.err)
                .useAnsi(ansi);
        CommandLine commandLine = new CommandLine(new Arguments())
                .registerConverter(LocalDate.class, new LocalDateConverter())
                .registerConverter(String.class, new StringConverter());
        List<Object> results = commandLine.parseWithHandlers(abstractParseResultHandler, defaultExceptionHandler, args);
        Arguments arguments = (results == null || results.isEmpty()) ? null : (Arguments) results.get(0);
        if (arguments == null) {
            return;
        }
        if (arguments.isVerbose()) {
            Configurator.setLevel(PartitionAssistant.class.getCanonicalName(), Level.ALL);
        }
        String path;
        if (arguments.getPrefix() == null || arguments.getPrefix().isEmpty()) {
            path = arguments.getBucket();
        } else {
            path = arguments.getBucket() + "/" + arguments.getPrefix();
        }
        try (Hive hive = new Hive(arguments.getHiveUrl())) {
            hive.createTable(arguments.getTableName(), path, arguments.getAccountId(), arguments.getRegion());
            LocalDate localDate = LocalDate.from(arguments.getFrom());
            while (localDate.compareTo(arguments.getTo()) <= 0) {
                Partition partition = new Partition();
                partition.setTableName(arguments.getTableName());
                partition.setYear(localDate.getYear());
                partition.setMonth(localDate.getMonthValue());
                partition.setDay(localDate.getDayOfMonth());
                StringBuilder stringBuilder = new StringBuilder(String.format("s3a://%s/AWSLogs/%s/elasticloadbalancing/%s",
                        path,
                        arguments.getAccountId(),
                        arguments.getRegion()))
                        .append('/')
                        .append(String.format("%04d", partition.getYear()))
                        .append('/')
                        .append(String.format("%02d", partition.getMonth()))
                        .append('/')
                        .append(String.format("%02d", partition.getDay()));
                partition.setLocation(stringBuilder.toString());
                LOGGER.info(stringBuilder.toString());
                hive.addPartition(partition);
                localDate = localDate.plusDays(1);
            }
        }
    }
}
