package com.example.samuel.ugcc.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.samuel.ugcc.NetworkUtil;
import com.example.samuel.ugcc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookSessionFragment extends Fragment {
    private static final String TAG = "BookSessionFragment";
    Spinner stressors, scheduled_time;
    EditText description;
    TextView counsellor_assigned, signup;
    ArrayList<Long> numberArray;
    ArrayList<String> dateStringArray;
    ProgressDialog progressBar;

    String  password, stressor, descriptiontext, scheduled_time_txt;
    int student_id, counsellor_assigned_id;

    public BookSessionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_book_session, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.stressors, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        stressors.setAdapter(adapter);

        numberArray =  new ArrayList<Long>();
        dateStringArray =new ArrayList<String>();


        stressors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = adapterView.getItemAtPosition(i).toString();
                if(!selectedItem.equals("Choose A Stressor ..."))
                {
                    stressor =selectedItem;
                    String url= NetworkUtil.URL+"ugccc/api/get_counsellors.php";

                    StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i(TAG, "onResponse: " +response);
                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                if(jsonObject.getInt("status") == 0){
                                  counsellor_assigned.setVisibility(View.VISIBLE);
                                    scheduled_time.setVisibility(View.VISIBLE);
                                    counsellor_assigned.setText("Counsellor Assigned: "+" \n"
                                                        +jsonObject.getString("counsellor_name"));
                                    JSONArray available_booking = jsonObject.getJSONArray("available_booking");
                                    for (int i=0; i< available_booking.length(); i++){
                                        int unixtime = available_booking.getInt(i);
                                        numberArray.add(new Long(unixtime));

                                        // convert seconds to milliseconds
                                        Date date = new java.util.Date(unixtime*1000L);
                                        // the format of your date
                                        SimpleDateFormat sdf = new java.text.SimpleDateFormat("E dd-MM-yyyy HH:mm a");
                                        String formattedDate = sdf.format(date);
                                        dateStringArray.add(formattedDate);
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                            getContext(), android.R.layout.simple_spinner_item, dateStringArray);
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    scheduled_time.setAdapter(adapter);
                                    counsellor_assigned_id = jsonObject.getInt("counsellor_id");

                                }  else{
                                    counsellor_assigned.setVisibility(View.GONE);
                                    scheduled_time.setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                    }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params=new HashMap<>();
                            params.put("student_id", student_id+"");
                            params.put("password", password);
                            params.put("stressor", stressor);
                            params.put("short_desc", descriptiontext);
                            return params;
                        }
                    };

                    NetworkUtil.getInstance(getContext()).addToRequestQueue(request);
                } else if(selectedItem.equals("Others...")){
                    stressor=selectedItem;
                    counsellor_assigned.setVisibility(View.GONE);
                    scheduled_time.setVisibility(View.GONE);
                } else{
                    Toast.makeText(getContext(), "Please fill Details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        scheduled_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
             scheduled_time_txt = numberArray.get(i)+"";
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                scheduled_time_txt ="";
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.show();
                descriptiontext = description.getText().toString();
                String url= NetworkUtil.URL+"ugccc/api/assign_counsellor.php";
                StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if(jsonObject.getInt("status") == 0){
                                Toast.makeText(getContext(), "Complaint filled successfully", Toast.LENGTH_SHORT).show();

                            } else{
                                Toast.makeText(getContext(), "Complaint filling failed", Toast.LENGTH_SHORT).show();
                            }
                            progressBar.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params=new HashMap<>();
                        params.put("student_id", student_id+"");
                        params.put("password", password);
                        params.put("stressor", stressor);
                        params.put("short_desc", descriptiontext);
                        params.put("scheduled_date", scheduled_time_txt);
                        params.put("counsellor_id", counsellor_assigned_id+"");
                        return params;
                    }
                };
                NetworkUtil.getInstance(getContext()).addToRequestQueue(request);
            }
        });

    }

    private void init(View view) {
        stressors = (Spinner) view.findViewById(R.id.stressors);
        description = (EditText) view.findViewById(R.id.description);
        counsellor_assigned = (TextView) view.findViewById(R.id.counsellor_assigned);
        scheduled_time= (Spinner) view.findViewById(R.id.scheduled_time);
        signup = (TextView) view.findViewById(R.id.signup);

        student_id = 10518900;
        password = "hokd";
        descriptiontext="";
        counsellor_assigned_id = 0;
        scheduled_time_txt = "";

        progressBar = new ProgressDialog(getActivity());
        progressBar.setTitle("Sending Complaint Details");
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);

    }
}
