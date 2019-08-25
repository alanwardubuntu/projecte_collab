package com.example.projectecollab;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.media.MediaDrm;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class GraphicsActivity extends AppCompatActivity {

    private CanvasView canvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphics);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String title = intent.getStringExtra(MainActivity.INTENT_TITLE);
        String url_s = intent.getStringExtra(MainActivity.INTENT_URL);

        setTitle(title);
        canvas = findViewById(R.id.view);

        DownloadGraphicData downloadgrdata = new DownloadGraphicData();
        downloadgrdata.setGraphicActivity(GraphicsActivity.this);
        //downloadgrdata.setCanvas(canvas);
        downloadgrdata.setCallback(new OnEventListener() {
            @Override
            public void onSuccess(List<Double> x, List<Double> y,
                                  String firstDate, String lastDate) {

                canvas.setData(x, y, firstDate, lastDate);
                canvas.invalidate();
            }

            @Override
            public void onFailure(String error) {
                // do nothing, do not refresh graphics
                Log.println(0, error, error);
            }
        });
        downloadgrdata.execute(url_s);
    }
}


class DownloadGraphicData extends AsyncTask<String, Void, Void> {

    private OnEventListener callback;
    private GraphicsActivity graphicActivity = null;
    private String Error = null;
    private ProgressDialog dialog = null;
    private List<Double> x, y;
    private String firstDate = null, lastDate = null;


    public void setCallback(OnEventListener callback) {

        this.callback = callback;
    }

    public void setGraphicActivity (GraphicsActivity activity) {

        this.graphicActivity = activity;
    }

    @Override
    protected void onPreExecute() {

        dialog = new ProgressDialog(this.graphicActivity);

        dialog.setMessage("Downloading CSV file...");
        dialog.show();

        super.onPreExecute();
    }

    protected Void doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);

            x = new ArrayList<Double>();
            y = new ArrayList<Double>();

            try {
                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                httpConn.setRequestMethod("GET");

                InputStream inputStream = httpConn.getInputStream();
                int ch;
                StringBuilder sb = new StringBuilder();
                while ((ch = inputStream.read()) != -1)
                    sb.append((char) ch);
                String data = sb.toString();

                httpConn.disconnect();

                firstDate = null;
                lastDate = null;

                double xi = 0.0;
                BufferedReader br = new BufferedReader(new StringReader(data));
                String line;
                while ((line = br.readLine()) != null) {
                    String[] row = line.split(",");
                    String dia = row[0],
                            mes = row[1],
                            an = row[2],
                            hora = row[3],
                            minut = row[4],
                            segon = row[5],
                            datum = row[6];

                    lastDate = dia+"/"+mes+"/"+an+" "+hora+":"+minut;
                    if (firstDate == null)
                        firstDate = lastDate;

                    x.add(new Double(xi));
                    xi += 1.0 / 24.0 / 6.0;
                    y.add(new Double(datum));
                }


                int limit = 7 * 24 * 6;
                while (x.size() > limit){
                    x.remove(0);
                    y.remove(0);
                }

            } catch (IOException e) {
                Error = "IOException";
                Log.println(1, "Error", Error);
            }
        } catch (MalformedURLException e) {
            Error = "MalformedURLException";
            Log.println(1, "Error", Error);
        }

        return null;
    }

    protected void onPostExecute(Void unused) {

        if (Error != null) {
            Log.d(Error, Error);
            callback.onFailure(Error);
        } else {
            dialog.dismiss();
            callback.onSuccess(x, y, firstDate, lastDate);
        }
    }
}

