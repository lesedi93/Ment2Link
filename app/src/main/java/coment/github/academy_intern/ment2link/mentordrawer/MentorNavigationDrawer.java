package coment.github.academy_intern.ment2link.mentordrawer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import coment.github.academy_intern.ment2link.SignOut;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import coment.github.academy_intern.ment2link.MainActivity;
import coment.github.academy_intern.ment2link.activities.BlockedMentee;
import coment.github.academy_intern.ment2link.activities.ViewProfileActivity;
import coment.github.academy_intern.ment2link.adapter.RequestAdapter;
import coment.github.academy_intern.ment2link.fragment.AboutUs;
import coment.github.academy_intern.ment2link.pojo.Bookings;
import coment.github.academy_intern.ment2link.pojo.MentorProfile;
import coment.github.academy_intern.ment2link.pojo.Request;
import coment.github.academy_intern.ment2link.widget.RecyclerItemClickListener;
import comment.github.academy_intern.ment2link.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class MentorNavigationDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "tag";
    private DatabaseReference rootRef, requestRef;
    private TextView title_email, title_name;
    private RecyclerView recyclerView;
    private RequestAdapter mAdapter;
    private ArrayList<MentorProfile> profiles;
    private ArrayList<Request> mentors;
    private MentorProfile mentorProfile1;
    private Request requestMentor;
    private String uid;
    Bookings bookings;
    private FirebaseUser user, user1;
    private DatabaseReference myUserRef, myRequestRef, myNavRef;
    private DatabaseReference bookingRef;
    private  static int index;
    private static Request mentor1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Request");


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.getHeaderView(0);

        final CircleImageView drawer_image = hView.findViewById(R.id.drawer_profile_pic);
        final TextView drawer_name = hView.findViewById(R.id.title_name1);
        final TextView drawer_email = hView.findViewById(R.id.title_email1);


        user1 = FirebaseAuth.getInstance().getCurrentUser();
        myNavRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user1.getUid());


        myNavRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MentorProfile value = dataSnapshot.getValue(MentorProfile.class);
                drawer_name.setText(value.getName() + " " + value.getSurname());
                drawer_email.setText(value.getEmail());
                Glide.with(getApplicationContext()).load(value.getImageUrl()).into(drawer_image);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //pull data
        myUserRef = FirebaseDatabase.getInstance().getReference();

        myRequestRef = FirebaseDatabase.getInstance().getReference();


        recyclerView = findViewById(R.id.rv);

        mentors = new ArrayList<>();
        profiles = new ArrayList<>();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        myRequestRef.child("Request").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    requestMentor = snapshot.getValue(Request.class);
                    //  mentorProfile = snapshot.getValue(MentorProfile.class);

                    uid = user.getUid();

                    if(uid.equalsIgnoreCase(requestMentor.getMentor_uid()))
                    {
                        mentors.add(requestMentor);
                    }

                    mAdapter = new RequestAdapter(getApplicationContext(), mentors);
                    recyclerView.setAdapter(mAdapter);


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view,int position) {
                //handle click events here
                final Request mentor = mentors.get(position);
                mentor1 = mentors.get(position);
                index = position;

                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.mentor_dialog, null);
                final CircleImageView img = alertLayout.findViewById(R.id.req_photo);
                final TextView tvName = alertLayout.findViewById(R.id.req_men_name);
                final TextView tvReason = alertLayout.findViewById(R.id.person_reason);
                final TextView tvLocation = alertLayout.findViewById(R.id.location);
                final TextView tvTime = alertLayout.findViewById(R.id.time);
                final TextView tvDate = alertLayout.findViewById(R.id.calender);

                final ImageView img_close = alertLayout.findViewById(R.id.img_close);
                // Intent intent = new Intent(MentorsActivity.this, MentorProfileActivity.class);
                // intent.putExtra("mentor",mentor);
                // startActivity(intent);

                final AlertDialog.Builder alert = new AlertDialog.Builder(MentorNavigationDrawer.this);
                //alert.setTitle("Request");
                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);
                // disallow cancel of AlertDialog on click of back button and outside touch
                alert.setCancelable(true);

                myUserRef.child("Users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            mentorProfile1 = snapshot.getValue(MentorProfile.class);

                            Request requestMentor = mentors.get(index);


                            String profileId = mentorProfile1.getUid();
                            String requestProfileId = requestMentor.getMentee_uid();


                            if (profileId != null) {


                                // Log.i("Mentee", mentorProfile1.getName());
                                if (profileId.equals(requestProfileId)) {
                                    Log.i("Mentee", mentorProfile1.getName());
                                    //.with(MentorNavigationDrawer.this).load(mentor1.).fit().centerCrop().into(imProfilePic);
                                    Picasso.with(MentorNavigationDrawer.this).load(mentorProfile1.getImageUrl()).fit().centerCrop().rotate(360).into(img);

                                    tvName.setText(mentorProfile1.getName() + " " + mentorProfile1.getSurname());
                                    tvReason.setText(mentor.getReason());
                                    tvDate.setText(mentor.getDate());
                                    tvLocation.setText(mentorProfile1.getLocation());
                                    tvTime.setText(mentor.getTime());
                                    break;

                                }


                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                alert.setPositiveButton("Accept", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //create a booking
                        createBooking(index);

                        Toast.makeText(getApplicationContext(),"index " + index,Toast.LENGTH_SHORT).show();


                    }
                });
                alert.setNeutralButton("Block", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getBaseContext(), mentorProfile1.getName() + " is blocked", Toast.LENGTH_LONG).show();


                        blockMentee(index);
                    }
                });


                alert.setNegativeButton("Decline", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getBaseContext(), mentorProfile1.getName() + "is declined", Toast.LENGTH_SHORT).show();

                        removeRequest();
                    }
                });

                final AlertDialog dialog = alert.create();

                img_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }


            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));


    }


    public void blockMentee(int selectedIndex) {

        int i = selectedIndex;

        Request requestBlock;

        requestBlock = mentors.get(i);


        String blocked_mentee_uid = (String) requestBlock.getMentee_uid();
        String bloking_mentor_uid = (String) requestBlock.getMentor_uid();

        DatabaseReference db2 = FirebaseDatabase.getInstance().getReference().child("Blocked");


        Map<String, String> userData = new HashMap<String, String>();

        userData.put("blocked_mentee_uid", blocked_mentee_uid);
        userData.put("bloking_mentor_uid", bloking_mentor_uid);


        db2.push().setValue(userData);


         removeRequest();
    }

    public void createBooking(int selectedIndex) {


        Request book;

        int i = selectedIndex;

        book = mentors.get(i);

        String date = book.getDate().toString();
        String booking_mentee_uid = (String) book.getMentee_uid();
        String booking_mentor_uid = (String) book.getMentor_uid();
        String session_details = (String) book.getReason();
        String time =  book.getTime();

        DatabaseReference db2 = FirebaseDatabase.getInstance().getReference().child("Bookings");


        Map<String, String> userData = new HashMap<String, String>();

        userData.put("date", date);
        userData.put("booking_mentee_uid", booking_mentee_uid);
        userData.put("booking_mentor_uid", booking_mentor_uid);
        userData.put("session_details", session_details);
        userData.put("time", time);

        db2.push().setValue(userData);

         removeRequest();


    }


    public void removeRequest() {


        DatabaseReference db3 = FirebaseDatabase.getInstance().getReference();


        Toast.makeText(getApplicationContext(),mentor1.getMentee_uid(),Toast.LENGTH_LONG).show();


        db3.child("Request").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {

                    Request menteeReq = snapshot1.getValue(Request.class);

                    String menteeReqUid = menteeReq.getMentee_uid();

                    if(menteeReqUid != null) {


                        if (menteeReqUid.equals(mentor1.getMentee_uid())) {

                            snapshot1.getRef().child(menteeReq.getMentor_uid()).removeValue();

                            mentors.clear();

                            mAdapter.updateData(mentors);

                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }


    @Override
    protected void onStart() {
        super.onStart();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile_mentor) {
            startActivity(new Intent(MentorNavigationDrawer.this, ViewProfileActivity.class));
        } else if (id == R.id.nav_blocked) {

            Intent intent = new Intent(MentorNavigationDrawer.this, BlockedMentee.class);
            startActivity(intent);
        } else if (id == R.id.nav_about_mentor) {

            getFragmentManager().beginTransaction().add(R.id.container, new AboutUs()).commit();

        } else if (id == R.id.nav_siginout_mentor) {

            Intent intent = new Intent(MentorNavigationDrawer.this, SignOut.class);
            startActivity(intent);
        }
         else if (id == R.id.nav_home)
        {
            Intent intent = new Intent(MentorNavigationDrawer.this, MentorNavigationDrawer.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
