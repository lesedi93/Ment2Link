package coment.github.academy_intern.ment2link.activities;

/**
 * Created by team_leader on 2018/03/19.
 */
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.xml.sax.DTDHandler;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.KeyStoreBuilderParameters;

import coment.github.academy_intern.ment2link.ShowActivity;
import coment.github.academy_intern.ment2link.adapter.BlockedMenteeAdapter;
import coment.github.academy_intern.ment2link.pojo.Blocked;
import coment.github.academy_intern.ment2link.pojo.MentorProfile;
import coment.github.academy_intern.ment2link.pojo.Request;
import coment.github.academy_intern.ment2link.widget.RecyclerItemClickListener;
import comment.github.academy_intern.ment2link.R;


public class BlockedMentee extends AppCompatActivity {

    private RecyclerView recyclerView;
    ArrayList<Blocked> blocked;
    List<MentorProfile> profiles;
    MentorProfile profile;
    List<Blocked> blocks;
    Blocked blockedMentees;
    BlockedMenteeAdapter adapter;
    Blocked currentUser;
    int index;
    private FirebaseUser user;
    private String blocked_mentees;
    static String kEYB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked_mentee);
        recyclerView = findViewById(R.id.recyclerviews);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Blocked List");

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Blocked");



        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                blocked = new ArrayList<>();
                blockedMentees = new Blocked();
//                profile=new MentorProfile();
//                profiles=new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    blockedMentees = snapshot.getValue(Blocked.class);

                    blocked.add(blockedMentees);
                    Log.d("Muzi", blockedMentees.getBlocked_mentee_uid());

                }


                adapter = new BlockedMenteeAdapter(getApplicationContext(), blocked,profiles);
                recyclerView.setAdapter(adapter);

                recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                //final MentorProfile mentor = profiles.get(position);
                                index = position;

                                LayoutInflater inflater = getLayoutInflater();
                                View alertLayout = inflater.inflate(R.layout.blockeddialog,null);
                                final ImageView image = alertLayout.findViewById(R.id.req_photo);
                                final TextView tvName = alertLayout.findViewById(R.id.req_men_name);
                                final TextView tvReason = alertLayout.findViewById(R.id.person_reason);
                                final TextView tvLocation = alertLayout.findViewById(R.id.location);
                                final TextView tvEmail = alertLayout.findViewById(R.id.email1);

                                AlertDialog.Builder alert = new AlertDialog.Builder(BlockedMentee.this);
                                alert.setView(alertLayout);
                                // disallow cancel of AlertDialog on click of back button and outside touch
                                alert.setCancelable(false);
                                final Blocked blockers = blocked.get(position);

                                final DatabaseReference database1 = FirebaseDatabase.getInstance().getReference("Users");

                                database1.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {



                                            profile=dataSnapshot1.getValue(MentorProfile.class);
                                            String blockedUserId=profile.getUid();
                                            profiles=new ArrayList<>();

                                            if(blockedUserId.equalsIgnoreCase(blockers.getBlocked_mentee_uid()))
                                            {

                                                kEYB =dataSnapshot1.getKey();
                                                //Toast.makeText(getApplicationContext()," key "+ key,Toast.LENGTH_LONG).show();

                                                tvEmail.setText(profile.getEmail());
                                                tvName.setText(profile.getName() + " "+ profile.getSurname());
                                                tvReason.setText(profile.getField_of_study());

                                                String urlPic = profile.getImageUrl();
                                                Picasso.with(getApplicationContext())
                                                        .load(urlPic)
                                                        .placeholder(R.drawable.user)   // optional
                                                        .error(R.drawable.user)      // optional
                                                        .rotate(360)                             // optional
                                                        .into(image);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                alert.setPositiveButton("Unblock", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // adapter.onChildRemoved();
                                        removeRequest();
                                    }
                                });
                                alert.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                AlertDialog dialog = alert.create();
                                dialog.show();

                            }

                            @Override
                            public void onItemLongClick(View view, int position) {
                            }
                        })
                );
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
        blocked_mentees = user.getUid();

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

    public void removeRequest() {


        final DatabaseReference db3=FirebaseDatabase.getInstance().getReference("Blocked");
        final Blocked blocked1=new Blocked();

        db3.orderByChild("blocked_mentee_uid").equalTo(blocked1.getBlocked_mentee_uid()).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                
//                String key =dataSnapshot.getKey();
//                Toast.makeText(getApplicationContext()," key "+ key,Toast.LENGTH_LONG).show();

                dataSnapshot.getRef().child(kEYB).removeValue();
                //db3.child("Blocked").child(dataSnapshot.getKey()).removeValue();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}