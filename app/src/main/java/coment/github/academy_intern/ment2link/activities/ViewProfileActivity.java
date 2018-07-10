package coment.github.academy_intern.ment2link.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import coment.github.academy_intern.ment2link.pojo.MentorProfile;
import comment.github.academy_intern.ment2link.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class ViewProfileActivity extends AppCompatActivity {

    private CircleImageView imProfilePic;
    private static final int RC_UPLOAD_FILE = 102;
    private TextInputEditText name, study_field;
    private TextInputEditText bio, surname, location;
    private TextView email, title_name;
    private MentorProfile mentorProfile;
    private StorageReference mStorageRef;
    private String uid;
    private DatabaseReference uploadRef;
    private FirebaseUser user;

    private FloatingActionButton floatingActionButton;

    ProgressDialog pd;
    Uri downloadUrl;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;


    //uri to store file
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }


        imProfilePic = (CircleImageView) findViewById(R.id.profile_pic);
        name = findViewById(R.id.username);
        title_name = findViewById(R.id.title_name);
        surname = findViewById(R.id.surname);
        bio = findViewById(R.id.bio);
        email = findViewById(R.id.email);
        location = findViewById(R.id.location);
        study_field = findViewById(R.id.field);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        pd = new ProgressDialog(this);
        pd.setMessage("Uploading....");


        //inizalize the user
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();

        //database reference pointing to root of database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mStorageRef = FirebaseStorage.getInstance().getReference().child(uid);
        uploadRef = database.getReference().child("Users").child(uid);


        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                        mentorProfile = (MentorProfile) postSnapShot.getValue(MentorProfile.class);


                        String profileId = mentorProfile.getUid();
                        String curProfileId = mAuth.getCurrentUser().getUid();

                        if (mentorProfile != null) {

                            if (profileId != null) {

                                if (profileId.equals(curProfileId)) {

                                    String title = mentorProfile.getName() + " " + mentorProfile.getSurname();
                                    title_name.setText(title);
                                    name.setText(mentorProfile.getName());
                                    surname.setText(mentorProfile.getSurname());
                                    bio.setText(mentorProfile.getBio());
                                    location.setText(mentorProfile.getLocation());
                                    email.setText(mentorProfile.getEmail());
                                    study_field.setText(mentorProfile.getField_of_study());
                                    Glide.with(getApplicationContext()).load(mentorProfile.getImageUrl()).into(imProfilePic);


                                }

                            }
                        }
                    }

                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        try{
            Uri imageUri = mAuth.getCurrentUser().getPhotoUrl();
            if(imageUri != null){
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imProfilePic.setImageBitmap(selectedImage);

            }else{
                imProfilePic.setImageResource(R.drawable.unknown);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.floatingActionButton:
                        //pick image
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");
                        startActivityForResult(photoPickerIntent, RC_UPLOAD_FILE);

                }


            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if(requestCode == RC_UPLOAD_FILE && resultCode == RESULT_OK) {



            pd.show();

            Uri uri =data.getData();
            imProfilePic.setImageURI(uri);
            StorageReference filepath = mStorageRef.child(uid);
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    pd.dismiss();
                    Toast.makeText(ViewProfileActivity.this,"Uploading done",Toast.LENGTH_LONG).show();

                    downloadUrl = taskSnapshot.getDownloadUrl();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setPhotoUri(downloadUrl).build();

                    if(user!=null)
                    {
                        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                       //store image on the database
                                    mentorProfile = new MentorProfile();
                                    mentorProfile.setImageUrl(String.valueOf(downloadUrl));


                                    Map<String, Object> userData = new HashMap<String, Object>();

                                    userData.put("imageUrl", mentorProfile.getImageUrl());

                                    uploadRef.updateChildren(userData);

                                    Log.d("Profile Updated ", "User profile updated.");


                                    displayProfilePic(Uri.parse(mentorProfile.getImageUrl()));

                                }

                                else
                                {
                                    Toast.makeText(ViewProfileActivity.this," hello",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });
        }

    }
    private void displayProfilePic(Uri downloadUri) {
        if (downloadUri != null) {
            Picasso.with(ViewProfileActivity.this).load(downloadUri).fit().centerCrop().into(imProfilePic);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile_update, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.updatSos) {

            //String uid, String imageUrl, String name, String surname, String email, String bio, String location, String field_of_study
//            String imageUrl = String.valueOf(downloadUrl);
//            MentorProfile mentorProfile = new MentorProfile(uid,imageUrl,name.getText().toString(),surname.getText().toString(),email.getText().toString(),bio.getText().toString(),location.getText().toString(),study_field.getText().toString());
//            rootRef.setValue(mentorProfile).addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void aVoid) {
//                    pd.dismiss();
//                }
//            });
        }
        if (id == android.R.id.home) {
            onBackPressed();
        }


        return super.onOptionsItemSelected(item);
    }
}