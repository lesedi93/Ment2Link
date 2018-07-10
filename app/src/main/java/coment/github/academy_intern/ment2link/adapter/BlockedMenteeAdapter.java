package coment.github.academy_intern.ment2link.adapter;

/**
 * Created by team_leader on 2018/03/19.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import coment.github.academy_intern.ment2link.pojo.Blocked;
import coment.github.academy_intern.ment2link.pojo.MentorProfile;
import comment.github.academy_intern.ment2link.R;

/**
 * Created by academy_intern on 3/14/18.
 */

public class BlockedMenteeAdapter extends RecyclerView.Adapter<BlockedMenteeAdapter.MyViewHolder> {


    private Context context;
    private List<Blocked> blockedMentees;
    //private ContactsAdapterListener listener;

    public BlockedMenteeAdapter(Context context, List<Blocked> blockedMentees,List<MentorProfile> mentees) {
        this.context = context;
        this.blockedMentees = blockedMentees;



    }

    @Override
    public BlockedMenteeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.blockedmentee, parent, false);
        return new BlockedMenteeAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final BlockedMenteeAdapter.MyViewHolder holder, final int position) {
        final Blocked blocked = blockedMentees.get(position);

        final DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Users");
        database.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                MentorProfile user;

                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    user = snap.getValue(MentorProfile.class);


                    String userId = user.getUid();
                    String blockedMenteeId = blocked.getBlocked_mentee_uid();

                    if (userId != null) {

                        if (userId.equals(blockedMenteeId)) {
                            holder.name.setText((user.getName() + " " + user.getSurname()));


                            String urlPic = user.getImageUrl();
                            Picasso.with(context)
                                    .load(urlPic)
                                    .placeholder(R.drawable.user)   // optional
                                    .error(R.drawable.user)      // optional
                                    .rotate(360)                             // optional
                                    .into(holder.image);
                        }

                    }

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }


    @Override
    public int getItemCount() {
        int size = blockedMentees.size();
        return size;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, surname;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.menteeName);
            surname = view.findViewById(R.id.menteeSurname);
            image = view.findViewById(R.id.profile);

        }
    }
}
