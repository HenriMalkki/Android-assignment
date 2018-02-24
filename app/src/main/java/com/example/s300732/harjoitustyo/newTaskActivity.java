package com.example.s300732.harjoitustyo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class newTaskActivity extends AppCompatActivity implements LocationListener {

    Intent intent;
    public String urlAddress = "https://codez.savonia.fi/jukka/project/inserttask.php";
    String adminname_value, adminpass_value, description_value, place_value, lat_value, long_value;
    Location location;
    LocationManager lm;
    Criteria criteria;
    String bestProvider;
    int TAG_CODE_PERMISSION_LOCATION = 1;
    double longitude, latitude;
    Boolean locationmapista = false;

    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        Button gobackbutton = (Button) findViewById(R.id.goback_button);
        Button createtaskbutton = (Button) findViewById(R.id.deletetask_button);
        Button mapbutton = (Button) findViewById(R.id.mappibutton);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);


        gobackbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast toast5 = Toast.makeText(getApplicationContext(), "Home page", Toast.LENGTH_SHORT);
                toast5.show();
                intent = new Intent(newTaskActivity.this, MainActivity.class);
                startActivity(intent);


            }
        });

        mapbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast toast5 = Toast.makeText(getApplicationContext(), "Map", Toast.LENGTH_SHORT);
                toast5.show();
                intent = new Intent(newTaskActivity.this, MapsActivity.class);
                startActivity(intent);

                locationmapista = true;

            }
        });

        createtaskbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText admin_nimi = (EditText) findViewById (R.id.admin_name_delete);
                EditText admin_passu = (EditText) findViewById (R.id.admin_pass_delete);
                EditText task_kuvaus = (EditText) findViewById (R.id.task_desc);
                EditText task_placee = (EditText) findViewById (R.id.task_place);

                adminname_value = admin_nimi.getText().toString();
                adminpass_value = admin_passu.getText().toString();
                description_value = task_kuvaus.getText().toString();
                description_value = description_value.replaceAll(" ", "%20");
                place_value = task_placee.getText().toString();
                place_value = place_value.replaceAll(" ", "%20");


                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                    criteria = new Criteria();
                    bestProvider = String.valueOf(lm.getBestProvider(criteria, true)).toString();
                    location = lm.getLastKnownLocation(bestProvider);

                    if (adminname_value.equals(getString(R.string.adminnimi)) && adminpass_value.equals(getString(R.string.adminpassua)) && location != null)
                    {
                        Toast toast4 = Toast.makeText(getApplicationContext(), "Creating a new task...", Toast.LENGTH_SHORT);
                        toast4.show();
                        if (!locationmapista)
                        {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            long_value = Double.toString(longitude);
                            lat_value = Double.toString(latitude);
                            new createNewTask().execute();
                        }
                        else if (locationmapista)
                        {
                            Log.d("Lon ja lat mapista: ", ">" + pref.getString("tasklocationlat", "62.89331") + "...." + pref.getString("tasklocationlon", "27.679334"));
                            latitude = Double.parseDouble(pref.getString("tasklocationlat", "62.89331"));
                            longitude = Double.parseDouble(pref.getString("tasklocationlon", "27.679334"));
                            long_value = Double.toString(longitude);
                            lat_value = Double.toString(latitude);
                            new createNewTask().execute();
                        }

                    }

                    else if (!adminname_value.equals(getString(R.string.adminnimi)) && !adminpass_value.equals(getString(R.string.adminpassua)))
                    {
                        Toast toast4 = Toast.makeText(getApplicationContext(), "Your admin creditentials weren't correct!", Toast.LENGTH_SHORT);
                        toast4.show();
                    }

                    else
                    {
                        lm.requestLocationUpdates(bestProvider, 1000, 0, newTaskActivity.this);
                    }

                }
                else
                {
                    ActivityCompat.requestPermissions(newTaskActivity.this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION }, TAG_CODE_PERMISSION_LOCATION);
                }


                //placeholder

                //long_value = "62.898781";
                //lat_value = "27.662774";

                Log.d("Admin nimi: ", "> " + adminname_value + getString(R.string.adminnimi));
                Log.d("Admin passu: ", "> " + adminpass_value);



            }
        });
    }

    private class createNewTask extends AsyncTask<String, Void, String> {

        ProgressDialog proDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress loading dialog
            proDialog = new ProgressDialog(newTaskActivity.this);
            proDialog.setMessage("Please wait…");
            proDialog.setCancelable(false);
            proDialog.show();


        }

        protected String doInBackground(String... params) {
            try {
                String url_parametreineen = urlAddress + "?Description=" + description_value + "&Lon=" + long_value + "&Lat=" + lat_value + "&Place=" + place_value;
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

    protected void onPause() {
        super.onPause();
        if(lm != null)
        {
            lm.removeUpdates(this);
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        //Hey, a non null location! Sweet!

        //remove location callback:
        lm.removeUpdates(this);

        //open the map:
        if (!locationmapista)
        {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            long_value = Double.toString(longitude);
            lat_value = Double.toString(latitude);
            Toast.makeText(newTaskActivity.this, "latitude:" + latitude + " longitude:" + longitude, Toast.LENGTH_SHORT).show();
            new createNewTask().execute();
        }
        else if (locationmapista)
        {
            latitude = Double.parseDouble(pref.getString("tasklocationlat", "62.89331"));
            longitude = Double.parseDouble(pref.getString("tasklocationlon", "27.679334"));
            long_value = Double.toString(longitude);
            lat_value = Double.toString(latitude);
            Toast.makeText(newTaskActivity.this, "latitude:" + latitude + " longitude:" + longitude, Toast.LENGTH_SHORT).show();
            new createNewTask().execute();
        }

    }

    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void searchNearestPlace(String v2txt) {
        //.....
    }

}

