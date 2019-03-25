import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import picocli.CommandLine;
import picocli.CommandLine.Option;

import java.time.LocalDate;
import java.util.concurrent.Callable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@CommandLine.Command(name = "java -jar aws-alb-hive-partition-assistant.jar")
public class Arguments implements Callable<Arguments> {
    @Option(names = {"--account-id"}, required = true, description = "AWS account ID.")
    private String accountId;
    @Option(names = {"--bucket"}, required = true, description = "The name of the S3 bucket.")
    private String bucket;
    @Option(names = {"--from"}, required = true, description = "From date, inclusive.")
    private LocalDate from;
    @Option(names = {"--hive-url"}, description = "Hive url. (default: ${DEFAULT-VALUE})")
    private String hiveUrl = "localhost:10000";
    @Option(names = {"--prefix"}, description = "The prefix (logical hierarchy) in the bucket. (default: ${DEFAULT-VALUE})")
    private String prefix = "";
    @Option(names = {"--region"}, description = "AWS region. (default: ${DEFAULT-VALUE})")
    private String region = "us-east-1";
    @Option(names = {"--table-name"}, description = "Table name. (default: ${DEFAULT-VALUE})")
    private String tableName = "alb_logs";
    @Option(names = {"--to"}, description = "To date, inclusive. (default: ${DEFAULT-VALUE})")
    private LocalDate to = LocalDate.now();
    @Option(names = {"--verbose"}, description = "Be verbose.")
    private boolean verbose = false;

    @Override
    public Arguments call() {
        return this;
    }
}
