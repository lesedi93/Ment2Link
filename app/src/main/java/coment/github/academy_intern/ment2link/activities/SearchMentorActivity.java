package coment.github.academy_intern.ment2link.activities;

import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import coment.github.academy_intern.ment2link.EditActivity;
import coment.github.academy_intern.ment2link.MainActivity;
import coment.github.academy_intern.ment2link.ShowActivity;
import coment.github.academy_intern.ment2link.adapter.SearchMentorAdapter;
import coment.github.academy_intern.ment2link.pojo.Availability;
import coment.github.academy_intern.ment2link.pojo.MentorProfile;
import coment.github.academy_intern.ment2link.widget.AgendaAdapter;
import coment.github.academy_intern.ment2link.widget.EventEditView;
import coment.github.academy_intern.ment2link.widget.RecyclerItemClickListener;
import comment.github.academy_intern.ment2link.R;

public class SearchMentorActivity extends AppCompatActivity implements SearchMentorAdapter.SearchAdapterListener {

    //
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private SearchMentorAdapter mAdapter;
    private SearchView searchView;
    private static List<MentorProfile> listOfprofiles = new ArrayList<>();
    private MentorProfile mentorProfile;
    private List<MentorProfile> profiles;
    private List<Availability> availabilities;
    private Availability availability;
    public static final String AVAILABILITY_ID = "id";
    private Intent intent;
    private String time,startDate,startT,date;
    long hour;
    private SimpleDateFormat dateFormat,timeFormat;
    DatabaseReference refAvailability;
    static int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_mentor);

        //private ValueEventListener valueEventListener;
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users");
        refAvailability = FirebaseDatabase.getInstance().getReference().child("Availability");


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        recyclerView = findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                profiles = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    mentorProfile = snapshot.getValue(MentorProfile.class);

                    if ("Mentor".equalsIgnoreCase(mentorProfile.getRole())) {
                        profiles.add(mentorProfile);
                    }

                    listOfprofiles = profiles;
                    Log.i("Ygritte", snapshot.toString());
                }


                getContent();


                recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        index = position;
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                }));


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public String getAvailabilityTime(final String mentorUid)
    {
        final String time = null;

        DatabaseReference avaDb = FirebaseDatabase.getInstance().getReference().child("Bookings");

        avaDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Availability availability;

                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    availability = snapshot.getValue(Availability.class);

                    if(availability.getUid().equalsIgnoreCase(mentorUid))
                    {
                        //time = availability.getDtstart();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return time;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                if (query.isEmpty()) {
                    getContent();
                } else {
                    mAdapter.getFilter().filter(query);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                if (query.isEmpty()) {
                    getContent();
                } else {
                    mAdapter.getFilter().filter(query);
                }

                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }


    //query database and call this method
    public void editEvent(Context context, AgendaAdapter.EventItem eventItem) {
        EventEditView.Event.Builder eventBuilder = new EventEditView.Event.Builder()
                .start(eventItem.mStartTimeMillis)
                .end(eventItem.mEndTimeMillis)
                .allDay(eventItem.mIsAllDay);
        if (!(eventItem instanceof AgendaAdapter.NoEventItem)) {
            eventBuilder.id(eventItem.mId)
                    .calendarId(eventItem.mCalendarId)
                    .title(eventItem.mTitle);
        }
        context.startActivity(new Intent(context, EditActivity.class)
                .putExtra(EditActivity.EXTRA_EVENT, eventBuilder.build()));
    }


    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        } else {
            getContent();

        }
        super.onBackPressed();
    }
    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }


    public void getContent() {
        mAdapter = new SearchMentorAdapter(getApplicationContext(), listOfprofiles,this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onSearchSelected(MentorProfile prof) {


        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.mentordetails, null);
        final ImageView image = alertLayout.findViewById(R.id.req_photo);
        final TextView tvName = alertLayout.findViewById(R.id.req_men_name);
        final TextView tvReason = alertLayout.findViewById(R.id.person_reason);
        final TextView tvLocation = alertLayout.findViewById(R.id.location);
        final TextView tvTime = alertLayout.findViewById(R.id.time);
        final TextView tvDate = alertLayout.findViewById(R.id.calender);
        final TextView tvBIO = alertLayout.findViewById(R.id.bois);
        final TextView tvEmail = alertLayout.findViewById(R.id.email1);

        if (prof == null) {

            final MentorProfile mentor = listOfprofiles.get(index);
            final String userID = prof.getUid();

            AlertDialog.Builder alert = new AlertDialog.Builder(SearchMentorActivity.this);
            // this is set the view from XML inside AlertDialog
            alert.setView(alertLayout);
            // disallow cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(false);

            refAvailability.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    availabilities = new ArrayList<>();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        availability = snapshot.getValue(Availability.class);

                        //Initialising date format object
                        dateFormat = new SimpleDateFormat("EE, MMMM dd");
                        timeFormat = new SimpleDateFormat("hh:mm:ss a");

                        //Date values
                        startDate = dateFormat.format(Long.valueOf(availability.getDtend()));

                        time = timeFormat.format(Long.valueOf(availability.getDtend()));

                        Toast.makeText(getApplicationContext(), mentor.getBio(), Toast.LENGTH_LONG).show();

                        tvName.setText(mentor.getName() + " " + mentor.getSurname());
                        tvReason.setText(mentor.getField_of_study());

                        String uriPic = mentor.getImageUrl();
                        Picasso.with(SearchMentorActivity.this).load(uriPic).placeholder(R.drawable.user).error(R.drawable.user).fit().centerCrop().into(image);


                        if (availability.getUid().equalsIgnoreCase(userID)) {

                            tvTime.setText(time);
                            tvDate.setText(startDate);
                            tvLocation.setText(mentor.getLocation());



                            availabilities.add(availability);
                            break;

                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                    Toast.makeText(getApplicationContext(), mentorProfile.getUid(), Toast.LENGTH_LONG).show();
                }
            });


            alert.setPositiveButton("Book", new DialogInterface.OnClickListener() {

                //@Override
                public void onClick(DialogInterface dialog, int which) {
                    intent = new Intent(getApplicationContext(), ShowActivity.class);
                    intent.putExtra(AVAILABILITY_ID, mentor.getUid());
                    startActivity(intent);





            }});
            alert.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

        } else {

            final MentorProfile mentor = prof;
            final String userID = prof.getUid();

            AlertDialog.Builder alert = new AlertDialog.Builder(SearchMentorActivity.this);
            // this is set the view from XML inside AlertDialog
            alert.setView(alertLayout);
            // disallow cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(false);

            refAvailability.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    availabilities = new ArrayList<>();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        availability = snapshot.getValue(Availability.class);

                        dateFormat = new SimpleDateFormat("EE, MMMM dd");
                        timeFormat = new SimpleDateFormat("hh:mm:ss a");

                        //Date values
                        startDate = dateFormat.format(Long.valueOf(availability.getDtend()));

                        time = timeFormat.format(Long.valueOf(availability.getDtend()));

                        tvName.setText(mentor.getName() + " " + mentor.getSurname());
                        tvBIO.setText(mentor.getBio());
                        tvReason.setText(mentor.getField_of_study());
                        String uriPic = mentor.getImageUrl();
                        Picasso.with(SearchMentorActivity.this).load(uriPic).fit().centerCrop().into(image);

                        if (availability.getUid().equalsIgnoreCase(userID)) {

                            tvTime.setText(time);
                            tvDate.setText(startDate);
                            tvLocation.setText(mentor.getLocation());

                            availabilities.add(availability);
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                    Toast.makeText(getApplicationContext(), mentorProfile.getUid(), Toast.LENGTH_LONG).show();
                }
            });

            alert.setPositiveButton("Book", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    intent = new Intent(getApplicationContext(), ShowActivity.class);
                    intent.putExtra(AVAILABILITY_ID, mentor.getUid());
                    startActivity(intent);
            }});
            alert.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });


            AlertDialog dialog = alert.create();
            dialog.show();
        }

    }
}