package com.example.s300732.harjoitustyo;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class notiTaskDetails extends AppCompatActivity {

    SharedPreferences pref;
    SharedPreferences.Editor mEditor;
    TextView detailsid, detailsuserid, detailsdescription, detailsexplanation, detailsstart, detailsstop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noti_task_details);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        mEditor = pref.edit();

        detailsid = (TextView) findViewById(R.id.details_ID);
        detailsuserid = (TextView) findViewById(R.id.details_UserID);
        detailsdescription = (TextView) findViewById(R.id.details_description);
        detailsexplanation = (TextView) findViewById(R.id.details_explanation);
        detailsstart = (TextView) findViewById(R.id.details_start);
        detailsstop = (TextView) findViewById(R.id.details_stop);

        detailsid.setText(pref.getString("noti_id", "Not received"));
        detailsuserid.setText(pref.getString("noti_userid", "Not received"));
        detailsdescription.setText(pref.getString("noti_description", "Not received"));
        detailsexplanation.setText(pref.getString("noti_explanation", "Not received"));
        detailsstart.setText(pref.getString("noti_start", "Not received"));
        detailsstop.setText(pref.getString("noti_stop", "Not received"));
    }
}
