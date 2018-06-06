package com.example.samuel.ugcc.activities;

/**
 * Created by Samuel on 10-Mar-18.
 */
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Samuel on 23-Feb-18.
 */
import android.support.v7.app.AppCompatActivity;

import com.example.samuel.ugcc.R;

public class WelcomeActivity extends AppCompatActivity{
    TextView sin;
    LinearLayout circle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);

        circle = (LinearLayout)findViewById(R.id.circle);
        sin = (TextView)findViewById(R.id.sin);

        circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(WelcomeActivity.this,SignUpActivity.class);
                startActivity(it);

            }
        });
        sin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(WelcomeActivity.this,SignInActivity.class);
                startActivity(it);
            }
        });

    }
}
