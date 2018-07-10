package coment.github.academy_intern.ment2link.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import comment.github.academy_intern.ment2link.R;

/**
 * Created by team_leader on 2018/02/26.
 */

public class ForgetPasswordActivity extends Fragment {

    View view;
    private EditText inputEmail;
    private Button btnReset, btnBack;
    private FirebaseAuth auth;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.rest_password_layout, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        inputEmail = view.findViewById(R.id.email);
        btnReset = view.findViewById(R.id.btn_reset_password);
        btnBack = view.findViewById(R.id.btn_back);
        progressBar = view.findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();

//
//        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Users").push();
//
//
//        Map<String, String> userData = new HashMap<String, String>();
//
//        userData.put("uid", "UeP6laPIUIVLcaGb4fHQYK5AU4V2");
//        userData.put("name", "samson");
//        userData.put("surname", "Zungu");
//        userData.put("e_mail", "samson@thedigitalacademy.co.za");
//        userData.put("role", "Mentor");
//        userData.put("bio", "Software Developer");
//        userData.put("location", "Johannesburg");
//        userData.put("imageUrl", " ");
//        userData.put("field_of_study", "Technical Applications");
//
//
//        database.setValue(userData);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFragmentManager().beginTransaction().replace(R.id.container, new Login()).commit();

            }
        });


        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getActivity(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                }

                                progressBar.setVisibility(View.GONE);
                            }
                        });
            }
        });

    }
}
