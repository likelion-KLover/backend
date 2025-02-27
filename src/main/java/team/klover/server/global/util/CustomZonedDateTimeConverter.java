package team.klover.server.global.util;

import org.springframework.data.elasticsearch.core.mapping.PropertyValueConverter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CustomZonedDateTimeConverter implements PropertyValueConverter {

    private final DateTimeFormatter formatterWithZone = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSSSSSX");
    private final DateTimeFormatter formatterWithoutZone1 = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSSSSS");
    private final DateTimeFormatter formatterWithoutZone2 = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSS");

    @Override
    public Object write(Object value) {
        if (value instanceof ZonedDateTime zonedDateTime) {
            return formatterWithZone.format(zonedDateTime);
        } else {
            return value;
        }
    }

    @Override
    public Object read(Object value) {
        if (value instanceof String s) {
            try {
                return formatterWithZone.parse(s, ZonedDateTime::from);
            } catch (DateTimeParseException e1) {
                try {
                    return formatterWithoutZone1.parse(s, LocalDateTime::from).atZone(ZoneId.of("Asia/Seoul"));
                }catch (DateTimeParseException e2){
                    return formatterWithoutZone2.parse(s, LocalDateTime::from).atZone(ZoneId.of("Asia/Seoul"));
                }
            }
        } else {
            return value;
        }
    }
}