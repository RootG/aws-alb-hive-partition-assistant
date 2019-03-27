import picocli.CommandLine;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;

public class LocalDateConverter implements CommandLine.ITypeConverter<LocalDate> {
    private final DateTimeFormatter dateTimeFormatter;

    public LocalDateConverter() {
        dateTimeFormatter = new DateTimeFormatterBuilder()
                .appendOptional(DateTimeFormatter.ofPattern("yyyy-M-d"))
                .appendOptional(DateTimeFormatter.ofPattern("yyyy-M-dd"))
                .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-d"))
                .appendOptional(DateTimeFormatter.ofPattern("yyyy-M-dd"))
                .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                .appendOptional(DateTimeFormatter.ofPattern("yyyy/M/d"))
                .appendOptional(DateTimeFormatter.ofPattern("yyyy/M/dd"))
                .appendOptional(DateTimeFormatter.ofPattern("yyyy/MM/d"))
                .appendOptional(DateTimeFormatter.ofPattern("yyyy/M/dd"))
                .appendOptional(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
                .appendOptional(DateTimeFormatter.ofPattern("d-M-yyyy"))
                .appendOptional(DateTimeFormatter.ofPattern("dd-M-yyyy"))
                .appendOptional(DateTimeFormatter.ofPattern("d-MM-yyyy"))
                .appendOptional(DateTimeFormatter.ofPattern("dd-M-yyyy"))
                .appendOptional(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                .appendOptional(DateTimeFormatter.ofPattern("d/M/yyyy"))
                .appendOptional(DateTimeFormatter.ofPattern("dd/M/yyyy"))
                .appendOptional(DateTimeFormatter.ofPattern("d/MM/yyyy"))
                .appendOptional(DateTimeFormatter.ofPattern("dd/M/yyyy"))
                .appendOptional(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                .toFormatter();
    }

    @Override
    public LocalDate convert(String value) throws Exception {
        try {
            return LocalDate.parse(value, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Example date format: yyyy-MM-dd, 2019-03-27");
        }
    }
}
