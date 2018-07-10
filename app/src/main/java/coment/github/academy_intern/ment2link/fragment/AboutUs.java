package coment.github.academy_intern.ment2link.fragment;

/**
 * Created by team_leader on 2018/03/08.
 */

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

        import android.app.Fragment;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import coment.github.academy_intern.ment2link.pojo.About;
        import coment.github.academy_intern.ment2link.pojo.MentorProfile;
        import comment.github.academy_intern.ment2link.R;

/**
 * Created by academy_intern on 2018/03/08.
 */

public class AboutUs extends Fragment {

    View view;
    TextView txtAboutUs;
    About about;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.about_us, container, false);
        return view;


    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle("About");

        txtAboutUs = view.findViewById(R.id.txtAboutUsText);

        about=new About();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("About");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    about=dataSnapshot1.getValue(About.class);
                    txtAboutUs.setText(about.getAbout_info());
                    txtAboutUs.setMovementMethod(new ScrollingMovementMethod());
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}