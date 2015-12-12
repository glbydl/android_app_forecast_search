package com.example.wangf.forecastsearch;

import com.example.wangf.forecastsearch.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.hamweather.aeris.communication.AerisEngine;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class MainActivity extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    public static String street_bk;
    public static String city_bk;
    public static String state_bk;
    public static String degree_bk;

    private EditText condition_street;
    private EditText condition_city;
    private Spinner condition_state;
    private RadioButton condition_degreeF;
    private RadioButton condition_degreeC;
    private TextView validation;
    private ImageView forecast_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forecast_search);

        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView = findViewById(R.id.fullscreen_content);

        condition_street = (EditText) findViewById(R.id.condition_street);
        condition_city = (EditText) findViewById(R.id.condition_city);
        condition_state = (Spinner) findViewById(R.id.condition_state);
        condition_degreeF = (RadioButton) findViewById(R.id.condition_degreeF);
        condition_degreeC = (RadioButton) findViewById(R.id.condition_degreeC);
        validation = (TextView) findViewById(R.id.validation);
        forecast_logo = (ImageView) findViewById(R.id.forecast_logo);


        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    // Cached values.
                    int mControlsHeight;
                    int mShortAnimTime;

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                            // If the ViewPropertyAnimator API is available
                            // (Honeycomb MR2 and later), use it to animate the
                            // in-layout UI controls at the bottom of the
                            // screen.
                            if (mControlsHeight == 0) {
                                mControlsHeight = controlsView.getHeight();
                            }
                            if (mShortAnimTime == 0) {
                                mShortAnimTime = getResources().getInteger(
                                        android.R.integer.config_shortAnimTime);
                            }
                            controlsView.animate()
                                    .translationY(visible ? 0 : mControlsHeight)
                                    .setDuration(mShortAnimTime);
                        } else {
                            // If the ViewPropertyAnimator APIs aren't
                            // available, simply show or hide the in-layout UI
                            // controls.
                            controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
                        }

                        if (visible && AUTO_HIDE) {
                            // Schedule a hide().
                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
                        }
                    }
                });

        // Set up the user interaction to manually show or hide the system UI.
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TOGGLE_ON_CLICK) {
                    mSystemUiHider.toggle();
                } else {
                    mSystemUiHider.show();
                }
            }
        });

        // Search button click event
        final Button button_search = (Button) findViewById(R.id.button_search);
        button_search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // validate input value
                if (!inputValidation()) {
                    return;
                }

                // backup the search conditions
                backupCondition();

                // create the request url
                URL url = createRequestUrl();

                if (url != null) {
                    // execute the Async Task
                    DownloadFilesTask myAsyncTask = new DownloadFilesTask(getApplicationContext());
                    myAsyncTask.execute(url);
                }
            }
        });

        // Clear button click event, clear search condition
        final Button button_clear = (Button) findViewById(R.id.button_clear);
        button_clear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                condition_street.setText("");
                condition_city.setText("");
                condition_state.setSelection(0);
                condition_degreeF.setChecked(true);
                condition_degreeC.setChecked(false);
                validation.setText("");
            }
        });

        // About button click event, move to AboutActivity
        final Button button_about = (Button) findViewById(R.id.button_about);
        button_about.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        // forecast logo click event
        forecast_logo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://forecast.io"));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    /**
     * validate the input value
     * @return true: right  false: wrong
     */
    private boolean inputValidation() {
        validation.setText("");

        if (condition_street.getText().toString().equals("")) {
            validation.setText("Please enter a Street Address.");
            return false;
        }

        if (condition_city.getText().toString().equals("")) {
            validation.setText("Please enter a City");
            return false;
        }

        if (condition_state.getSelectedItemPosition() == 0) {
            validation.setText("Please select a State");
            return false;
        }

        return true;
    }

    /**
     * create request url for async task, go fetch json file
     * @return request url
     */
    private URL createRequestUrl() {
        String street = condition_street.getText().toString().replace(" ", "%20");
        String city = condition_city.getText().toString().replace(" ", "%20");

        StringBuilder strUrl = new StringBuilder();
        strUrl.append("http://firstapp0811-env.elasticbeanstalk.com/forecast.php?");
        strUrl.append("streetAddress=" + street + "&");
        strUrl.append("city=" + city + "&");
        strUrl.append("state=" + condition_state.getSelectedItem().toString() + "&");
        strUrl.append("degree=" + (condition_degreeF.isChecked() ? "1" : "2"));
        URL url = null;
        try {
            url = new URL(strUrl.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return url;
        }
    }

    /**
     * back up the search conditions
     */
    private void backupCondition() {
        street_bk = condition_street.getText().toString();
        city_bk = condition_city.getText().toString();
        state_bk = condition_state.getSelectedItem().toString();
        degree_bk = condition_degreeF.isChecked() ? "us" : "si";
    }
}
