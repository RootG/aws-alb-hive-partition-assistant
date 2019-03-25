import picocli.CommandLine;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.time.LocalDate;

public class PartitionAssistant {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, FileNotFoundException {
        Arguments arguments = CommandLine.call(new Arguments(), args);
        if (arguments == null) {
            return;
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
                        .append(partition.getYear())
                        .append('/')
                        .append(String.format("%02d", partition.getMonth()))
                        .append('/')
                        .append(String.format("%02d", partition.getDay()));
                partition.setLocation(stringBuilder.toString());
                hive.addPartition(partition);
                localDate = localDate.plusDays(1);
            }
        }
    }
}
