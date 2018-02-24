package com.example.s300732.harjoitustyo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class deleteActivity extends AppCompatActivity {


    Intent intent;
    public String urlAddress = "https://codez.savonia.fi/jukka/project/deleteuser.php";
    String name_value, pass_value, ID_value;
    SharedPreferences pref;
    SharedPreferences.Editor mEditor;
    String loggedontitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        Button deleteButton = (Button) findViewById(R.id.delete_button);
        EditText delete_nimi = (EditText) findViewById (R.id.delete_name);
        EditText delete_passu = (EditText) findViewById (R.id.delete_pass);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        mEditor = pref.edit();


        if (pref.getBoolean("signedin", false))
        {

            name_value = LoginActivity.logged_name;
            pass_value = LoginActivity.logged_password;
            ID_value = LoginActivity.logged_ID;

            loggedontitle = "Signed in: Yes";

            delete_nimi.setText(name_value);
            delete_passu.setText(pass_value);
        }

        else if (!pref.getBoolean("signedin", false))
        {
            delete_nimi.setText("Not logged in!");
            loggedontitle = "Signed in: No";
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (pref.getBoolean("signedin", false)) {
                    Toast toast4 = Toast.makeText(getApplicationContext(), "Deleting..", Toast.LENGTH_SHORT);
                    //Log.d("", MainActivity.signedin.toString());
                    toast4.show();
                    new deleteActivity.DeleteUser().execute();
                    mEditor.putBoolean("signedin", false);
                    mEditor.putString("ID", "");
                    mEditor.commit();

                }
                else if (!pref.getBoolean("signedin", false)) {

                    Toast toast4 = Toast.makeText(getApplicationContext(), "NOT LOGGED IN", Toast.LENGTH_SHORT);
                    toast4.show();
                }

            }
        });
    }

    private class DeleteUser extends AsyncTask<String, Void, String> {

        ProgressDialog proDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress loading dialog
            proDialog = new ProgressDialog(deleteActivity.this);
            proDialog.setMessage("Please wait…");
            proDialog.setCancelable(false);
            proDialog.show();


        }

        protected String doInBackground(String... params) {
            try {
                String url_parametreineen = urlAddress + "?ID=" + ID_value ;
                Log.d("urli", ">" + url_parametreineen);
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


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actions, menu);

        MenuItem signedinindicator = menu.findItem(R.id.check_log);
        signedinindicator.setTitle(loggedontitle);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast toast = Toast.makeText(getApplicationContext(), "Settings...", Toast.LENGTH_SHORT);
                toast.show();
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_register:
                Toast toast1 = Toast.makeText(getApplicationContext(), "Registering a new user", Toast.LENGTH_SHORT);
                toast1.show();
                intent = new Intent(this, registerActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_delete:
                Toast toast2 = Toast.makeText(getApplicationContext(), "Deleting an existing user", Toast.LENGTH_SHORT);
                toast2.show();
                intent = new Intent(this, deleteActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_update:
                Toast toast3 = Toast.makeText(getApplicationContext(), "Updating an existing user", Toast.LENGTH_SHORT);
                toast3.show();
                intent = new Intent(this, updateActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_login:
                Toast toast5 = Toast.makeText(getApplicationContext(), "Login page", Toast.LENGTH_SHORT);
                toast5.show();
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_home:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
