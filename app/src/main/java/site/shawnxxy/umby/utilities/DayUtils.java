package site.shawnxxy.umby.utilities;

import android.content.Context;
import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import site.shawnxxy.umby.R;

public final class DayUtils {

    public static final long SEC_IN_MILLIS = 1000;
    public static final long MIN_IN_MILLIS = SEC_IN_MILLIS * 60;
    public static final long HR_IN_MILLIS = MIN_IN_MILLIS * 60;
    public static final long DAY_IN_MILLIS = HR_IN_MILLIS * 24;

    public static long getDayNumber(long date) {
        TimeZone tz= TimeZone.getDefault();
        long utcOffset = tz.getOffset(date);
        return (date + utcOffset) / DAY_IN_MILLIS;
    }

    /**
     * To make it easy to query for the exact date, we normalize all dates that go into
     * the database to the start of the day in UTC time.
     *
     * @param date The UTC date to normalize
     *
     * @return The UTC date at 12 midnight
     */
    public static long normalizeDate(long date) {
        // Normalize the start date to the beginning of the (UTC) day in local time
        long retValNew = date / DAY_IN_MILLIS * DAY_IN_MILLIS;
        return retValNew;
    }

    // Convert UTC to local time
    public static long utcToLocalTime(long utcDate) {
        TimeZone tz = TimeZone.getDefault();
        long gmtOffset = tz.getOffset(utcDate);
        return utcDate - gmtOffset;
    }

    // Convert local time to UTC
    public static long localTimeToUTC (long localDate) {
        TimeZone tz = TimeZone.getDefault();
        long gmtOffset = tz.getOffset(localDate);
        return localDate + gmtOffset;
    }

    // Date info formatter
    public static String dateFormatted(Context c, long dateInMillis, boolean showFullDate) {
        long localDate = utcToLocalTime(dateInMillis);
        long dayNum = getDayNumber(localDate);
        long currentDayNum = getDayNumber(System.currentTimeMillis());

        if (dayNum == currentDayNum || showFullDate) { // today
            String day = getDay(c, localDate);
            String readableDate =  getReadableDateString(c, localDate);
            if (dayNum - currentDayNum < 2) { // tomorrow
                String localizedDayName = new SimpleDateFormat("EEEE").format(localDate);
                return readableDate.replace(localizedDayName, day);
            } else {
                return readableDate;
            }
        } else if (dayNum < currentDayNum + 7) {
            return getDay(c, localDate);
        } else {
            int flags = DateUtils.FORMAT_SHOW_DATE
                    | DateUtils.FORMAT_NO_YEAR
                    | DateUtils.FORMAT_ABBREV_ALL
                    | DateUtils.FORMAT_SHOW_WEEKDAY;
            return DateUtils.formatDateTime(c, localDate, flags);
        }

    }

    private static String getReadableDateString(Context context, long timeInMillis) {
        int flags = DateUtils.FORMAT_SHOW_DATE
                | DateUtils.FORMAT_NO_YEAR
                | DateUtils.FORMAT_SHOW_WEEKDAY;

        return DateUtils.formatDateTime(context, timeInMillis, flags);
    }

    // today, tomorrow, Monday, Tuesday...
    private static String getDay(Context c, long dateInMillis) {
        long dayNum = getDayNumber(dateInMillis);
        long currentDayNum = getDayNumber(System.currentTimeMillis());
        if (dayNum == currentDayNum) {
            return c.getString(R.string.today);
        } else if (dayNum == currentDayNum + 1) {
            return c.getString(R.string.tomorrow);
        } else {
            SimpleDateFormat day = new SimpleDateFormat("EEEE");
            return day.format(dateInMillis);
        }
    }
}
