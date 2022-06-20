package com.example.navexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ProgramAdapter extends ArrayAdapter<String> {
    Context context;
    List<Integer> images;
    List<UserReferenceClass> users;
    List<LocationTag> locationTag;
    String[] locationTagArr;
    List<String> itemTitles;
    boolean isMyProfile;

    public ProgramAdapter(Context context, List<UserReferenceClass> users, List<String> itemTitles , List<Integer> images , List<LocationTag> locationTag, boolean isMyProfile){
        super(context,R.layout.single_item,R.id.listViewName,itemTitles);
        this.context = context;
        this.images =  images;
        this.itemTitles = itemTitles;
        this.users = users;
        this.locationTag = locationTag;
        this.isMyProfile = isMyProfile;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View singleItem = convertView;
        ProgramViewHolder holder = null;

        if(singleItem == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            singleItem = layoutInflater.inflate(R.layout.single_item,parent,false);
            holder = new ProgramViewHolder(singleItem);
            singleItem.setTag(holder);
        }else{
            holder = (ProgramViewHolder) singleItem.getTag();
        }
        holder.moodImage.setImageResource(images.get(position));
        holder.userName.setText(itemTitles.get(position));
        singleItem.setOnClickListener(v -> {
            if(isMyProfile)
            {
                if(locationTag != null)
                    locationTagArr = new String[]{String.valueOf(locationTag.get(position).Latitude), String.valueOf(locationTag.get(position).Longitude), locationTag.get(position).locationName,users.get(0).UserRef};
                MainActivity.loadFragment(new MapViewFragment(),new MyProfileFragment(),locationTagArr);
            }
            else
            {
                String[] userRefArr = {users.get(position).UserRef};
                MainActivity.loadFragment(new MyProfileFragment(), new MyTeamFragment(),userRefArr);
            }
        });
        return singleItem;
    }
}
