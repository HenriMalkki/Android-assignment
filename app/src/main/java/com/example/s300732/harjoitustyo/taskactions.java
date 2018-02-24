package com.example.s300732.harjoitustyo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class taskactions extends DialogFragment {

    public String urlAddress2 = "https://codez.savonia.fi/jukka/project/reservetask.php";
    public String urlAddress3 = "https://codez.savonia.fi/jukka/project/starttask.php";
    public String urlAddress4 = "https://codez.savonia.fi/jukka/project/stoptask.php";
    public String urliparametreineen;
    public String kaytettavaurli, useriID, iteminID, iteminstart, iteminstop, iteminuserID;
    Context c = getContext();
    AlertDialog.Builder builder;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        useriID = getArguments().getString("userid");
        iteminuserID = getArguments().getString("iteminuserID");
        iteminID = getArguments().getString("iteminID");
        iteminstart = getArguments().getString("iteminStart");
        iteminstop = getArguments().getString("iteminStop");

        builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Choose an action for the task")
                .setNegativeButton("Reserve", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        kaytettavaurli = urlAddress2;
                        new taskaktiooni().execute();

                    }
                })
                .setNeutralButton("Start", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        kaytettavaurli = urlAddress3;
                        new taskaktiooni().execute();


                    }
                })
                .setPositiveButton("Stop", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        kaytettavaurli = urlAddress4;
                        new taskaktiooni().execute();

                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private class taskaktiooni extends AsyncTask<String, Void, String> {

        // Hashmap for ListView
        ArrayList<UserTaskz> user_tasks;
        ProgressDialog proDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress loading dialog
            proDialog = new ProgressDialog(getContext());
            proDialog.setMessage("Please wait…");
            proDialog.setCancelable(false);
            proDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Log.d("UserID", "> " + useriID);
                if (kaytettavaurli.equals(urlAddress2) && iteminuserID == "null")
                {
                    Log.d("Päästiin tänne", "> " + useriID);
                    urliparametreineen = kaytettavaurli + "?Id=" + iteminID + "&UserId=" + useriID;
                    Log.d("Ja tänne", "> " + useriID);
                }

                else if(kaytettavaurli.equals(urlAddress3) && iteminstart == "null" && iteminstop == "null")
                {
                    urliparametreineen = kaytettavaurli + "?Id=" + iteminID;
                }

                else if(kaytettavaurli.equals(urlAddress4) && iteminstop == "null")
                {
                    urliparametreineen = kaytettavaurli + "?Id=" + iteminID + "&Explanation=Finished";
                }


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



        }

    }
}