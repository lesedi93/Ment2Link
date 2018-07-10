package coment.github.academy_intern.ment2link.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


import android.widget.Filter;

import coment.github.academy_intern.ment2link.pojo.MentorProfile;
import comment.github.academy_intern.ment2link.R;


/**
 * Created by team_leader on 2018/02/27.
 */

/**
 * Created by team_leader on 2018/02/27.
 */

public class SearchMentorAdapter extends RecyclerView.Adapter<SearchMentorAdapter.MyViewHolder> implements Filterable
{


    private Context context;
    private List<MentorProfile> profiles;
    private List<MentorProfile> filteredList = null;
    private SearchAdapterListener listener;
    MentorProfile INDEX;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, surname,email,study;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            surname = view.findViewById(R.id.surname);
            email = view.findViewById(R.id.email);
            study= view.findViewById(R.id.study);
            thumbnail = view.findViewById(R.id.thumbnail);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // send selected contact in callback
                    if(filteredList!=null)
                    {
                        listener.onSearchSelected(filteredList.get(getAdapterPosition()));

                    }else {

                        listener.onSearchSelected(profiles.get(getAdapterPosition()));
                    }

                }
            });
        }
    }


    public SearchMentorAdapter(Context context, List<MentorProfile> profiles,SearchAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.profiles = profiles;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row_xml, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final MentorProfile mentors = profiles.get(position);

        holder.name.setText(mentors.getName());
        holder.surname.setText(mentors.getSurname());
//        holder.email.setText(mentors.getE_mail());
        holder.study.setText(mentors.getField_of_study());


        Picasso.with(context)
                .load(mentors.getImageUrl())
                .placeholder(R.drawable.user)   // optional
                .error(R.drawable.user)      // optional
                .rotate(360)                             // optional
                .into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults= new FilterResults();

                filteredList = new ArrayList<>();

                for (MentorProfile row : profiles) {

                    // name match condition. this might differ depending on your requirement
                    // here we are looking for name or surname number match
                    if (row.getName().toUpperCase().startsWith(String.valueOf(constraint).toUpperCase())||row.getField_of_study().toUpperCase().contains(String.valueOf(constraint).toUpperCase())) {
                        filteredList.add(row);
                    }
                }

                filterResults.values = filteredList;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                profiles = (ArrayList<MentorProfile>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public interface SearchAdapterListener {
        void onSearchSelected(MentorProfile prof);

    }

}