package student.arcadia.com.gratyfime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;


public class MapActivity extends ActionBarActivity implements OnMapReadyCallback {
    private double latitude, longitude;
    MapFragment mapView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Gson gson = new Gson();
        Record record = gson.fromJson(bundle.getString("json"), Record.class);

        setTitle(record.getBusiness_id());

        latitude = record.getLocation()[0];
        longitude = record.getLocation()[1];
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext()) == ConnectionResult.SUCCESS) {
            mapView = (MapFragment) getFragmentManager().findFragmentById(R.id.mapView);
            mapView.getMapAsync(this);
            mapView.getMap().addMarker(new MarkerOptions().title(record.getBusiness_id()).snippet(record.getAddress()).position(new LatLng(latitude, longitude)));
        }
        TableLayout tableLayout = (TableLayout) findViewById(R.id.table);

        TextView addressView = (TextView) findViewById(R.id.company_address);
        TableRow addressRow = (TableRow) findViewById(R.id.address_row);
        if (record.getAddress() != null && record.getAddress().isEmpty()) {
            tableLayout.removeView(addressRow);
        } else {
            addressView.setText(record.getAddress());
        }

        TextView telView = (TextView) findViewById(R.id.company_tel);
        TableRow telRow = (TableRow) findViewById(R.id.tel_row);
        if (record.getTel() != null && record.getTel().isEmpty()) {
            tableLayout.removeView(telRow);
        } else {
            telView.setText(record.getTel());
        }

        TextView websiteView = (TextView) findViewById(R.id.company_website);
        TableRow websiteRow = (TableRow) findViewById(R.id.website_row);
        if (record.getWebsite() != null && record.getWebsite().isEmpty()) {
            tableLayout.removeView(websiteRow);
        } else {
            websiteView.setText(record.getWebsite());
        }

        TextView emailView = (TextView) findViewById(R.id.company_email);
        TableRow emailRow = (TableRow) findViewById(R.id.email_row);
        if (record.getEmail() != null && record.getEmail().isEmpty()) {
            tableLayout.removeView(emailRow);
        } else {
            emailView.setText(record.getEmail());
        }

        TextView twitterView = (TextView) findViewById(R.id.company_twitter);
        TableRow twitterRow = (TableRow) findViewById(R.id.twitter_row);
        if (record.getTwitter() != null && record.getTwitter().isEmpty()) {
            tableLayout.removeView(twitterRow);
        } else {
            twitterView.setText(record.getTwitter());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_maps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = NavUtils.getParentActivityIntent(this);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                NavUtils.navigateUpTo(this, intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(latitude, longitude), 15));
        map.setMyLocationEnabled(true);
    }
}
