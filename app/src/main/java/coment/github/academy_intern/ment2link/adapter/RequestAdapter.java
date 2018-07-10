package coment.github.academy_intern.ment2link.adapter;

/**
 * Created by team_leader on 2018/03/02.
 */

/**
 * Created by academy_intern on 2/7/18.
 */



        import android.content.Context;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.bumptech.glide.Glide;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;
        import com.squareup.picasso.Picasso;

        import java.util.ArrayList;
        import java.util.List;

        import coment.github.academy_intern.ment2link.pojo.MentorProfile;
        import coment.github.academy_intern.ment2link.pojo.Request;
        import comment.github.academy_intern.ment2link.R;


public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder>{
    private Context context;
    private List<Request> mentorReq;


    public RequestAdapter(Context context,List<Request> mentorReq) {
        this.context = context;
        this.mentorReq = mentorReq;

    }
    @Override
    public RequestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_mentor_card, parent, false);
        return new ViewHolder(view);
    }

    public void updateData(ArrayList<Request> requests)
    {
        mentorReq.clear();
        mentorReq.addAll(requests);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final RequestAdapter.ViewHolder holder, int position) {
        final Request requestMentor = mentorReq.get(position);

        final DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Users");

        database.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                MentorProfile user;

                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    user = snap.getValue(MentorProfile.class);

                    String reqMenteeUid = requestMentor.getMentee_uid();
                    String userUid = user.getUid();


                    if (userUid != null) {

                        if (userUid.equals(reqMenteeUid)) {


                            String urlPic = user.getImageUrl();
                            Picasso.with(context)
                                    .load(urlPic)
                                    .placeholder(R.drawable.user)   // optional
                                    .error(R.drawable.user)      // optional
                                    .rotate(360)                             // optional
                                    .into(holder.mentee_picture);
                        }

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Glide.with(context).load(mentorReq.get(position).getImageUrl()).into(holder.mentee_picture);
        holder.mentee_date.setText(mentorReq.get(position).getDate());
        holder.mentee_reason.setText(mentorReq.get(position).getReason());


    }

    @Override
    public int getItemCount() {
        return mentorReq.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mentee_reason,mentee_date;
        private ImageView mentee_picture;

        public ViewHolder(View view) {
            super(view);
            mentee_picture = view.findViewById(R.id.person_photo);
            mentee_reason = view.findViewById(R.id.person_name);
            mentee_date = view.findViewById(R.id.person_date);

        }
    }
}



