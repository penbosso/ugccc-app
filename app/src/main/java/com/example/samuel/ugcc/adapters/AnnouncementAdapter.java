package com.example.samuel.ugcc.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.samuel.ugcc.NetworkUtil;
import com.example.samuel.ugcc.models.Announcement;
import com.example.samuel.ugcc.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Samuel on 06-Apr-18.
 */

public class AnnouncementAdapter extends ArrayAdapter<Announcement> {
    public AnnouncementAdapter(@NonNull Context context, ArrayList<Announcement> announcements) {
        super(context, 0, announcements);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View announcement_view = convertView;

        if (announcement_view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            announcement_view = inflater.inflate(R.layout.card_view, null);
        }

        ImageView image = (ImageView) announcement_view.findViewById(R.id.cardImage);
        TextView title = (TextView) announcement_view.findViewById(R.id.cardDesc);

        Announcement announcement = getItem(position);

        //setting the details
          String image_url = NetworkUtil.URL + "ugccc/images/" + announcement.getImage();
        title.setText(announcement.getTitle());
        Picasso.with(getContext()).load(image_url).into(image);

        return announcement_view;
    }
}
