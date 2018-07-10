package coment.github.academy_intern.ment2link.activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import coment.github.academy_intern.ment2link.adapter.RecomendedMentorAdapter;
import coment.github.academy_intern.ment2link.pojo.MentorProfile;
import comment.github.academy_intern.ment2link.R;

public class RecommendedMentors extends AppCompatActivity {


    private RecyclerView recyclerView;
    ArrayList<MentorProfile> profiles;
    private MentorProfile mentorProfile;
    RecomendedMentorAdapter adapter;
    MentorProfile currentUser;
    private FirebaseUser user;
    private String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended_mentors);

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users");

        recyclerView = findViewById(R.id.recyclerviews);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Recommended Mentors");

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        DatabaseReference db1 = FirebaseDatabase.getInstance().getReference().child("Users");

        db1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    mentorProfile = snapshot.getValue(MentorProfile.class);

                    if (mentorProfile.getUid().equalsIgnoreCase(uid)) {

                        currentUser = new MentorProfile(mentorProfile.getUid(), mentorProfile.getImageUrl(), mentorProfile.getName(), mentorProfile.getSurname(), mentorProfile.getEmail(), mentorProfile.getBio(), mentorProfile.getLocation(), mentorProfile.getField_of_study());

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                profiles = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    mentorProfile = snapshot.getValue(MentorProfile.class);

                    if ("Mentor".equalsIgnoreCase(mentorProfile.getRole())) {

                       if(mentorProfile.getField_of_study().equalsIgnoreCase(currentUser.getField_of_study()))
                       {
                           profiles.add(mentorProfile);
                       }
                    }
                }

                adapter = new RecomendedMentorAdapter(getApplicationContext(), profiles);
                recyclerView.setAdapter(adapter);
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
        uid = user.getUid();



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }


    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }
}
