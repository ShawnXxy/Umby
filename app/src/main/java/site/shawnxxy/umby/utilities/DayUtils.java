package site.shawnxxy.umby.utilities;

import android.content.Context;
import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import site.shawnxxy.umby.R;


public final class DayUtils {

//    public static final long SEC_IN_MILLIS = 1000;
//    public static final long MIN_IN_MILLIS = SEC_IN_MILLIS * 60;
//    public static final long HR_IN_MILLIS = MIN_IN_MILLIS * 60;
//    public static final long DAY_IN_MILLIS = HR_IN_MILLIS * 24;

//    Milliseconds in a day
    public static final long DAY_IN_MILLIS = TimeUnit.DAYS.toMillis(1);

    /**
     *  Return number of days since Jan 01, 1970, 12:00 Midnight UTC from passed date
     */
    public static long getDayNumber(long date) {
//        TimeZone tz= TimeZone.getDefault();
//        long utcOffset = tz.getOffset(date);
//        return (date + utcOffset) / DAY_IN_MILLIS;
        return TimeUnit.MICROSECONDS.toDays(date);
    }

    /**
     *  Return milliseconds (UTC / GMT) for today's date at midnight in the local time zone
     *
     */
    public static long millisToUtcToday() {
        // Normalize the start date to the beginning of the (UTC) day in local time
//        long retValNew = date / DAY_IN_MILLIS * DAY_IN_MILLIS;

        long utcNowInMillis = System.currentTimeMillis();
        TimeZone currentTimeZone = TimeZone.getDefault();
        long gmtOffsetInMillis = currentTimeZone.getOffset(utcNowInMillis);
        // number of millis since Jan 01, 1970, 12:00 Midnight UTC
        long numOfMillis = utcNowInMillis + gmtOffsetInMillis;
        // Convert millis in number of days
        long millisToDays = TimeUnit.MILLISECONDS.toDays(numOfMillis);
        // convert number of days into millis in UTC
        long millisToUtc= TimeUnit.DAYS.toMillis(millisToDays);
//        return retValNew;
        return millisToUtc;
    }

    /**
    *   Convert passed in date in millis to UTC millis
    */
    public static long millisToDate(long date) {
        long numOfDays = getDayNumber(date);
        return numOfDays * DAY_IN_MILLIS;
    }

    /**
     *  To check and ensure the values passed in is a valid millis
     */
    public static boolean isMillis(long millis) {
        boolean isMillis = false;
        if (millis % DAY_IN_MILLIS == 0) {
            isMillis = true;
        }
        return isMillis;
    }

    /**
     *      Convert UTC to local time
      */
    public static long utcToLocalTime(long utcDate) {
        TimeZone tz = TimeZone.getDefault();
        long gmtOffset = tz.getOffset(utcDate);
        return utcDate - gmtOffset;
    }

    /**
     *      Convert local time to UTC
      */
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
