package com.example.wangf.forecastsearch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/**
 * Created by wangf on 2015/11/16.
 */
public class ResultActivity extends Activity {

    public static JSONObject jsonResult;

    public String icon_bk;
    public String summary_bk;

    private String temperatureUnit;
    private String windSpeedUnit;
    private String dewPointUnit;
    private String visibilityUnit;
    private String pressureUnit;

    private TextView resultTemperature;
    private TextView resultHighLow;
    private ImageView resultIcon;
    private TextView resultSummary;
    private TextView resultPrecipitation;
    private TextView resultChanceOfRain;
    private TextView resultWindSpeed;
    private TextView resultDewPoint;
    private TextView resultHumidity;
    private TextView resultVisibility;
    private TextView resultSunrise;
    private TextView resultSunset;
    private ImageView facebook_logo;

    private CallbackManager callbackManager;
    private ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_result);
        Intent intent = getIntent();

        try {
            jsonResult = new JSONObject(intent.getStringExtra("jsonResult"));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        resultTemperature = (TextView) findViewById(R.id.temperature);
        resultChanceOfRain = (TextView) findViewById(R.id.table_result_chance_of_rain);
        resultPrecipitation = (TextView) findViewById(R.id.table_result_precipitation);
        resultDewPoint = (TextView) findViewById(R.id.table_result_dew_point);
        resultWindSpeed = (TextView) findViewById(R.id.table_result_wind_speed);
        resultHighLow = (TextView) findViewById(R.id.high_low);
        resultHumidity = (TextView) findViewById(R.id.table_result_humidity);
        resultIcon = (ImageView) findViewById(R.id.image_result_weather_icon);
        resultSummary = (TextView) findViewById(R.id.summary);
        resultSunrise = (TextView) findViewById(R.id.table_result_sunrise);
        resultSunset = (TextView) findViewById(R.id.table_result_sunset);
        resultVisibility = (TextView) findViewById(R.id.table_result_visibility);
        facebook_logo = (ImageView) findViewById(R.id.image_fb_icon);

        JSONObject currentlyObj = jsonResult.optJSONObject("currently");
        JSONObject dailyObj = jsonResult.optJSONObject("daily");
        JSONArray dailyDataArray = dailyObj.optJSONArray("data");
        JSONObject dailyDataObj;
        try {
            dailyDataObj = (JSONObject) dailyDataArray.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // create weather icon map
        HashMap<String, String> iconMap = createWeatherIconMap();

        // set forecast metric unit
        setValueUnit(MainActivity.degree_bk);

        // get precipitation word
        String precipitationWord = getPrecipitationWord(currentlyObj.optString("precipIntensity"));

        Resources res = getResources();

        icon_bk = iconMap.get(currentlyObj.optString("icon"));
        int iconId = res.getIdentifier(icon_bk, "drawable", getPackageName());
        resultIcon.setImageResource(iconId);

        summary_bk = currentlyObj.optString("summary");
        resultSummary.setText(summary_bk + " in "
                + MainActivity.city_bk + ", " + MainActivity.state_bk);
        resultTemperature.setText(Math.round(Double.valueOf(currentlyObj.optString("temperature")))
                + temperatureUnit);
        resultHighLow.setText("L" + Math.round(Double.valueOf(dailyDataObj.optString("temperatureMin")))
                + "° | H" + Math.round(Double.valueOf(dailyDataObj.optString("temperatureMax"))) + "°");
        resultPrecipitation.setText(precipitationWord);
        resultChanceOfRain.setText(Math.round(Double.valueOf(currentlyObj.optString("precipProbability"))*100) + "%");
        resultWindSpeed.setText(currentlyObj.optString("windSpeed") + windSpeedUnit);
        resultDewPoint.setText(currentlyObj.optString("dewPoint") + dewPointUnit);
        resultHumidity.setText(Math.round(Double.valueOf(currentlyObj.optString("humidity")) * 100) + "%");
        resultVisibility.setText(currentlyObj.optString("visibility") + visibilityUnit);
        resultSunrise.setText(changeDateFormat(dailyDataObj.optString("sunriseTime"), jsonResult.optString("timezone")));
        resultSunset.setText(changeDateFormat(dailyDataObj.optString("sunsetTime"), jsonResult.optString("timezone")));

        // More details button click event
        final Button button_more_details = (Button) findViewById(R.id.button_more_details);
        button_more_details.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, DetailsActivity.class);
                startActivity(intent);
            }
        });

        // View maps button click event
        final Button button_view_maps = (Button) findViewById(R.id.button_view_maps);
        button_view_maps.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        // facebook logo click event
        facebook_logo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FacebookShare();
            }
        });
    }

    public void FacebookShare() {
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        LoginManager.getInstance().logOut();
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                if (result.getPostId() != null) {
                    showToast("You shared this post.");
                    showToast("Facebook Post Successful");
                } else {
                    showToast("Posted Cancelled");
                }
            }

            @Override
            public void onCancel() {
                // nothing here
            }

            @Override
            public void onError(FacebookException error) {
                showToast(error.getCause().toString());
            }
        });
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("Current Weather in " + MainActivity.city_bk + ", " + MainActivity.state_bk)
                    .setContentDescription(summary_bk + ", " + resultTemperature.getText().toString())
                    .setContentUrl(Uri.parse("http://forecast.io"))
                    .setImageUrl(Uri.parse("http://cs-server.usc.edu:45678/hw/hw8/images/" + icon_bk + ".png"))
                    .build();

            shareDialog.show(linkContent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void showBottomMessage(String messsage) {
        Context context = getApplicationContext();
        CharSequence text = messsage;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    /**
     * create weather icon map
     * @return weather icon map
     */
    public HashMap<String, String> createWeatherIconMap() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("clear-day", "clear");
        map.put("clear-night", "clear_night");
        map.put("rain", "rain");
        map.put("snow", "snow");
        map.put("sleet", "sleet");
        map.put("wind", "wind");
        map.put("fog", "fog");
        map.put("cloudy", "cloudy");
        map.put("partly-cloudy-day", "cloud_day");
        map.put("partly-cloudy-night", "cloud_night");
        return map;
    }

    /**
     * get the precipition word
     * @param strValue precipition value
     * @return precipition word
     */
    public String getPrecipitationWord(String strValue) {
        try {
            Double value = Double.valueOf(strValue);
            if (value >= 0 && value < 0.002) {
                return "None";
            }
            if (value >= 0.002 && value < 0.017) {
                return "Very Light";
            }
            if (value >= 0.017 && value < 0.1) {
                return "Light";
            }
            if (value >= 0.1 && value < 0.4) {
                return "Moderate";
            }
            if (value >= 0.4) {
                return "Heavy";
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * set Forecast metric unit
     * @param strUnit degree of search condition
     */
    public void setValueUnit(String strUnit) {
        if (strUnit.equals("us")) {
            temperatureUnit = "°F";
            windSpeedUnit = "mph";
            dewPointUnit = "°F";
            visibilityUnit = "mi";
            pressureUnit = "mb";
        } else {
            temperatureUnit = "°C";
            windSpeedUnit = "m/s";
            dewPointUnit = "°C";
            visibilityUnit = "km";
            pressureUnit = "hPa";
        }
    }

    public String changeDateFormat(String strTime, String strTimeZone) {
        TimeZone timezone = TimeZone.getTimeZone(strTimeZone);
        Date date = new Date((long) Long.valueOf(strTime) * 1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        dateFormat.setTimeZone(timezone);
        return dateFormat.format(date);
    }

    public void showToast(String message) {
        Context context = getApplicationContext();
        CharSequence text = message;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
