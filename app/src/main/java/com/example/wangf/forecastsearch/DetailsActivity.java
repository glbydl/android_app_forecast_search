package com.example.wangf.forecastsearch;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by wangf on 2015/11/18.
 */
public class DetailsActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);

        TextView textView = (TextView) findViewById(R.id.more_details_title);
        textView.setText("More Details for " + MainActivity.city_bk + ", " + MainActivity.state_bk);

        selectFrag(findViewById(R.id.button_next_24_hours));

    }

    public void selectFrag(View view) {
        Fragment fr;

        if(view == findViewById(R.id.button_next_24_hours)) {
            fr = new Next24HoursFragment();
            Button button_next_24_hours = (Button) view;
            button_next_24_hours.setBackgroundColor(getResources().getColor(R.color.midBlue));
            Button button_next_7_days = (Button) findViewById(R.id.button_next_7_days);
            button_next_7_days.setBackgroundColor(getResources().getColor(R.color.superGray));
        } else {
            fr = new Next7DaysFragment();
            Button button_next_24_hours = (Button) findViewById(R.id.button_next_24_hours);
            button_next_24_hours.setBackgroundColor(getResources().getColor(R.color.superGray));
            Button button_next_7_days = (Button) view;
            button_next_7_days.setBackgroundColor(getResources().getColor(R.color.midBlue));
        }

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_place, fr);
        fragmentTransaction.commit();

    }

    /**
     * create weather icon map
     * @return weather icon map
     */
    public HashMap<String, String> createWeatherIconMap() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("clear-day", "s_clear");
        map.put("clear-night", "s_clear_night");
        map.put("rain", "s_rain");
        map.put("snow", "s_snow");
        map.put("sleet", "s_sleet");
        map.put("wind", "s_wind");
        map.put("fog", "s_fog");
        map.put("cloudy", "s_cloudy");
        map.put("partly-cloudy-day", "s_cloud_day");
        map.put("partly-cloudy-night", "s_cloud_night");
        return map;
    }
}
