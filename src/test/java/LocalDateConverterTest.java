import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LocalDateConverterTest {
    @Test
    public void testConvertDelimiterDashMissingLeadingZero() {
        CommandLine.Help.Ansi ansi = CommandLine.Help.Ansi.AUTO;
        CommandLine.AbstractParseResultHandler<List<Object>> abstractParseResultHandler = new CommandLine.RunLast().useOut(System.out).useAnsi(ansi);
        CommandLine.DefaultExceptionHandler<List<Object>> defaultExceptionHandler = new CommandLine.DefaultExceptionHandler<List<Object>>()
                .useErr(System.err)
                .useAnsi(ansi);
        CommandLine commandLine = new CommandLine(new Arguments()).registerConverter(LocalDate.class, new LocalDateConverter());
        String[] args = new String[]{"--account-id", "", "--bucket", "", "--from", "2019-01-1"};
        List<Object> results = commandLine.parseWithHandlers(abstractParseResultHandler, defaultExceptionHandler, args);
        Arguments arguments = (results == null || results.isEmpty()) ? null : (Arguments) results.get(0);
        assertNotNull(arguments);
        assertNotNull(arguments.getFrom());
    }

    @Test
    public void testConvertDelimiterSlashWithMissingLeadingZero() {
        CommandLine.Help.Ansi ansi = CommandLine.Help.Ansi.AUTO;
        CommandLine.AbstractParseResultHandler<List<Object>> abstractParseResultHandler = new CommandLine.RunLast().useOut(System.out).useAnsi(ansi);
        CommandLine.DefaultExceptionHandler<List<Object>> defaultExceptionHandler = new CommandLine.DefaultExceptionHandler<List<Object>>()
                .useErr(System.err)
                .useAnsi(ansi);
        CommandLine commandLine = new CommandLine(new Arguments()).registerConverter(LocalDate.class, new LocalDateConverter());
        String[] args = new String[]{"--account-id", "", "--bucket", "", "--from", "2019/01/1"};
        List<Object> results = commandLine.parseWithHandlers(abstractParseResultHandler, defaultExceptionHandler, args);
        Arguments arguments = (results == null || results.isEmpty()) ? null : (Arguments) results.get(0);
        assertNotNull(arguments);
        assertNotNull(arguments.getFrom());
    }
}
