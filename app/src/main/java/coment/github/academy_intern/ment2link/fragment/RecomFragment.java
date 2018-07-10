package coment.github.academy_intern.ment2link.fragment;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import coment.github.academy_intern.ment2link.ShowActivity;
import coment.github.academy_intern.ment2link.adapter.RecomendedMentorAdapter;
import coment.github.academy_intern.ment2link.pojo.Availability;
import coment.github.academy_intern.ment2link.pojo.MentorProfile;
import coment.github.academy_intern.ment2link.widget.RecyclerItemClickListener;
import comment.github.academy_intern.ment2link.R;

/**
 * Created by team_leader on 2018/03/08.
 */

public class RecomFragment extends Fragment {


    private RecyclerView recyclerView;
    ArrayList<MentorProfile> profiles;
    private MentorProfile mentorProfile;
    private MentorProfile menteeProfile;
    RecomendedMentorAdapter adapter;
    private FirebaseUser user;
    DatabaseReference avaDb;
    public static final String AVAILABILITY_ID = "id";
    private Availability availability;
    private List<Availability> availabilities;
    private String uid;
    String[] arrays = new String[]{ "Technical Applications", "Software Development", "Talent Specialist"};
    private String time,startDate,startT,date;
    private SimpleDateFormat dateFormat,timeFormat;



    public RecomFragment()
    {

    }

    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.recommended_layout,container,false);

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users");
        avaDb=FirebaseDatabase.getInstance().getReference().child("Availability");

        recyclerView = view.findViewById(R.id.recyclerviews);

        getActivity().setTitle("Recommended");

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        populateMentee();

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                profiles= new ArrayList<>();
                user= FirebaseAuth.getInstance().getCurrentUser();
                uid=user.getUid();



                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    mentorProfile = snapshot.getValue(MentorProfile.class);

                    if ("Mentor".equalsIgnoreCase(mentorProfile.getRole())  && menteeProfile.getField_of_study().equalsIgnoreCase(mentorProfile.getField_of_study())) {

                        profiles.add(mentorProfile);

                    }
                }

                adapter = new RecomendedMentorAdapter(getActivity(),profiles);
                recyclerView.setAdapter(adapter);

                recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        // do whatever
                        final MentorProfile mentor = profiles.get(position);
                        final String userID = mentor.getUid();

                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        View alertLayout = inflater.inflate(R.layout.mentordetails, null);
                        final ImageView image=alertLayout.findViewById(R.id.req_photo);
                        final TextView tvBIO = alertLayout.findViewById(R.id.bois);
                        final TextView tvName = alertLayout.findViewById(R.id.req_men_name);
                        final TextView tvReason = alertLayout.findViewById(R.id.person_reason);
                        final TextView tvLocation = alertLayout.findViewById(R.id.location);
                        final TextView tvTime = alertLayout.findViewById(R.id.time);
                        final TextView tvDate = alertLayout.findViewById(R.id.calender);
                        //final Spinner spinner=(Spinner)alertLayout.findViewById(R.id.spinner);

                        // Creating adapter for spinner

                        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                        // this is set the view from XML inside AlertDialog
                        alert.setView(alertLayout);
                        // disallow cancel of AlertDialog on click of back button and outside touch
                        alert.setCancelable(false);


                        avaDb.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                availabilities= new ArrayList<>();

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                    availability = snapshot.getValue(Availability.class);
                                    dateFormat = new SimpleDateFormat("EE, MMMM dd");
                                    timeFormat = new SimpleDateFormat("hh:mm:ss a");

                                    //Date values
                                    startDate = dateFormat.format(Long.valueOf(availability.getDtend()));

                                    time = timeFormat.format(Long.valueOf(availability.getDtend()));

                                    tvName.setText(mentor.getName() +" "+ mentor.getSurname());
                                    tvBIO.setText(mentor.getBio());
                                    tvReason.setText(mentor.getField_of_study());
                                    String uriPic = mentor.getImageUrl();
                                    Picasso.with(getActivity()).load(uriPic).placeholder(R.drawable.user).error(R.drawable.user).fit().centerCrop().into(image);
//                                    ArrayAdapter<String> adapter= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.Resources));
//                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                                    spinner.setAdapter(adapter);
//
                                    if(availability.getUid().equalsIgnoreCase(userID))
                                    {
                                        tvTime.setText(time);
                                        tvDate.setText(startDate);

                                        tvLocation.setText(mentor.getLocation());
                                        tvLocation.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + tvLocation.getText().toString());
                                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                                mapIntent.setPackage("com.google.android.apps.maps");
                                                startActivity(mapIntent);
                                            }
                                        });
                                        availabilities.add(availability);
                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                                Toast.makeText(getActivity(),mentorProfile.getUid(),Toast.LENGTH_LONG).show();
                            }
                        });


                        alert.setPositiveButton("Request Meet-up", new DialogInterface.OnClickListener() {

                            //@Override
                            public void onClick(DialogInterface dialog, int which) {

                                Toast.makeText(getActivity().getBaseContext(),mentor.getName() + " made a booking ",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getActivity(), ShowActivity.class);
                                intent.putExtra(AVAILABILITY_ID, mentor.getUid());
                                startActivity(intent);

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
                }));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }


    private void populateMentee()
    {
        DatabaseReference db1 = FirebaseDatabase.getInstance().getReference().child("Users");

        db1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                MentorProfile mentee1;
                FirebaseUser user1 =  FirebaseAuth.getInstance().getCurrentUser();
                String uid1 = user1.getUid();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    mentee1 = snapshot.getValue(MentorProfile.class);

                    if(mentee1.getUid().equalsIgnoreCase(uid1))
                    {
                        menteeProfile = new MentorProfile(mentee1.getUid(),mentee1.getImageUrl(),mentee1.getName(),mentee1.getSurname(),mentee1.getEmail(),mentee1.getBio(),mentee1.getLocation(),mentee1.getField_of_study());
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
