package com.example.wangf.forecastsearch;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by wangf on 2015/12/9.
 */
public class Next24HoursFragment extends Fragment {

    int plusButtonId = View.generateViewId();
    private String temperatureUnit;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_next_24_hours, container, false);

        TableLayout tableLayout = (TableLayout) view.findViewById(R.id.data_next_24_hours);

        ResultActivity resultActivity = new ResultActivity();
        DetailsActivity detailsActivity = new DetailsActivity();

        // create weather icon map
        HashMap<String, String> iconMap = detailsActivity.createWeatherIconMap();

        Resources res = getResources();

        JSONArray hourlyDataArray = ResultActivity
                .jsonResult
                .optJSONObject("hourly")
                .optJSONArray("data");

        if (MainActivity.degree_bk.equals("us")) {
            temperatureUnit = "°F";
        } else {
            temperatureUnit = "°C";
        }

        TableRow headerRow = new TableRow(getActivity());
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        lp.setMargins(0, 0, 0, 2);
        headerRow.setLayoutParams(lp);
        headerRow.setBackgroundColor(getResources().getColor(R.color.lightBlue));

        TextView headerTime = new TextView(getActivity());
        headerTime.setText("Time");
        headerTime.setTextColor(getResources().getColor(R.color.black));
        headerTime.setTextSize(20);

        TextView headerSummary = new TextView(getActivity());
        headerSummary.setText("Summary");
        headerSummary.setTextColor(getResources().getColor(R.color.black));
        headerSummary.setTextSize(20);
        headerSummary.setGravity(Gravity.CENTER);

        TextView headerTemp = new TextView(getActivity());
        headerTemp.setText("Temp(" + temperatureUnit + ")");
        headerTemp.setTextColor(getResources().getColor(R.color.black));
        headerTemp.setTextSize(20);
        headerTemp.setGravity(Gravity.RIGHT);

        headerRow.addView(headerTime);
        headerRow.addView(headerSummary);
        headerRow.addView(headerTemp);
        tableLayout.addView(headerRow);
        //hourlyDataArray.length()
        int i = 0;
        try {

            for(i = 0; i < hourlyDataArray.length() / 2; i++) {
                JSONObject detailData = (JSONObject) hourlyDataArray.get(i);

                TableRow tableRow = new TableRow(getActivity());
                tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                if (i % 2 == 0) {
                    tableRow.setBackgroundColor(getResources().getColor(R.color.superGray));
                } else {
                    tableRow.setBackgroundColor(getResources().getColor(R.color.midPink));
                }

                TextView textViewTime = new TextView(getActivity());
                textViewTime.setText(resultActivity.changeDateFormat(detailData.optString("time"), ResultActivity.jsonResult.optString("timezone")));
                textViewTime.setTextColor(getResources().getColor(R.color.black));
                textViewTime.setTextSize(20);

                int iconId = res.getIdentifier(iconMap.get(detailData.optString("icon")), "drawable", getActivity().getPackageName());
                ImageView imageViewSummary = new ImageView(getActivity());
                imageViewSummary.setImageResource(iconId);
                imageViewSummary.setScaleType(ImageView.ScaleType.CENTER);

                TextView textViewTemp = new TextView(getActivity());
                textViewTemp.setText(String.valueOf(Math.round(Double.valueOf(detailData.optString("temperature")))));
                textViewTemp.setTextColor(getResources().getColor(R.color.black));
                textViewTemp.setTextSize(20);
                textViewTemp.setGravity(Gravity.RIGHT);

                tableRow.addView(textViewTime);
                tableRow.addView(imageViewSummary);
                tableRow.addView(textViewTemp);
                tableLayout.addView(tableRow);
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        TableRow tableRowPlus = new TableRow(getActivity());
        tableRowPlus.setId(plusButtonId);
        tableRowPlus.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        if (i % 2 == 0) {
            tableRowPlus.setBackgroundColor(getResources().getColor(R.color.superGray));
        } else {
            tableRowPlus.setBackgroundColor(getResources().getColor(R.color.midPink));
        }

        TextView textViewExtra1 = new TextView(getActivity());
        TextView textViewExtra2 = new TextView(getActivity());
        textViewExtra2.setGravity(Gravity.LEFT);

        int plus = res.getIdentifier("plus", "drawable", getActivity().getPackageName());
        ImageView plusButton = new ImageView(getActivity());
        plusButton.setImageResource(plus);
        plusButton.setScaleType(ImageView.ScaleType.CENTER);

        tableRowPlus.addView(textViewExtra1);
        tableRowPlus.addView(plusButton);
        tableRowPlus.addView(textViewExtra2);

        tableLayout.addView(tableRowPlus);

        plusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createNext24Hours();
            }
        });

        //Inflate the layout for this fragment

        return view;
    }

    public void createNext24Hours() {
        TableLayout tableLayout = (TableLayout) getView().findViewById(R.id.data_next_24_hours);

        ResultActivity resultActivity = new ResultActivity();
        DetailsActivity detailsActivity = new DetailsActivity();

        // create weather icon map
        HashMap<String, String> iconMap = detailsActivity.createWeatherIconMap();

        Resources res = getResources();

        JSONArray hourlyDataArray = ResultActivity
                .jsonResult
                .optJSONObject("hourly")
                .optJSONArray("data");
        TableRow iv = (TableRow) getActivity().findViewById(plusButtonId);
        tableLayout.removeView(iv);
        int i = 0;
        try {

            for(i = hourlyDataArray.length() / 2; i < hourlyDataArray.length(); i++) {
                JSONObject detailData = (JSONObject) hourlyDataArray.get(i);

                TableRow tableRow = new TableRow(getActivity());
                tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                if (i % 2 == 0) {
                    tableRow.setBackgroundColor(getResources().getColor(R.color.superGray));
                } else {
                    tableRow.setBackgroundColor(getResources().getColor(R.color.midPink));
                }

                TextView textViewTime = new TextView(getActivity());
                textViewTime.setText(resultActivity.changeDateFormat(detailData.optString("time"), ResultActivity.jsonResult.optString("timezone")));
                textViewTime.setTextColor(getResources().getColor(R.color.black));
                textViewTime.setTextSize(20);

                int iconId = res.getIdentifier(iconMap.get(detailData.optString("icon")), "drawable", getActivity().getPackageName());
                ImageView imageViewSummary = new ImageView(getActivity());
                imageViewSummary.setImageResource(iconId);
                imageViewSummary.setScaleType(ImageView.ScaleType.CENTER);

                TextView textViewTemp = new TextView(getActivity());
                textViewTemp.setText(String.valueOf(Math.round(Double.valueOf(detailData.optString("temperature")))));
                textViewTemp.setTextColor(getResources().getColor(R.color.black));
                textViewTemp.setTextSize(20);
                textViewTemp.setGravity(Gravity.RIGHT);

                tableRow.addView(textViewTime);
                tableRow.addView(imageViewSummary);
                tableRow.addView(textViewTemp);
                tableLayout.addView(tableRow);
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
