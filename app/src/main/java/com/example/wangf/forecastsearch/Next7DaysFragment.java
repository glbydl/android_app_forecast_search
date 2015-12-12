package com.example.wangf.forecastsearch;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by wangf on 2015/12/9.
 */
public class Next7DaysFragment extends Fragment {

    private String temperatureUnit;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_next_7_days, container, false);

        DetailsActivity detailsActivity = new DetailsActivity();

        // create weather icon map
        HashMap<String, String> iconMap = detailsActivity.createWeatherIconMap();

        Resources res = getResources();

        if (MainActivity.degree_bk.equals("us")) {
            temperatureUnit = "°F";
        } else {
            temperatureUnit = "°C";
        }

        JSONArray daylyDataArray = ResultActivity
                .jsonResult
                .optJSONObject("daily")
                .optJSONArray("data");
        try {
            // set the date of the first day of next week
            JSONObject detailData1 = (JSONObject) daylyDataArray.get(1);
            TextView textViewDate1 = (TextView) view.findViewById(R.id.next_7_days_1_date);
            textViewDate1.setText(this.changeDateFormat(detailData1.optString("time"), detailData1.optString("timezone")));
            ImageView imageView1 = (ImageView) view.findViewById(R.id.next_7_days_1_icon);
            int iconId = res.getIdentifier(iconMap.get(detailData1.optString("icon")), "drawable", getActivity().getPackageName());
            imageView1.setImageResource(iconId);
            TextView textViewTemp1 = (TextView) view.findViewById(R.id.next_7_days_1_temp);
            textViewTemp1.setText("Min:" + Math.round(Double.valueOf(detailData1.optString("temperatureMin")))
                    + temperatureUnit + " | " + "Max:"
                    + Math.round(Double.valueOf(detailData1.optString("temperatureMax"))) + temperatureUnit);

            // set the date of the second day of next week
            JSONObject detailData2 = (JSONObject) daylyDataArray.get(2);
            TextView textViewDate2 = (TextView) view.findViewById(R.id.next_7_days_2_date);
            textViewDate2.setText(this.changeDateFormat(detailData2.optString("time"), detailData2.optString("timezone")));
            ImageView imageView2 = (ImageView) view.findViewById(R.id.next_7_days_2_icon);
            iconId = res.getIdentifier(iconMap.get(detailData2.optString("icon")), "drawable", getActivity().getPackageName());
            imageView2.setImageResource(iconId);
            TextView textViewTemp2 = (TextView) view.findViewById(R.id.next_7_days_2_temp);
            textViewTemp2.setText("Min:" + Math.round(Double.valueOf(detailData2.optString("temperatureMin")))
                    + temperatureUnit + " | " + "Max:"
                    + Math.round(Double.valueOf(detailData2.optString("temperatureMax"))) + temperatureUnit);

            // set the date of the third day of next week
            JSONObject detailData3 = (JSONObject) daylyDataArray.get(3);
            TextView textViewDate3 = (TextView) view.findViewById(R.id.next_7_days_3_date);
            textViewDate3.setText(this.changeDateFormat(detailData3.optString("time"), detailData3.optString("timezone")));
            ImageView imageView3 = (ImageView) view.findViewById(R.id.next_7_days_3_icon);
            iconId = res.getIdentifier(iconMap.get(detailData3.optString("icon")), "drawable", getActivity().getPackageName());
            imageView3.setImageResource(iconId);
            TextView textViewTemp3 = (TextView) view.findViewById(R.id.next_7_days_3_temp);
            textViewTemp3.setText("Min:" + Math.round(Double.valueOf(detailData3.optString("temperatureMin")))
                    + temperatureUnit + " | " + "Max:"
                    + Math.round(Double.valueOf(detailData3.optString("temperatureMax"))) + temperatureUnit);

            // set the date of the fourth day of next week
            JSONObject detailData4 = (JSONObject) daylyDataArray.get(4);
            TextView textViewDate4 = (TextView) view.findViewById(R.id.next_7_days_4_date);
            textViewDate4.setText(this.changeDateFormat(detailData4.optString("time"), detailData4.optString("timezone")));
            ImageView imageView4 = (ImageView) view.findViewById(R.id.next_7_days_4_icon);
            iconId = res.getIdentifier(iconMap.get(detailData4.optString("icon")), "drawable", getActivity().getPackageName());
            imageView4.setImageResource(iconId);
            TextView textViewTemp4 = (TextView) view.findViewById(R.id.next_7_days_4_temp);
            textViewTemp4.setText("Min:" + Math.round(Double.valueOf(detailData4.optString("temperatureMin")))
                    + temperatureUnit + " | " + "Max:"
                    + Math.round(Double.valueOf(detailData4.optString("temperatureMax"))) + temperatureUnit);

            // set the date of the fifth day of next week
            JSONObject detailData5 = (JSONObject) daylyDataArray.get(5);
            TextView textViewDate5 = (TextView) view.findViewById(R.id.next_7_days_5_date);
            textViewDate5.setText(this.changeDateFormat(detailData5.optString("time"), detailData5.optString("timezone")));
            ImageView imageView5 = (ImageView) view.findViewById(R.id.next_7_days_5_icon);
            iconId = res.getIdentifier(iconMap.get(detailData5.optString("icon")), "drawable", getActivity().getPackageName());
            imageView5.setImageResource(iconId);
            TextView textViewTemp5 = (TextView) view.findViewById(R.id.next_7_days_5_temp);
            textViewTemp5.setText("Min:" + Math.round(Double.valueOf(detailData5.optString("temperatureMin")))
                    + temperatureUnit + " | " + "Max:"
                    + Math.round(Double.valueOf(detailData5.optString("temperatureMax"))) + temperatureUnit);

            // set the date of the sixth day of next week
            JSONObject detailData6 = (JSONObject) daylyDataArray.get(6);
            TextView textViewDate6 = (TextView) view.findViewById(R.id.next_7_days_6_date);
            textViewDate6.setText(this.changeDateFormat(detailData6.optString("time"), detailData6.optString("timezone")));
            ImageView imageView6 = (ImageView) view.findViewById(R.id.next_7_days_6_icon);
            iconId = res.getIdentifier(iconMap.get(detailData6.optString("icon")), "drawable", getActivity().getPackageName());
            imageView6.setImageResource(iconId);
            TextView textViewTemp6 = (TextView) view.findViewById(R.id.next_7_days_6_temp);
            textViewTemp6.setText("Min:" + Math.round(Double.valueOf(detailData6.optString("temperatureMin")))
                    + temperatureUnit + " | " + "Max:"
                    + Math.round(Double.valueOf(detailData6.optString("temperatureMax"))) + temperatureUnit);

            // set the date of the seventh day of next week
            JSONObject detailData7 = (JSONObject) daylyDataArray.get(7);
            TextView textViewDate7 = (TextView) view.findViewById(R.id.next_7_days_7_date);
            textViewDate7.setText(this.changeDateFormat(detailData7.optString("time"), detailData7.optString("timezone")));
            ImageView imageView7 = (ImageView) view.findViewById(R.id.next_7_days_7_icon);
            iconId = res.getIdentifier(iconMap.get(detailData7.optString("icon")), "drawable", getActivity().getPackageName());
            imageView7.setImageResource(iconId);
            TextView textViewTemp7 = (TextView) view.findViewById(R.id.next_7_days_7_temp);
            textViewTemp7.setText("Min:" + Math.round(Double.valueOf(detailData7.optString("temperatureMin")))
                    + temperatureUnit + " | " + "Max:"
                    + Math.round(Double.valueOf(detailData7.optString("temperatureMax"))) + temperatureUnit);

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return view;
    }

    public String changeDateFormat(String strTime, String strTimeZone) {
        TimeZone timezone = TimeZone.getTimeZone(strTimeZone);
        Date date = new Date((long) Long.valueOf(strTime) * 1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd", Locale.ENGLISH);
        dateFormat.setTimeZone(timezone);
        return dateFormat.format(date);
    }
}
