package com.example.s300732.harjoitustyo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
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

public class LoginActivity extends AppCompatActivity {
    Intent intent;
    public String urlAddress = "https://codez.savonia.fi/jukka/project/user.php";
    public static String name_value, pass_value, desc_value, logged_name, logged_password, logged_desc, logged_ID;
    SharedPreferences pref;
    SharedPreferences.Editor mEditor;
    Toolbar myToolbar;
    public Boolean signedin;
    String loggedontitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        Button loginButton = (Button) findViewById(R.id.button_login);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        mEditor = pref.edit();
        loggedontitle = "Signed in: No";

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText login_nimi = (EditText) findViewById (R.id.textbox_name);
                EditText login_passu = (EditText) findViewById (R.id.textbox_password);

                name_value = login_nimi.getText().toString();
                pass_value = login_passu.getText().toString();

                Toast toast4 = Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT);

                toast4.show();
                new LoginActivity.LoginUser().execute();


            }
        });
    }

    private class LoginUser extends AsyncTask<String, Void, String> {

        ProgressDialog proDialog;
        Toast toast5 = Toast.makeText(getApplicationContext(), "Login FAILED!", Toast.LENGTH_LONG);
        Toast toast6 = Toast.makeText(getApplicationContext(), "Logged in!", Toast.LENGTH_LONG);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress loading dialog
            proDialog = new ProgressDialog(LoginActivity.this);
            proDialog.setMessage("Please wait…");
            proDialog.setCancelable(false);
            proDialog.show();


        }

        protected String doInBackground(String... params) {
            try {
                String url_parametreineen = urlAddress + "?Name=" + name_value + "&Password=" + pass_value;
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
                if (builder.toString().equals("[]"))
                {
                    signedin = false;
                    mEditor.putBoolean("signedin", signedin);
                    mEditor.putString("ID", "");
                    mEditor.commit();
                    Log.d("", "LOGIN FAILED");
                    toast5.show();
                    Log.d("", signedin.toString());
                    loggedontitle = "Signed in: No";
                    supportInvalidateOptionsMenu();
                }
                else
                {
                    signedin = true;
                    Log.d("", signedin.toString());
                    mEditor.putBoolean("signedin", signedin);
                    mEditor.commit();
                    toast6.show();
                    logged_name = name_value;
                    logged_password = pass_value;
                    ParseJSON(builder.toString());
                    loggedontitle = "Signed in: Yes";
                    Log.d("", loggedontitle);
                    supportInvalidateOptionsMenu();

                }

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
    private void ParseJSON(String json) {

        if (json != null) {
            try {

                //JSONObject jsonObj = new JSONObject(json);
                Log.d("JSON to list: ", "> " + json);

                // Getting JSON Array node
                JSONArray users = new JSONArray(json);

                // looping through All Users
                for (int i = 0; i < users.length(); i++) {
                    JSONObject c = users.getJSONObject(i);

                    String id = c.getString("ID");
                    logged_desc = c.getString("Description");
                    mEditor.putString("ID", id);
                    logged_ID = id;
                    mEditor.commit();

                }

            } catch (JSONException e) {
                e.printStackTrace();

            }
        } else {
            Log.e("ServiceHandler", "No data received from HTTP Request");
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

