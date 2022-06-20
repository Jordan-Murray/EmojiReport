package com.example.navexample;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ProgramViewHolder {
    ImageView moodImage;
    TextView userName;

    ProgramViewHolder(View v)
    {
        moodImage = v.findViewById(R.id.imageView);
        userName = v.findViewById(R.id.listViewName);
    }
}
