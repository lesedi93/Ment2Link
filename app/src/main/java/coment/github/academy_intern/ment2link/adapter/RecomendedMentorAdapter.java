package coment.github.academy_intern.ment2link.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.util.List;

import coment.github.academy_intern.ment2link.pojo.Constants;
import coment.github.academy_intern.ment2link.pojo.MentorProfile;
import comment.github.academy_intern.ment2link.R;

/**
 * Created by team_leader on 2018/03/07.
 */

public class RecomendedMentorAdapter extends  RecyclerView.Adapter<RecomendedMentorAdapter.MyViewHolder>{



    private Context context;
    private List<MentorProfile> mentorProfiles;
    //private ContactsAdapterListener listener;

    public RecomendedMentorAdapter(Context context, List<MentorProfile> mentorProfiles) {
        this.context = context;
        this.mentorProfiles = mentorProfiles;

    }

    @Override
    public RecomendedMentorAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recomendedmentors, parent, false);
        return new RecomendedMentorAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(RecomendedMentorAdapter.MyViewHolder holder, final int position) {

        final MentorProfile mentorProfile = mentorProfiles.get(position);
        holder.mentorName.setText(mentorProfile.getName() +" "+mentorProfile.getSurname());
//        holder.email.setText(mentorProfile.getE_mail());
        holder.location.setText(mentorProfile.getLocation());



        if(mentorProfile.getImageUrl() != null)
        {
            Picasso.with(context)
                    .load(mentorProfile.getImageUrl())
                    .placeholder(R.drawable.user)   // optional
                    .error(R.drawable.user)      // optional
                    .rotate(360)                             // optional
                    .into(holder.image);
        }
    }


    @Override
    public int getItemCount() {
        int size = mentorProfiles.size();
        return size;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mentorName,location,email;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            mentorName = view.findViewById(R.id.mentorName);
            location=view.findViewById(R.id.mentorLocation);
//            email = view.findViewById(R.id.mentorEmail);
            image = view.findViewById(R.id.mentorPicture);

        }
    }


//    private Context context; //context
//    private ArrayList<MentorProfile> items; //data source of the list adapter
//
//    //public constructor
//    public RecomendedMentorAdapter(Context context, ArrayList<MentorProfile> items) {
//        this.context = context;
//        this.items = items;
//    }
//
//    @Override
//    public int getCount() {
//        return items.size(); //returns total of items in the list
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return items.get(position); //returns list item at the specified position
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        // inflate the layout for each list row
//        if (convertView == null) {
//            convertView = LayoutInflater.from(context).
//                    inflate(R.layout.recomendedmentors, parent, false);
//        }
//
//        // get current item to be displayed
//        MentorProfile currentItem = (MentorProfile) getItem(position);
//
//        // get the TextView for item name and item description
//        TextView textViewItemName = (TextView)
//                convertView.findViewById(R.id.mentorName);
//        TextView textViewItemDescription = (TextView)
//                convertView.findViewById(R.id.mentorSurname);
//
//        //sets the text for item name and item description from the current item object
//        textViewItemName.setText(currentItem.getName());
//        textViewItemDescription.setText(currentItem.getSurname());
//
//        // returns the view for the current row
//        return convertView;
//    }


}