package com.smartdata.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.smartdata.mydaily.R;
import com.smartdata.utils.Constants;
import com.smartdata.utils.Utility;


/**
 * Created by Anurag Sethi
 * The activity is used for handling the login screen actions
 */
public class MapViewActivity extends AppActivity implements OnMapReadyCallback {
    private static final String TAG = MapViewActivity.class.getSimpleName();
    private Utility mUtility;
    private GoogleMap mGoogleMap;
    private LatLng mLatLng;

    private String mLatitude, mLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        initData();
        initMapIfNecessary();
        bindControls();

    }


    /**
     * Default method of activity life cycle to handle the actions required once the activity starts
     * checks if the network is available or not
     *
     * @return none
     */

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();


    }

    /**
     * Default activity life cycle method
     */
    @Override
    public void onStop() {
        super.onStop();


    }


    /**
     * Initializes the objects
     *
     * @return none
     */
    @Override
    public void initData() {
        mUtility = new Utility(MapViewActivity.this);
        mUtility.customActionBar(MapViewActivity.this, true, R.string.daily_location);
        mLatitude = getIntent().getStringExtra(Constants.InputTag.LATITUDE);
        mLongitude = getIntent().getStringExtra(Constants.InputTag.LONGITUDE);
    }

    /**
     * Binds the UI controls
     *
     * @return none
     */
    @Override
    public void bindControls() {

        mLatLng = new LatLng(Double.parseDouble(mLatitude), Double.parseDouble(mLongitude));
        initCamera();
    }


    /**
     * The method handles the result from the Facebook
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void initMapIfNecessary() {
        if (mGoogleMap != null) {
            return;
        }
        //mGoogleMap = ( (MapFragment) getFragmentManager().findFragmentById( R.id.map ) ).getMap();
        SupportMapFragment mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        initMapSettings();
        mLatLng = new LatLng(Double.parseDouble(mLatitude), Double.parseDouble(mLongitude));
        initCamera();
    }

    private void initMapSettings() {
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        //mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        if (ContextCompat.checkSelfPermission(MapViewActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //  mGoogleMap.setMyLocationEnabled(true);
        }
    }

    private void initCamera() {
        if (mLatLng != null) {
            CameraPosition position = CameraPosition.builder()
                    .target(mLatLng)
                    .zoom(getInitialMapZoomLevel())
                    .build();

            MarkerOptions options = new MarkerOptions();
            options.position(mLatLng);

            if (mGoogleMap != null) {
                // Adding the marker in the Google Map
                mGoogleMap.addMarker(options);
                //mGoogleMap.addMarker(new MarkerOptions().flat(true)
                //.anchor(0.5f, 0.5f).position(mLatLng));
                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), null);
            }
        }

    }


    private float getInitialMapZoomLevel() {
        return 17.0f;
    }
}
