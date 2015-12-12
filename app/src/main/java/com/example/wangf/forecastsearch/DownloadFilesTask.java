package com.example.wangf.forecastsearch;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by wangf on 2015/11/16.
 */
public class DownloadFilesTask extends AsyncTask<URL, Integer, String> {

    private Context context;

    public DownloadFilesTask(Context context) {
        this.context = context;
    }

    protected String doInBackground(URL... urls) {
        URL url = urls[0];
        System.out.println(url.toString());

        HttpURLConnection urlConnection = null;
        StringBuilder responseStrBuilder = new StringBuilder();
        try {
            //URL url = new URL("http://www-scf.usc.edu/~wang787/Test/jsondata2.json");
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String inputStr = IOUtils.toString(in, "UTF-8");
            if (inputStr != null) {
                responseStrBuilder.append(inputStr);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
            return responseStrBuilder.toString();
        }
    }

    protected void onPostExecute(String result) {
        if (result.equals("") || result.equals(null)) {
            return;
        }

        try {
            Intent intent = new Intent(context, ResultActivity.class);
            intent.putExtra("jsonResult", result);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}