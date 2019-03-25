import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

public class Hive implements Closeable {
    private static final int INVALID_PARTITION = 10006;
    private final Connection connection;

    public Hive(String hiveUrl) throws ClassNotFoundException, SQLException {
        Class.forName("org.apache.hive.jdbc.HiveDriver");
        Properties properties = new Properties();
        properties.put("connectTimeout", 5000);
        connection = DriverManager
                .getConnection("jdbc:hive2://" + hiveUrl + "/default", properties);
    }

    public void createTable(String tableName, String path, String accountId, String region) throws SQLException, FileNotFoundException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("alb_logs_table.sql");
        try (Scanner scanner = new Scanner(inputStream).useDelimiter("\\Z")) {
            String createTableSql = String.format(scanner.next(),
                    tableName,
                    path,
                    accountId,
                    region);
            try (Statement statement = connection.createStatement()) {
                statement.execute(createTableSql);
            }
        }
    }

    public void addPartition(Partition partition) throws SQLException {
        if (exists(partition)) {
            return;
        }
        String addPartitionSql = String.format("ALTER TABLE %s ADD PARTITION (pyear = %d, pmonth = %d, pday = %d) location '%s'",
                partition.getTableName(),
                partition.getYear(),
                partition.getMonth(),
                partition.getDay(),
                partition.getLocation());
        try (Statement statement = connection.createStatement()) {
            statement.execute(addPartitionSql);
        }
    }

    private boolean exists(Partition partition) throws SQLException {
        String descPartitionSql = String.format("DESC %s PARTITION(pyear = %d, pmonth = %d, pday = %d)",
                partition.getTableName(),
                partition.getYear(),
                partition.getMonth(),
                partition.getDay());

        try (Statement statement = connection.createStatement()) {
            statement.execute(descPartitionSql);
        } catch (SQLException ex) {
            if (ex.getErrorCode() == INVALID_PARTITION) {
                return false;
            }
            throw ex;
        }
        return true;
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
