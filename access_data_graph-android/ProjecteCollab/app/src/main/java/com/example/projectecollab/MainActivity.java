package com.example.projectecollab;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> itemList;
    ArrayAdapter<String> spinnerAdapter;

    public static final String INTENT_TITLE = "com.example.projecte_collab.TITLE";
    public static final String INTENT_URL = "com.example.projecte_collab.URL";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* create button to close application */
        android.widget.Button tancar = findViewById(R.id.button2);
        tancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });

        /* create button to open About dialog */
        android.widget.Button aproposit = findViewById(R.id.button3);
        aproposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Projecte Collab és un projecte que recull i tracta " +
                        "dades ambients dins un edifici (temperatura i altres). Podeu " +
                        "trobar més informació en el nostre repositori\n\n " +
                        "http://github.com/alanwardubuntu/projecte_collab\n\n" +
                        "Programació d'aquesta app: Alan Ward (C) 2019");
                builder.setTitle("A propòsit de...");

                builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do nothing, just close dialog
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        /* populate Spinner with current list of data files */
        final android.widget.Spinner spinner = findViewById(R.id.spinner);
        itemList = new ArrayList<String>();

        spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, itemList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        /* create button to open data source with focus in spinner */
        android.widget.Button obrir = findViewById(R.id.button);
        obrir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = String.valueOf(spinner.getSelectedItem());
                URL url = null;

                try {
                    url = new URL("https://raw.githubusercontent.com/alanwardubuntu/projecte_collab_dades/master/data/" +
                            title);

                    Intent intent = new Intent(MainActivity.this, GraphicsActivity.class);
                    intent.putExtra(INTENT_TITLE, title);
                    intent.putExtra(INTENT_URL, url.toString());

                    startActivity(intent);

                } catch (MalformedURLException e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Internal error: malformed URL " + url);
                    builder.setTitle("Error obrint dades");

                    builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // do nothing, just close dialog
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        String url = getString(R.string.github_address);
        DownloadData downloaddata = new DownloadData();
        downloaddata.setList(itemList);
        downloaddata.setAdapter(spinnerAdapter);
        downloaddata.setMainActivity(MainActivity.this);
        downloaddata.execute(url);
        super.onStart();
    }
}


class DownloadData extends AsyncTask<String, Void, Void> {

    private List itemList = null;
    private ArrayAdapter adapter = null;
    private Activity mainActivity = null;
    private String Error = null;
    private ProgressDialog dialog = null;

    public void setList(List list) {

        this.itemList = list;
    }

    public void setAdapter(ArrayAdapter a) {

        this.adapter = a;
    }

    public void setMainActivity (Activity activity) {

        this.mainActivity = activity;
    }

    @Override
    protected void onPreExecute() {

        dialog = new ProgressDialog(this.mainActivity);

        dialog.setMessage("Downloading data list...");
        dialog.show();

        super.onPreExecute();
    }

    protected Void doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);
            HttpURLConnection httpConn = (HttpURLConnection)url.openConnection();
            httpConn.setRequestMethod("GET");

            InputStream inputStream = httpConn.getInputStream();
            int ch;
            StringBuilder sb = new StringBuilder();
            while((ch = inputStream.read()) != -1)
                sb.append((char)ch);
            String line = sb.toString();

            try {

                // clear list of previous elements, if any
                itemList.clear();

                // read in new elements from JSON structure
                // reported by Github
                JSONArray files = new JSONArray(line);

                for (int i = 0; i < files.length(); i++) {
                    JSONObject f = files.getJSONObject(i);
                    String name = f.getString("name");
                    this.itemList.add(name);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            httpConn.disconnect();
        } catch (IOException e) {
            Error = e.getMessage();
            cancel(true);
        }

        return null;
    }

    protected void onPostExecute(Void unused) {

        if (Error != null) {
            Log.d(Error, Error);
        } else {
            dialog.dismiss();
            this.adapter.notifyDataSetChanged();
        }
    }
}

