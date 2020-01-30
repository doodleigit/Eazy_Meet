package net.doodlei.android.eazymeet.shareLocation.view;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import net.doodlei.android.eazymeet.R;
import net.doodlei.android.eazymeet.home.model.InviteLocation;
import net.doodlei.android.eazymeet.shareLocation.model.ConfirmContact;
import net.doodlei.android.eazymeet.shareLocation.model.ContactLocation;
import net.doodlei.android.eazymeet.shareLocation.service.InviteCurrentLocationService;
import net.doodlei.android.eazymeet.utils.PreferenceManager;
import net.doodlei.android.eazymeet.utils.SocketIOManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private boolean mLocationPermissionGranted;
    private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 5465;

    private InviteCurrentLocationService inviteCurrentLocationService;

    private InviteLocation invitedLocation;
    private ArrayList<ContactLocation> contactLocations;
    private String userId, invitationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initialComponent();
        showGpsSetting();
    }

    private void initialComponent() {
        contactLocations = new ArrayList<>();
        invitedLocation = getIntent().getParcelableExtra("invited_location");
        invitationId = getIntent().getStringExtra("invitation_id");
        userId = PreferenceManager.newInstance(getApplicationContext()).getID();
        inviteCurrentLocationService = InviteCurrentLocationService.retrofit.create(InviteCurrentLocationService.class);

        SocketIOManager.newInstance().socket.on(invitationId + "_invited_map", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject jsonObject = new JSONObject(args[0].toString());
                    String userId, invitationId;
                    double latitude, longitude;

                    userId = jsonObject.getString("user_id");
//                    invitationId = jsonObject.getString("invitation");
                    latitude = jsonObject.getDouble("latitude");
                    longitude = jsonObject.getDouble("longitude");

                    for (int i = 0; i < contactLocations.size(); i++) {
                        if (contactLocations.get(i).getConfirmContact().getId().equals(userId)) {
                            contactLocations.get(i).setLatitude(latitude);
                            contactLocations.get(i).setLongitude(longitude);
                            setInvitedLocationMarker(contactLocations.get(i));
                            break;
                        }
                    }
                } catch (JSONException ignored) {}
            }
        });
    }

    private void showGpsSetting() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            LocationRequest mLocationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(10 * 1000)
                    .setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder settingsBuilder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(mLocationRequest);
            settingsBuilder.setAlwaysShow(true);

            Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(this)
                    .checkLocationSettings(settingsBuilder.build());
            result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
                @Override
                public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                    try {
                        LocationSettingsResponse response =
                                task.getResult(ApiException.class);
                    } catch (ApiException exception) {
                        if (exception.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {// Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.
                            try {
                                // Cast to a resolvable exception.
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult(
                                        MapsActivity.this,
                                        LocationRequest.PRIORITY_HIGH_ACCURACY);
                            } catch (IntentSender.SendIntentException | ClassCastException e) {
                                // Ignore the error.
                            } // Ignore, should be an impossible error.

                        }

                    }
                }
            });
        }
    }

    private void setInvitedLocationMarker(ContactLocation contactLocation) {
        String name = contactLocation.getConfirmContact().getFirstName() + " " + contactLocation.getConfirmContact().getLastName();
        String number = contactLocation.getConfirmContact().getCountryPhoneCode() + contactLocation.getConfirmContact().getMobile();
        contactLocation.setMarker(mMap.addMarker((new MarkerOptions()).position(new LatLng(contactLocation.getLatitude(), contactLocation.getLongitude())).title(name)
                .snippet(number)));
    }

    private void updateAllUserLocation() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int i = 0; i < contactLocations.size(); i++) {
            setInvitedLocationMarker(contactLocations.get(i));
            builder.include(contactLocations.get(i).getMarker().getPosition());
        }
//        LatLngBounds bounds = builder.build();
//        int padding = 0; // offset from edges of the map in pixels
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
//        mMap.animateCamera(cameraUpdate);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                getLocationPermission();
                return false;
            }
        });

        getLocationPermission();
        getInvitationAcceptedList(userId, invitationId);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            // Turn on the My Location layer and the related control on the map.
            updateLocationUI();

            // Get the current location of the device and set the position of the map.
//            getDeviceLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
//                mLastKnownLocation = null;
//                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getInvitationAcceptedList(String userId, String invitationId) {
        Call<String> call = inviteCurrentLocationService.getInviteAcceptedList(userId, invitationId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        boolean status = jsonObject.getBoolean("status");
                        if (status) {
                            JSONArray jsonArray = jsonObject.getJSONArray("list");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                double latitude, longitude;
                                JSONObject dataObj = object.getJSONObject("location_data");
                                JSONObject userDetailsObj = object.getJSONObject("user_details");
                                latitude = dataObj.getDouble("latitude");
                                longitude = dataObj.getDouble("longitude");
                                ConfirmContact confirmContact = new Gson().fromJson(userDetailsObj.toString(), ConfirmContact.class);
                                contactLocations.add(new ContactLocation(latitude, longitude, null, confirmContact));
                            }
                            updateAllUserLocation();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getString(R.string.the_server_not_responding), Toast.LENGTH_LONG).show();
            }
        });
    }

}
