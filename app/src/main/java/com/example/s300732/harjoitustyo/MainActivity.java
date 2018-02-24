package com.example.s300732.harjoitustyo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.preference.Preference;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.app.Notification;
import android.app.NotificationManager;

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
import java.util.HashMap;

public class MainActivity extends ListActivity implements LocationListener{

    Intent intent;
    public static MainActivity mainActivity;
    public String urlAddress = "https://codez.savonia.fi/jukka/project/UserAndFreeTasks.php";
    public String urliparametreineen;
    //public static String name_value, pass_value, desc_value, logged_name, logged_password;
    SharedPreferences pref;
    SharedPreferences.Editor mEditor;
    ListView listview;
    TextView signedinteksti;
    Bundle bundle;
    FragmentManager fm;
    taskactions dialogFragment;
    Handler h;
    int delay;
    Location location;
    LocationManager lm;
    Criteria criteria;
    String bestProvider;
    int TAG_CODE_PERMISSION_LOCATION = 1;
    double longitude, latitude;
    String long_value, lat_value;
    float prefdistance;
    //public Boolean signedin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button gotoButton = (Button) findViewById(R.id.goto_button);
        Button naytaButton = (Button) findViewById(R.id.nayta_button);
        Button newTaskButton = (Button) findViewById(R.id.newtask_button);
        Button deleteTaskButton = (Button) findViewById(R.id.deletetask_button);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        signedinteksti = (TextView) findViewById(R.id.signedintext);
        mEditor = pref.edit();
        mainActivity = this;
        bundle = new Bundle();





        if (pref.getBoolean("signedin", false))
        {
            signedinteksti.setText("Signed in: Yes");
        }
        else if (!pref.getBoolean("signedin", false))
        {
            signedinteksti.setText("Signed in: No");
        }


        gotoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast toast5 = Toast.makeText(getApplicationContext(), "Login page", Toast.LENGTH_SHORT);
                toast5.show();
                intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });

        newTaskButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast toast5 = Toast.makeText(getApplicationContext(), "Creating a new task", Toast.LENGTH_SHORT);
                toast5.show();
                intent = new Intent(MainActivity.this, newTaskActivity.class);
                startActivity(intent);

            }
        });

        deleteTaskButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast toast5 = Toast.makeText(getApplicationContext(), "Deleting a task", Toast.LENGTH_SHORT);
                toast5.show();
                intent = new Intent(MainActivity.this, deleteTaskActivity.class);
                startActivity(intent);

            }
        });

        naytaButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (pref.getBoolean("signedin", false))
                {
                    signedinteksti.setText("Signed in: Yes");
                    new MainActivity.UserTasks().execute();
                    listview = (ListView) findViewById(android.R.id.list);
                    h = new Handler();
                    delay = Integer.parseInt(pref.getString("interval", "20000"));
                    prefdistance = Float.parseFloat(pref.getString("distance", "1000"));

                    h.postDelayed(new Runnable(){
                        public void run(){

                            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                                criteria = new Criteria();
                                bestProvider = String.valueOf(lm.getBestProvider(criteria, true)).toString();
                                location = lm.getLastKnownLocation(bestProvider);
                                if (location != null)
                                {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                    long_value = Double.toString(longitude);
                                    lat_value = Double.toString(latitude);

                                    new MainActivity.UserTasks().execute();
                                }
                                else
                                {
                                    lm.requestLocationUpdates(bestProvider, 1000, 0, MainActivity.this);
                                }
                            }
                            else
                            {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[] {
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION }, TAG_CODE_PERMISSION_LOCATION);
                            }

                            h.postDelayed(this, delay);
                        }
                    }, delay);


                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                        public void onItemClick(AdapterView<?>adapter,View v, int position, long asd){
                            //ListVitem = adapter.getItemAtPosition(position);

                            String passattavaID  = ((TextView) v.findViewById(R.id.id)).getText().toString();
                            String passattavauserID = pref.getString("ID", "Nullia");
                            String passattavaitemuserID = ((TextView) v.findViewById(R.id.userid)).getText().toString();
                            String passattavastartti = ((TextView) v.findViewById(R.id.start)).getText().toString();
                            String passattavastoppi = ((TextView) v.findViewById(R.id.stop)).getText().toString();

                            Log.d("userid", ">" + passattavauserID);

                            bundle.putString("userid", passattavauserID);
                            bundle.putString("iteminuserID", passattavaitemuserID);
                            bundle.putString("iteminID", passattavaID);
                            bundle.putString("iteminStart", passattavastartti);
                            bundle.putString("iteminStop", passattavastoppi);
                            fm = getFragmentManager();
                            dialogFragment = new taskactions ();
                            dialogFragment.setArguments(bundle);
                            dialogFragment.show(fm, "");
                        }
                    });
                }

                else if (!pref.getBoolean("signedin", false))
                {
                    signedinteksti.setText("Signed in: No");
                }

            }
        });
    }

    private class UserTasks extends AsyncTask<String, Void, String> {

        // Hashmap for ListView
        ArrayList<UserTaskz> user_tasks;
        ProgressDialog proDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress loading dialog
            proDialog = new ProgressDialog(MainActivity.this);
            proDialog.setMessage("Please wait…");
            proDialog.setCancelable(false);
            proDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Log.d("UserID", "> " + pref.getString("ID", "Nullia"));
                urliparametreineen = urlAddress + "?UserID=" + pref.getString("ID", "Nullia");
                URL url = new URL(urliparametreineen);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();

                String inputString;
                while ((inputString = bufferedReader.readLine()) != null) {
                    builder.append(inputString);
                }
                Log.d("Response: ", "> " + builder.toString());

                user_tasks = ParseJSON(builder.toString());
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
// Dismiss the progress dialog
            if (proDialog.isShowing())
                proDialog.dismiss();
/**
 * Updating received data from JSON into ListView
 * */
            MyAdapter adapter = new MyAdapter(MainActivity.mainActivity,user_tasks);

            setListAdapter(adapter);

        }

    }
    private ArrayList<UserTaskz> ParseJSON(String json) {
        ArrayList<UserTaskz> userlist = new <UserTaskz> ArrayList();
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
                    String userID = c.getString("UserID");
                    String start = c.getString("Start");
                    String stop = c.getString("Stop");
                    String description = c.getString("Description");
                    String explanation = c.getString("Explanation");
                    String place = c.getString("Place");
                    if (location != null)
                    {
                        String itemlat = c.getString("Lat");
                        String itemlon = c.getString("Lon");

                        Log.d("lot ja lang molemmista", ">" + itemlat + " " + itemlon  + " " + lat_value + " " + long_value);

                        Location comparelocation1 = new Location("current");
                        comparelocation1.setLatitude(Double.parseDouble(lat_value));
                        comparelocation1.setLongitude(Double.parseDouble(long_value));

                        Location comparelocation2 = new Location("taskslocation");
                        comparelocation2.setLatitude(Double.parseDouble(itemlat));
                        comparelocation2.setLongitude(Double.parseDouble(itemlon));

                        float distance = comparelocation1.distanceTo(comparelocation2);
                        Log.d("Distance between points", ">" + distance);

                        int comparingresult = Float.compare(prefdistance, distance);


                        if (comparingresult == 1)
                        {
                            Log.d("Vahemman!", ">" + comparingresult);
                            mEditor.putString("noti_id", id);
                            mEditor.putString("noti_userid", userID);
                            mEditor.putString("noti_description", description);
                            mEditor.putString("noti_place", place);
                            mEditor.putString("noti_start", start);
                            mEditor.putString("noti_stop", stop);
                            mEditor.commit();
                            showNotification(description, place);
                        }
                        else if (comparingresult == -1)
                        {
                            Log.d("Enemman!", ">" + comparingresult);
                        }
                        else
                        {
                            Log.d("Samanarvoisia!", ">" + comparingresult);
                        }
                    }


                    UserTaskz u = new UserTaskz();

                    u.ID = id;
                    u.UserID = userID;
                    u.Start = start;
                    u.Stop = stop;
                    u.Description = description;
                    u.Explanation = explanation;
                    u.Place = place;
                    u.loggedinuser = pref.getString("ID", "Nullia");

                    // adding user to users list
                    userlist.add(u);
                }
                return userlist;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            Log.e("ServiceHandler", "No data received from HTTP Request");
            return null;
        }
    }

    public void showNotification(String descriptionN, String placeN) {
        Intent notifyIntent = new Intent(this, notiTaskDetails.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[]{notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("There is a free task available near you!")
                .setContentText("Task Desc. " + descriptionN + "\nTask location: " + placeN)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(1, notification);


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

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        long_value = Double.toString(longitude);
        lat_value = Double.toString(latitude);
        Log.d("Lat and lon current", ">" + lat_value + long_value);

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



