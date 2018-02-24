package com.example.s300732.harjoitustyo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class deleteTaskActivity extends AppCompatActivity {

    Intent intent;
    public String urlAddress = "https://codez.savonia.fi/jukka/project/deletetask.php";
    String adminname_value, adminpass_value, deletetaskid_value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_task);

        Button gobackbutton = (Button) findViewById(R.id.goback_button);
        Button deletetaskbutton = (Button) findViewById(R.id.deletetask_button);

        gobackbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast toast5 = Toast.makeText(getApplicationContext(), "Home page", Toast.LENGTH_SHORT);
                toast5.show();
                intent = new Intent(deleteTaskActivity.this, MainActivity.class);
                startActivity(intent);


            }
        });

        deletetaskbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText admin_nimi = (EditText) findViewById(R.id.admin_name_delete);
                EditText admin_passu = (EditText) findViewById(R.id.admin_pass_delete);
                EditText deletetaskid = (EditText) findViewById(R.id.delete_id);

                adminname_value = admin_nimi.getText().toString();
                adminpass_value = admin_passu.getText().toString();
                deletetaskid_value = deletetaskid.getText().toString();

                if (adminname_value.equals(getString(R.string.adminnimi)) && adminpass_value.equals(getString(R.string.adminpassua)))
                {
                    new deleteTask().execute();
                }
                else
                {
                    Toast toast4 = Toast.makeText(getApplicationContext(), "Your admin creditentials weren't correct!", Toast.LENGTH_SHORT);
                    toast4.show();
                }

            }
        });
    }

    private class deleteTask extends AsyncTask<String, Void, String> {

        ProgressDialog proDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress loading dialog
            proDialog = new ProgressDialog(deleteTaskActivity.this);
            proDialog.setMessage("Please wait…");
            proDialog.setCancelable(false);
            proDialog.show();


        }

        protected String doInBackground(String... params) {
            try {
                Log.d("ID: ", "> " + deletetaskid_value);
                String url_parametreineen = urlAddress + "?Id=" + deletetaskid_value;
                URL url = new URL(url_parametreineen);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();

                String inputString;
                while ((inputString = bufferedReader.readLine()) != null) {
                    builder.append(inputString);
                }
                Log.d("Response: ", "> " + builder.toString());

            }
            catch (Exception ex)
            {
                Log.d("Response: ", "> " + ex.getMessage());
            }

            return null;
        }
        @Override
        protected void onPostExecute(String requestresult) {
            super.onPostExecute(requestresult);
            if (proDialog.isShowing())
                proDialog.dismiss();

        }
    }
}
