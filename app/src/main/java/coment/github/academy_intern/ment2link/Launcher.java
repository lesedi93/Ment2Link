package coment.github.academy_intern.ment2link;

import android.app.Activity;
import android.os.Bundle;

import coment.github.academy_intern.ment2link.fragment.Splash;
import comment.github.academy_intern.ment2link.R;

public class Launcher extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        //attach splash screen fragment on the activite
        getFragmentManager().beginTransaction().add(R.id.container, new Splash()).commit();
    }
}
