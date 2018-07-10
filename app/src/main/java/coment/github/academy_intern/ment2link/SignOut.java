package coment.github.academy_intern.ment2link;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import coment.github.academy_intern.ment2link.fragment.Login;
import comment.github.academy_intern.ment2link.R;

public class SignOut extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 3050;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_out);

        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();


        //handler for running splash activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


               // mAuth.signOut();
                //finish();
                System.exit(0);
                onDestroy();

            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
