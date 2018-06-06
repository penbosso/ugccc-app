package com.example.samuel.ugcc.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.samuel.ugcc.NetworkUtil;
import com.example.samuel.ugcc.adapters.AnnouncementAdapter;
import com.example.samuel.ugcc.models.Announcement;
import com.example.samuel.ugcc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AnnouncementsFragment extends Fragment {
    private ListView mListView;
    private ArrayList<Long> announcement_indexes;
    private String server_response;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_announcements, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Inflate the layout for this fragment
        mListView = (ListView) view.findViewById(R.id.announcements_list);
        announcement_indexes = new ArrayList<>();

        final ArrayList<Announcement> announcementList = new ArrayList<>();

        String url = NetworkUtil.URL + "ugccc/api/get_info.php?info_type=announcement";

        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                server_response = response;
                try {
                    JSONObject responseObject = new JSONObject(response);
                    if(responseObject.getString("status").equals("0")){
                        JSONArray announcementobjects = responseObject.getJSONArray("announcements");
                        for (int i = 0; i < announcementobjects.length(); i++) {
                            JSONObject announcement = announcementobjects.getJSONObject(i);
                            Long id =Long.parseLong(announcement.getString("id"));
                            announcement_indexes.add(id);
                            announcementList.add(new Announcement(id,
                                                                    announcement.getString("title"),
                                                                    announcement.getString("content"),
                                                                    announcement.getString("image"),
                                                                    announcement.getString("date_created")));
                        }

                        final AnnouncementAdapter adapter = new AnnouncementAdapter(getContext(), announcementList);
                        mListView.setAdapter(adapter);
                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                //sending the announcement details to the next page
                                int item_id = announcement_indexes.indexOf(adapter.getItem(i).getId());
                                Bundle detail_announcement_view=new Bundle();
                                detail_announcement_view.putInt("announcement_id", item_id);
                                detail_announcement_view.putString("server_response", server_response);
                                FullAnnouncement nextFrag= new FullAnnouncement();
                                nextFrag.setArguments(detail_announcement_view);
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.main_layout, nextFrag,"FullAnnouncement")
                                        .addToBackStack(null)
                                        .commit();
                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "No new announcements found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        NetworkUtil.getInstance(getActivity().getApplicationContext()).addToRequestQueue(request);


    }
}
