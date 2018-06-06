package com.example.samuel.ugcc.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samuel.ugcc.NetworkUtil;
import com.example.samuel.ugcc.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class FullAnnouncement extends Fragment {
    int announcement_id;
    String server_response;
    ImageView full_cardImage;
    TextView card_title, full_cardDesc;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_full_announcement, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            announcement_id = bundle.getInt("announcement_id", -1);
            server_response = bundle.getString("server_response");
        }

        full_cardImage = (ImageView) view.findViewById(R.id.full_cardImage);
        full_cardDesc = (TextView) view.findViewById(R.id.full_cardDesc);
        card_title = (TextView) view.findViewById(R.id.card_title);
        displayAnnouncement();
    }

    private void displayAnnouncement() {
        try {
            JSONObject responseObject = new JSONObject(server_response);
            JSONArray announcementobjects = responseObject.getJSONArray("announcements");
            JSONObject announcement = announcementobjects.getJSONObject(announcement_id);
            full_cardDesc.setText(announcement.getString("content"));
            card_title.setText(announcement.getString("title"));
            String image_url = NetworkUtil.URL + "ugccc/images/" + announcement.getString("image");
            Picasso.with(getContext()).load(image_url).into(full_cardImage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
