package coment.github.academy_intern.ment2link.adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import coment.github.academy_intern.ment2link.pojo.Bookings;
import coment.github.academy_intern.ment2link.pojo.MentorProfile;
import coment.github.academy_intern.ment2link.pojo.UpcomingAppointment;
import comment.github.academy_intern.ment2link.R;

/**
 * Created by academy_intern on 2018/03/01.
 */

public class RecomendedUpcomingAdapter extends RecyclerView.Adapter<RecomendedUpcomingAdapter.MyViewHolder>{



    private Context context;
    private List<Bookings> bookings;
    //private ContactsAdapterListener listener;

    public RecomendedUpcomingAdapter(Context context, List<Bookings> bookings) {
        this.context = context;
        //this.listener = listener;
        this.bookings = bookings;

    }

    @Override
    public RecomendedUpcomingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.upcoming_appointment, parent, false);
        return new RecomendedUpcomingAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final RecomendedUpcomingAdapter.MyViewHolder holder, final int position) {

        final Bookings booking = bookings.get(position);
        holder.reason.setText(booking.getSession_details());
        holder.date.setText(booking.getTime());
        //holder.email.setText(mentors.getE_mail());

        DatabaseReference db1 = FirebaseDatabase.getInstance().getReference().child("Users");

        db1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    MentorProfile mentorProfile = snapshot.getValue(MentorProfile.class);

                    String mentorUid = mentorProfile.getUid();
                    String bookingMentorUid = booking.getBooking_mentor_uid();


                    if (mentorUid != null) {

                        if (mentorUid.equalsIgnoreCase(bookingMentorUid)) {


                            Picasso.with(context)
                                    .load(mentorProfile.getImageUrl())
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
        int size = bookings.size();
        return size;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView reason, date;
        ImageView image;

        public MyViewHolder(View view) {
            super(view);
            reason = view.findViewById(R.id.upcomingReason);
            date = view.findViewById(R.id.upcomingDate);
            image = view.findViewById(R.id.profile);

        }
    }


}
