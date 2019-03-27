import picocli.CommandLine;

public class StringConverter implements CommandLine.ITypeConverter<String> {
    @Override
    public String convert(String value) throws Exception {
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Empty string.");
        }
        return null;
    }
}
