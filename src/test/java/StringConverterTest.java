import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;

class StringConverterTest {
    @Test()
    void convert() {
        CommandLine.Help.Ansi ansi = CommandLine.Help.Ansi.AUTO;
        CommandLine.AbstractParseResultHandler<List<Object>> abstractParseResultHandler = new CommandLine.RunLast().useOut(System.out).useAnsi(ansi);
        CommandLine.DefaultExceptionHandler<List<Object>> defaultExceptionHandler = new CommandLine.DefaultExceptionHandler<List<Object>>()
                .useErr(System.err)
                .useAnsi(ansi);
        CommandLine commandLine = new CommandLine(new Arguments()).registerConverter(String.class, new StringConverter());
        String[] args = new String[]{"--account-id", "", "--bucket", ""};
        List<Object> results = commandLine.parseWithHandlers(abstractParseResultHandler, defaultExceptionHandler, args);
        Arguments arguments = (results == null || results.isEmpty()) ? null : (Arguments) results.get(0);
        assertNull(arguments);
    }
}
