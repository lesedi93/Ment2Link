package coment.github.academy_intern.ment2link;

import android.Manifest;
import android.content.AsyncQueryHandler;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import coment.github.academy_intern.ment2link.activities.SearchMentorActivity;
import coment.github.academy_intern.ment2link.content.CalendarCursor;
import coment.github.academy_intern.ment2link.pojo.Availability;
import coment.github.academy_intern.ment2link.pojo.MentorProfile;
import coment.github.academy_intern.ment2link.pojo.Request;

import coment.github.academy_intern.ment2link.widget.EventEditView;

import comment.github.academy_intern.ment2link.R;

/**
 * Created by academy_intern on 3/8/18.
 */

public class ShowActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>
{
    private FirebaseUser user;
    private DatabaseReference myRef,mRef;
    private EventEditView.Event event_show;
    private String uid;
    public static final String EXTRA_EVENT = "extra:event";
    private static final String STATE_EVENT = "state:event";
    private static final String EXTRA_CALENDAR_ID = "extra:calendarId";
    private static final int LOADER_CALENDARS = 0;
    private static final int LOADER_SELECTED_CALENDAR = 1;
    private EventEditView.Event event;
    private EventEditView.Event.Builder builder;
    private Availability availability;
    public static final String REQUEST_ID = "id";
    private Request requestMentor;

    private  SimpleDateFormat dateFormat;
    private  SimpleDateFormat timeFormat;
    private String endDate;
    private String startDate;
    private  long hour;
    private String startT;
    private String endT;

    private EventEditView eventShowView;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialising user object to get current user logged in
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        // Path to retrieve a table from the DB
        myRef = FirebaseDatabase.getInstance().getReference().child("Availability");

        if (!checkPermissions()) {
            // simply relaunch app if permissions are revoked
            finish();
            startActivity(getPackageManager().getLaunchIntentForPackage(getPackageName())
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            return;
        }
        setContentView(R.layout.activity_show);


        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayOptions(
                ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
        //View object used to access child views
        eventShowView = (EventEditView) findViewById(R.id.event_edit_view);


        if (savedInstanceState == null) {
            event = getIntent().getParcelableExtra(EXTRA_EVENT);
            if (event == null) {
                event = EventEditView.Event.createInstance();
            }
            //noinspection ConstantConditions
            eventShowView.setEvent(event);
            Bundle args = new Bundle();
            args.putLong(EXTRA_CALENDAR_ID, event.getCalendarId());
            getSupportLoaderManager().initLoader(LOADER_SELECTED_CALENDAR, args, this);
        } else {
            event = savedInstanceState.getParcelable(STATE_EVENT);
            //noinspection ConstantConditions
            eventShowView.setEvent(event);

        }

        setTitle(event.hasId() ? R.string.edit_event : R.string.create_event);
        getSupportLoaderManager().initLoader(LOADER_CALENDARS, null, this);


        //Funtion used to access the DB and use the values stored in the DB

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Intent intent = getIntent();

                    id = intent.getStringExtra(SearchMentorActivity.AVAILABILITY_ID);

                    availability = snapshot.getValue(Availability.class);


                    if(id != null){
                        if(id.equals(availability.getUid()))
                        {

                            eventShowView.setSelectedCalendar(user.getEmail());
                            eventShowView.setTitleEvent(availability.getTitle());

                            //Initialising date format object
                            dateFormat = new SimpleDateFormat("EE, MMMM dd");
                            timeFormat = new SimpleDateFormat("hh:mm:ss a");

                            //Date values
                            endDate = dateFormat.format(Long.valueOf(availability.getDtend()));
                            startDate = dateFormat.format(Long.valueOf(availability.getDtend()));

                            //Time values
                            hour = 3600 * 1000;

                            startT = timeFormat.format(Long.valueOf(availability.getDtend()));
                            endT = timeFormat.format(Long.valueOf(availability.getDtend()) + hour);

                            // Set Date_Time values to the views
                            eventShowView.setStartDate(startDate);
                            eventShowView.setEndDate(endDate);

                            eventShowView.setStartTime(startT);
                            eventShowView.setEndTime(endT);
//                                   eventShowView.setStartDate(availability.getDstart().substring(0,10));
                            //  eventShowView.setEndDate(date);
//                                Log.e("TAG", availability.getDstart());




                            //eventShowView.setEndDate(av);
                            //  Toast.makeText(ShowActivity.this," " + availability.getDtend().substring(0,10),Toast.LENGTH_SHORT).show();

                            //EventShowView.Event event = EventShowView.Event.createInstance();


//                            eventShowView.setEventAvailable(availability);



                            // availability.setTitle(String.valueOf());

                            Log.e("TAG",availability.getTitle());



                            // Toast.makeText(ShowActivity.this," " + id,Toast.LENGTH_SHORT).show();
                            Log.e("TAG",endDate);
                            Log.e("TAG",id);
                            // Log.e("TAG",currentDateTime);

                        }

                    }else {
                        Toast.makeText(ShowActivity.this,"null occurred",Toast.LENGTH_SHORT).show();


                    }


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_delete).setVisible(eventShowView.getEvent().hasId());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (item.getItemId() == R.id.action_save) {
            if (save()) {
                finish();
            }
            return true;
        }
        if (item.getItemId() == R.id.action_delete) {
            confirmDelete();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATE_EVENT, eventShowView.getEvent());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (eventShowView != null) { // may be null if not created due to missing permissions
            eventShowView.swapCalendarSource(null);
        }
    }

