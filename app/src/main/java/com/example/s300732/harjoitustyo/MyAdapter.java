package com.example.s300732.harjoitustyo;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.s300732.harjoitustyo.R;
import com.example.s300732.harjoitustyo.UserTaskz;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class MyAdapter extends ArrayAdapter<UserTaskz> {
    private final Context context;
    private final ArrayList<UserTaskz> userstasks;

    public MyAdapter(Context context, ArrayList<UserTaskz> userstasks) {
        super(context, R.layout.activity_main, userstasks);
        this.context = context;
        this.userstasks = userstasks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item, parent, false);
        TextView textView1 = (TextView) rowView.findViewById(R.id.id);
        TextView textView2 = (TextView) rowView.findViewById(R.id.description);
        TextView textView3 = (TextView) rowView.findViewById(R.id.explanation);
        TextView textView4 = (TextView) rowView.findViewById(R.id.place);
        TextView textView5 = (TextView) rowView.findViewById(R.id.userid);
        TextView textView6 = (TextView) rowView.findViewById(R.id.start);
        TextView textView7 = (TextView) rowView.findViewById(R.id.stop);


        String user = userstasks.get(position).UserID;
        String loggeduser = userstasks.get(position).loggedinuser;
        String startti = userstasks.get(position).Start;
        String stoppi = userstasks.get(position).Stop;

        Log.d("Startti ja stoppi", ">" + startti + stoppi);

        if (user == "null" && startti == "null" && stoppi == "null")
        {
            rowView.setBackgroundColor(Color.GREEN);
        }
        else if (user.equals(loggeduser) && startti == "null" )
        {
            rowView.setBackgroundColor(Color.YELLOW);
        }
        else if (user.equals(loggeduser) && startti != "null" && stoppi == "null")
        {
            rowView.setBackgroundColor(Color.CYAN);
        }
        else if (startti != "null" && stoppi != "null")
        {
            rowView.setBackgroundColor(Color.BLUE);
        }


        textView1.setText(userstasks.get(position).ID);
        textView5.setText(user);
        textView2.setText(userstasks.get(position).Description);
        textView3.setText(userstasks.get(position).Explanation);
        textView4.setText(userstasks.get(position).Place);
        textView6.setText(startti);
        textView7.setText(stoppi);

        return rowView;
    }
}