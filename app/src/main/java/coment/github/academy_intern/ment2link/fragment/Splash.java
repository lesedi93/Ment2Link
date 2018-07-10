package coment.github.academy_intern.ment2link.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import comment.github.academy_intern.ment2link.R;


/**
 * Created by team_leader on 2018/02/26.
 */

public class Splash extends Fragment {

    private final int SPLASH_DISPLAY_LENGTH = 3750;
    View view;
    // DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Bookings");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.splash_layout, container, false);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        //handler for running splash activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

              //  DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Blocked");

               // database.removeValue();


//                DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Blocked");
//
//                Map<String, String> userData = new HashMap<String, String>();
//
//                userData.put("blocked_mentee_uid", "");
//                userData.put("blocked_mentor_uid", "");
//
//                database.child("").setValue(userData);

                //start login screen after splash timer ends
                getFragmentManager().beginTransaction().replace(R.id.container, new Login()).commit();

            }
        }, SPLASH_DISPLAY_LENGTH);

    }
}
