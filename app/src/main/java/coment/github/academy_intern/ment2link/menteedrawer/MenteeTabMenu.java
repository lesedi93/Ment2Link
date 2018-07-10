package coment.github.academy_intern.ment2link.menteedrawer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import coment.github.academy_intern.ment2link.SignOut;

import coment.github.academy_intern.ment2link.MainActivity;
import coment.github.academy_intern.ment2link.activities.SearchMentorActivity;
import coment.github.academy_intern.ment2link.activities.ViewProfileActivity;

import coment.github.academy_intern.ment2link.fragment.AboutUs;
import coment.github.academy_intern.ment2link.fragment.AppointmentFragment;
import coment.github.academy_intern.ment2link.fragment.RecomFragment;
import comment.github.academy_intern.ment2link.R;

public class MenteeTabMenu extends AppCompatActivity {

    private static final String SELECTED_ITEM = "arg_selected_item";
    private BottomNavigationView mBottomNav;
    private int mSelectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentee_tab_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getFragmentManager().beginTransaction().add(R.id.container, new AppointmentFragment()).commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SearchMentorActivity.class));
            }
        });


        mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectFragment(item);
                return true;
            }
        });

        MenuItem selectedItem;
        if (savedInstanceState != null) {
            mSelectedItem = savedInstanceState.getInt(SELECTED_ITEM, 0);
            selectedItem = mBottomNav.getMenu().findItem(mSelectedItem);
        } else {
            selectedItem = mBottomNav.getMenu().getItem(0);
        }
        selectFragment(selectedItem);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, mSelectedItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        MenuItem homeItem = mBottomNav.getMenu().getItem(0);
        if (mSelectedItem != homeItem.getItemId()) {
            // select home item
            selectFragment(homeItem);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.item_logout) {


            Intent intent=new Intent(MenteeTabMenu.this,SignOut.class);
            startActivity(intent);
        }

        if (id == R.id.about) {

            getFragmentManager().beginTransaction().add(R.id.container, new AboutUs()).commit();
        }

        if (id == R.id.calender_date) {

            startActivity(new Intent(MenteeTabMenu.this, MainActivity.class));
        }

        if (id == R.id.menu_profile) {

             startActivity(new Intent(MenteeTabMenu.this, ViewProfileActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tab_layout, menu);
        return true;
    }


    private void selectFragment(MenuItem item) {
        Fragment frag = null;
        // init corresponding fragment
        switch (item.getItemId()) {

            case R.id.menu_appointment:

                //frag = MenuFragment.newInstance(getString(R.string.text_notifications), getColorFromRes(R.color.color_notifications));
                getFragmentManager().beginTransaction().replace(R.id.container, new AppointmentFragment()).commit();

                break;

            case R.id.menu_searchd:

                break;


            case R.id.menu_search:
               // frag = MenuFragment.newInstance(getString(R.string.text_home), getColorFromRes(R.color.color_home));
               // startActivity(new Intent(MenteeTabMenu.this, ViewProfileActivity.class));
                getFragmentManager().beginTransaction().replace(R.id.container, new RecomFragment()).commit();

                break;


        }


        // update selected item
        mSelectedItem = item.getItemId();

        // uncheck the other items.
        for (int i = 0; i < mBottomNav.getMenu().size(); i++) {
            MenuItem menuItem = mBottomNav.getMenu().getItem(i);
            menuItem.setChecked(menuItem.getItemId() == item.getItemId());
        }

        updateToolbarText(item.getTitle());

        if (frag != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.container, frag, frag.getTag());
            ft.commit();
        }
    }

    private void updateToolbarText(CharSequence text) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //actionBar.setTitle("Appointments");
        }
    }

    private int getColorFromRes(@ColorRes int resId) {
        return ContextCompat.getColor(this, resId);
    }
}

