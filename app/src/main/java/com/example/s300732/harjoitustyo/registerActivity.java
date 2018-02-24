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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class registerActivity extends AppCompatActivity {

    Intent intent;
    public String urlAddress = "https://codez.savonia.fi/jukka/project/insertuser.php";
    String name_value, pass_value, desc_value;
    String loggedontitle;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        Button registerButton = (Button) findViewById(R.id.register_button);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        if (pref.getBoolean("signedin", false))
        {

            loggedontitle = "Signed in: Yes";
        }

        else if (!pref.getBoolean("signedin", false))
        {
            loggedontitle = "Signed in: No";
        }

        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText register_nimi = (EditText) findViewById (R.id.register_name);
                EditText register_passu = (EditText) findViewById (R.id.register_password);
                EditText register_kuvaus = (EditText) findViewById (R.id.register_description);

                name_value = register_nimi.getText().toString();
                pass_value = register_passu.getText().toString();
                desc_value = register_kuvaus.getText().toString();

                Toast toast4 = Toast.makeText(getApplicationContext(), "Registering...", Toast.LENGTH_SHORT);
                toast4.show();
                new RegisterUser().execute();



                /*try
                {
                    String parametrit = "?Name=" + name_value + "&Password=" + pass_value + "&Description=" + desc_value;
                    URL url = new URL (urlAddress);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoOutput(true);
                    DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                    wr.writeBytes(parametrit);
                    wr.flush();
                    wr.close();

                    int responseCode = urlConnection.getResponseCode();
                    String TAG = "RegisterActivity";
                    Log.d(TAG, "Sending 'POST' request to URL : " + url);
                    Log.d(TAG, "Post parameters: " + parametrit);
                    Log.d(TAG, "Response code: " + responseCode);
                }

                catch (Exception e)
                {
                    e.printStackTrace();
                }*/
            }
        });
    }

    private class RegisterUser extends AsyncTask<String, Void, String> {

        ProgressDialog proDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress loading dialog
            proDialog = new ProgressDialog(registerActivity.this);
            proDialog.setMessage("Please wait…");
            proDialog.setCancelable(false);
            proDialog.show();


        }

        protected String doInBackground(String... params) {
            try {
                String url_parametreineen = urlAddress + "?Name=" + name_value + "&Password=" + pass_value + "&Description=" + desc_value;
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
