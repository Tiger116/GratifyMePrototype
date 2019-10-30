package student.arcadia.com.gratyfime;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity implements RecyclerView.OnItemTouchListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Record> records;
    private Download download;
    private File jsonFile;
    private GestureDetectorCompat detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        records = new ArrayList<>();

        detector = new GestureDetectorCompat(MainActivity.this, new RecyclerViewOnGestureListener());

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.addOnItemTouchListener(this);

        mAdapter = new RecyclerViewAdapter(records, MainActivity.this);
        mRecyclerView.setAdapter(mAdapter);

        download = new Download();
        download.execute(getString(R.string.jsonURL));
    }

    private void populateRecords() {
        FileReader reader = null;
        if (jsonFile.exists()) {
            try {
                reader = new FileReader(jsonFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            JsonReader jsonReader = new JsonReader(reader);
            Type type = new TypeToken<List<Record>>() {
            }.getType();
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            records = gson.fromJson(jsonReader, type);

            mAdapter = new RecyclerViewAdapter(records, MainActivity.this);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        detector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    public class Download extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setTitle("Updating offers. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(true);
            pDialog.show();
            pDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(final DialogInterface arg0) {
                    if (download != null)
                        download.cancel(true);
                }
            });
        }

        @Override
        protected String doInBackground(String... urls) {
            jsonFile = new File(getFilesDir(), "offers.json");
            if (!jsonFile.exists()) {
                InputStream stream = null;
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(jsonFile);
                    URL url = new URL(urls[0]);
                    URLConnection connection = url.openConnection();
                    connection.connect();
                    int lenghtOfFile = connection.getContentLength();
                    stream = new BufferedInputStream(url.openStream());
                    int length = -1, total = 0;
                    byte[] buffer = new byte[1024];
                    while ((length = stream.read(buffer)) > -1) {
                        total += length;
                        publishProgress(String.format("%d", (total * 100) / lenghtOfFile));
                        fos.write(buffer, 0, length);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... params) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(params[0]));
        }

        @Override
        protected void onPostExecute(String result) {
            populateRecords();
            pDialog.dismiss();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if (jsonFile != null)
                jsonFile.delete();
        }
    }

    private class RecyclerViewOnGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            View view = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            int position = mRecyclerView.getChildPosition(view);
            createIntent(position);
            return super.onSingleTapConfirmed(e);
        }

        public void onLongPress(MotionEvent e) {
            View view = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            int position = mRecyclerView.getChildPosition(view);
            super.onLongPress(e);
            createIntent(position);
        }

        private void createIntent(int position) {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            Gson gson = new Gson();
            intent.putExtra("json", gson.toJson(records.get(position)));
            startActivity(intent);
        }
    }
}
