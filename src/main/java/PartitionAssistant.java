import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.time.LocalDate;

public class PartitionAssistant {
    private static final String HIVE_URL = "localhost:10000";
    private static final String AWS_ALB_LOG_LOCATION = "s3a://%s/AWSLogs/%s/elasticloadbalancing/%s";

    public static void main(String[] args) throws SQLException, ClassNotFoundException, FileNotFoundException {
        LocalDate from = LocalDate.now();
        LocalDate to = LocalDate.now();
        String tableName = "";
        String albLogsDirectory = "";
        String accountId = "";
        String region = "";
        try (Hive hive = new Hive(HIVE_URL)) {
            hive.createTable(tableName, albLogsDirectory, accountId, region);
            LocalDate localDate = LocalDate.from(from);
            while (localDate.compareTo(to) <= 0) {
                localDate = localDate.plusDays(1);
                Partition partition = new Partition();
                partition.setTableName(tableName);
                partition.setYear(localDate.getYear());
                partition.setMonth(localDate.getMonthValue());
                partition.setDay(localDate.getDayOfMonth());
                StringBuilder stringBuilder = new StringBuilder(String.format(AWS_ALB_LOG_LOCATION,
                        albLogsDirectory,
                        accountId,
                        region));
                stringBuilder.append('/')
                        .append(partition.getYear())
                        .append('/')
                        .append(partition.getMonth())
                        .append('/')
                        .append(partition.getDay());
                partition.setLocation(stringBuilder.toString());
                hive.addPartition(partition);
            }
        }
    }
}
