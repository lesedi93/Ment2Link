package coment.github.academy_intern.ment2link.fragment;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import coment.github.academy_intern.ment2link.adapter.RecomendedUpcomingAdapter;
import coment.github.academy_intern.ment2link.pojo.Bookings;
import coment.github.academy_intern.ment2link.pojo.MentorProfile;
import coment.github.academy_intern.ment2link.pojo.UpcomingAppointment;
import coment.github.academy_intern.ment2link.widget.RecyclerItemClickListener;
import comment.github.academy_intern.ment2link.R;

import com.uber.sdk.android.core.UberSdk;
import com.uber.sdk.core.auth.Scope;
import com.uber.sdk.rides.client.SessionConfiguration;
import com.uber.sdk.android.rides.RideRequestButton;


/**
 * Created by team_leader on 2018/03/08.
 */

public class AppointmentFragment extends Fragment {
    View view;


    private FirebaseAuth mAuth;
    FirebaseDatabase mRef;
    RecyclerView recyclerView;
    ArrayList<Bookings> bookings;
    private Bookings booking;
    RecomendedUpcomingAdapter adapter;
    FirebaseUser user;
    private String uid;

    public AppointmentFragment() {

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.appointment_layout,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle("Appointment");


        mAuth = FirebaseAuth.getInstance();

        final DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Bookings");
        final DatabaseReference dbUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        //Uber funtions
        SessionConfiguration config = new SessionConfiguration.Builder()
                // mandatory
                .setClientId("48bInFtONkEPQutdGMiCGD3NdOkmYXxB")
                // required for enhanced button features
                .setServerToken("NJ-ZIyKn_Ens3FeY0wueUP355f7T3i1Nsg2jML_0")
                // required for implicit grant authentication
                //.setRedirectUri("<REDIRECT_URI>")
                // optional: set sandbox as operating environment
                .setEnvironment(SessionConfiguration.Environment.SANDBOX)
                .build();

        UberSdk.initialize(config);


        recyclerView = view.findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        bookings=new ArrayList<>();

        db.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {


                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    booking = snapshot.getValue(Bookings.class);
                    user= FirebaseAuth.getInstance().getCurrentUser();
                    uid=user.getUid();

                    if(uid.equalsIgnoreCase(booking.getBooking_mentee_uid()))
                    {
                        bookings.add(booking);
                    }

                    Log.i("Ygritte","MyClass.getView() — get item number "+ bookings.toString()) ;
                }

                adapter = new RecomendedUpcomingAdapter(getActivity(),bookings);
                recyclerView.setAdapter(adapter);

                recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        final String userID = booking.getBooking_mentee_uid();
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        View alertLayout = inflater.inflate(R.layout.upcoming_dialog, null);
                        final TextView tvSession = alertLayout.findViewById(R.id.details);
                        final TextView tvDetails = alertLayout.findViewById(R.id.bois);
                        final TextView tvDate = alertLayout.findViewById(R.id.calender);
                        final TextView location = alertLayout.findViewById(R.id.location);

                        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                        alert.setTitle("                  Session Details");
                        // this is set the view from XML inside AlertDialog
                        alert.setView(alertLayout);
                        // disallow cancel of AlertDialog on click of back button and outside touch
                        alert.setCancelable(false);

                        db.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                                {
                                    booking = snapshot.getValue(Bookings.class);
                                    user= FirebaseAuth.getInstance().getCurrentUser();
                                    uid = user.getUid();

                                    if(uid.equalsIgnoreCase(userID))
                                    {
                                        bookings.add(booking);

                                    }
                                    tvDate.setText(booking.getDate() +", "+ booking.getTime());
                                    tvSession.setText(booking.getSession_details());
                                    tvDetails.setText(booking.getDetails());


                                    Log.i("Ygritte","MyClass.getView() — get item number "+ bookings.toString()) ;
                                }

                            }




                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        dbUsers.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                                {
                                    MentorProfile mentors = snapshot.getValue(MentorProfile.class);
                                    location.setText(mentors.getLocation());

                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });


                        AlertDialog dialog = alert.create();
                        dialog.show();
                        dialog.getWindow().setLayout(1300, 1600);
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



}