    @Override
    public void onBackPressed() {
        confirmFinish();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void setTitle(int titleId) {
        if (findViewById(R.id.form_title) != null) { // exist in landscape
            ((TextView) findViewById(R.id.form_title)).setText(titleId);
        } else {
            getSupportActionBar().setDisplayOptions(getSupportActionBar().getDisplayOptions() | ActionBar.DISPLAY_SHOW_TITLE);
            super.setTitle(titleId);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = null;
        String[] selectionArgs = null;
        if (id == LOADER_SELECTED_CALENDAR) {
            selection = CalendarContract.Calendars._ID + "=?";
            selectionArgs = new String[]{String.valueOf(args.getLong(EXTRA_CALENDAR_ID))};
        }
        return new CursorLoader(this, CalendarContract.Calendars.CONTENT_URI,
                CalendarCursor.PROJECTION,
                selection, selectionArgs,
                CalendarContract.Calendars.DEFAULT_SORT_ORDER);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_CALENDARS:
                if (data != null && data.moveToFirst()) {
                    eventShowView.swapCalendarSource(new CalendarCursor(data));
                }
                break;
            case LOADER_SELECTED_CALENDAR:
                if (data != null && data.moveToFirst()) {
                    eventShowView.setSelectedCalendar(new CalendarCursor(data).getDisplayName());
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (loader.getId() == LOADER_CALENDARS) {
            eventShowView.swapCalendarSource(null);
        }
    }

    protected boolean checkPermissions() {
        return (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) |
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR)) ==
                PackageManager.PERMISSION_GRANTED;
    }

    private void confirmFinish() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.confirm_discard_changes)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ShowActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .create()
                .show();
    }

    private boolean save() {

        mRef = FirebaseDatabase.getInstance().getReference().child("Availability");


        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Availability availability;


                for (DataSnapshot snap : dataSnapshot.getChildren()) {

                    availability =  snap.getValue(Availability.class);

                    DatabaseReference refRequest = FirebaseDatabase.getInstance().getReference().child("Request");

                    Map<String, String> userData = new HashMap<String, String>();


                    userData.put("date", endDate);
                    userData.put("mentee_uid", user.getUid());
                    userData.put("mentor_uid", availability.getUid());
                    userData.put("time", endT);
                    userData.put("reason", availability.getTitle());

                    refRequest.push().setValue(userData);


                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Intent intent = new Intent(ShowActivity.this, SearchMentorActivity.class);
        startActivity(intent);

        Toast.makeText(getApplicationContext(),"Successfully sent request", Toast.LENGTH_SHORT).show();
        return true;

    }

    private boolean isValid(EventEditView.Event event) {
        if (!event.hasCalendarId()) {
            //noinspection ConstantConditions
            Snackbar.make(findViewById(R.id.coordinator_layout),
                    R.string.warning_missing_calendar,
                    Snackbar.LENGTH_SHORT)
                    .show();
            return false;
        }
        return event.hasTitle();
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.confirm_delete)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete();
                        finish();
                    }
                })
                .create()
                .show();
    }

    private void delete() {
        new ShowActivity.EventQueryHandler(this).startDelete(0, null,
                ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI,
                        eventShowView.getEvent().getId()),
                null, null);
    }

    static class EventQueryHandler extends AsyncQueryHandler {

        private final WeakReference<Context> mContext;

        public EventQueryHandler(Context context) {
            super(context.getContentResolver());
            mContext = new WeakReference<>(context);
        }

        @Override
        protected void onInsertComplete(int token, Object cookie, Uri uri) {
            if (mContext.get() != null) {

                Toast.makeText(mContext.get(), R.string.event_created, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onUpdateComplete(int token, Object cookie, int result) {
            if (mContext.get() != null) {
                Toast.makeText(mContext.get(), R.string.event_updated, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onDeleteComplete(int token, Object cookie, int result) {
            if (mContext.get() != null) {
                Toast.makeText(mContext.get(), R.string.event_deleted, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
