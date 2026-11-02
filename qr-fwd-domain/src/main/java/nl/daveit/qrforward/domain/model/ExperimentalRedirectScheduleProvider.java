package nl.daveit.qrforward.domain.model;

import java.time.LocalTime;
import java.util.*;

public class ExperimentalRedirectScheduleProvider implements RedirectScheduleProvider {

    private final RedirectKey redirectKey;
    private final Queue<RedirectScheduleEntry> orderedSchedule;
    private RedirectKey current;
    private RedirectKey next;

    public ExperimentalRedirectScheduleProvider(List<RedirectScheduleEntry> redirectSchedule, RedirectKey redirectKey) {
        this.redirectKey = redirectKey;
        this.orderedSchedule = createSchedule(redirectSchedule);
    }

    @Override
    public RedirectScheduleEntry getCurrentRedirectSchedule() {
        return null;
    }

    public static Queue<RedirectScheduleEntry> createSchedule(List<RedirectScheduleEntry> periods) {
        LocalTime now = LocalTime.now();
        Queue<RedirectScheduleEntry> queue = new LinkedList<>();

        Comparator<RedirectScheduleEntry> byRelationToNow = Comparator
                .<RedirectScheduleEntry>comparingInt(entry -> relationRank(entry, now))
                .thenComparing(RedirectScheduleEntry::start);

        periods.stream()
                .sorted(byRelationToNow)
                .forEach(queue::add);

        return queue;
    }

    private static int relationRank(RedirectScheduleEntry entry, LocalTime now) {
        if (isActive(entry, now)) return 0;
        if (isUpcoming(entry, now)) return 1;
        return 2;
    }

    private static boolean isActive(RedirectScheduleEntry entry, LocalTime now) {
        boolean wrapsMidnight = entry.end().isBefore(entry.start()) || entry.end().equals(entry.start());
        if (wrapsMidnight) {
            return !now.isBefore(entry.start()) || now.isBefore(entry.end());
        }
        return !now.isBefore(entry.start()) && now.isBefore(entry.end());
    }

    private static boolean isUpcoming(RedirectScheduleEntry p, LocalTime now) {
        boolean wrapsMidnight = p.end().isBefore(p.start()) || p.end().equals(p.start());
        if (wrapsMidnight) {
            return now.isBefore(p.start()) && !now.isBefore(p.end());
        }
        return now.isBefore(p.start());
    }
}
