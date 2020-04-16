package experiment.date;

import java.time.*;
import java.util.Date;

/**
 * Created by liangchuan on 2017/6/14.
 */
public class DateTest {
    public static void main(String[] args) {
        Date date = new Date();
        System.out.println(date);
        System.out.println(date.getTime());

        Clock clock = Clock.systemUTC();
        System.out.println(ZoneId.getAvailableZoneIds());
        LocalDate dateFromClock = LocalDate.now(clock);
        LocalTime timeFromClock = LocalTime.now(clock);
        System.out.println(clock.millis());
        System.out.println(dateFromClock);
        System.out.println(timeFromClock);

        // Change zone does not change milliseconds.
        clock = clock.withZone(ZoneId.of("Pacific/Pago_Pago"));
        dateFromClock = LocalDate.now(clock);
        timeFromClock = LocalTime.now(clock);
        System.out.println(clock.millis());
        System.out.println(dateFromClock);
        System.out.println(timeFromClock);

        // 一个简化的 date，不如 date 丰富。
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime);

    }
}
