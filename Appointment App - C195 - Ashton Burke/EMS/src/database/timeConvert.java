package database;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * Time conversion method
 * converts several different time zones (UTC, EST, LOCAL)
 * into each other to help with the conversion process when applying methods and functions that utilize time zones
 */
public class timeConvert {


    public static String timeDateConversion(String timeDate)
    {

        Timestamp timestamp = Timestamp.valueOf(String.valueOf(timeDate));
        LocalDateTime localdatetime = timestamp.toLocalDateTime();
        ZonedDateTime zoneddatetime = localdatetime.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
        ZonedDateTime datetimeUTC = zoneddatetime.withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime datetimeOUT = datetimeUTC.toLocalDateTime();
        String datetimeUTCOUT = datetimeOUT.format(DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss"));
        return datetimeUTCOUT;


    }

}
