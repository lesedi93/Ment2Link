package coment.github.academy_intern.ment2link.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import coment.github.academy_intern.ment2link.menteedrawer.MenteeTabMenu;
import coment.github.academy_intern.ment2link.mentordrawer.MentorNavigationDrawer;
import coment.github.academy_intern.ment2link.pojo.MentorProfile;
import comment.github.academy_intern.ment2link.R;

/**
 * Created by team_leader on 2018/02/26.
 */


public class LoginControl extends Fragment {

    View view;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users");
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_control_layout, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


//        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Request").push();
//
//
//        Map<String, String> userData = new HashMap<String, String>();
//
//        userData.put("mentor_uid", "UeP6laPIUIVLcaGb4fHQYK5AU4V2");
//        userData.put("date", "07/03/2018");
//        userData.put("mentee_uid", "sUpPZrBZ5RQg0qIOSMFCeVsm7cR2");
//        userData.put("reason", "achivements");
//        userData.put("time", "12:00");
//
//
//
//        database.setValue(userData);


        mAuth = FirebaseAuth.getInstance();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                MentorProfile profile;

                for (DataSnapshot snap : dataSnapshot.getChildren()) {

                    profile =  snap.getValue(MentorProfile.class);

                    String profileId = profile.getUid();
                    String curProfileId = mAuth.getCurrentUser().getUid();

                    if(profileId!= null)
                    {
                       if (profileId.equals(curProfileId)) {


                        if (profile.getRole().equalsIgnoreCase("Mentor")) {

                            startActivity(new Intent(getActivity(), MentorNavigationDrawer.class));
                            getActivity().finish();

                        } else if (profile.getRole().equalsIgnoreCase("Mentee")) {


                            startActivity(new Intent(getActivity(), MenteeTabMenu.class));
                            getActivity().finish();
                        }
                    }
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
