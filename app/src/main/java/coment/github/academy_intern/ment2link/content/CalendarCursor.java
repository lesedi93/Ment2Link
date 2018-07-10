package coment.github.academy_intern.ment2link.content;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.provider.CalendarContract;

/**
 * {@link android.provider.CalendarContract.Calendars} cursor wrapper
 */
public class CalendarCursor extends CursorWrapper {

    /**
     * {@link android.provider.CalendarContract.Calendars} query projection
     */
    public static final String[] PROJECTION = new String[]{
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME
    };
    private static final int PROJECTION_INDEX_ID = 0;
    private static final int PROJECTION_INDEX_DISPLAY_NAME = 1;

    public CalendarCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Gets ment2link ID
     * @return  ment2link ID
     */
    public long getId() {
        return getLong(PROJECTION_INDEX_ID);
    }

    /**
     * Gets ment2link display name
     * @return  ment2link display name
     */
    public String getDisplayName() {
        return getString(PROJECTION_INDEX_DISPLAY_NAME);
    }
}
