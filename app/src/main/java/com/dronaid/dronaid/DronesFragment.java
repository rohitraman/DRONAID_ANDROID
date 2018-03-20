package com.dronaid.dronaid;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.intentfilter.androidpermissions.PermissionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LOCATION_SERVICE;
import static java.util.Collections.singleton;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link DronesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DronesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    locationClass appLocationService;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    boolean isPermissionGranted = false;

    private KalmanLatLong smoothGPS;

    private FloatingActionButton refresh;
    private TextInputEditText latitude;
    private TextInputEditText longtitude;
    private TextInputEditText distance;
    private ProgressBar progressBar;
    private CardView cardView;
    private RelativeLayout relativeLayout;
    private Button request;

    String token;
    String usernameText;

    double lat = 0;
    double longti = 0;


    public DronesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DronesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DronesFragment newInstance(String param1, String param2) {
        DronesFragment fragment = new DronesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        appLocationService = new locationClass(
                getActivity());
        View v = inflater.inflate(R.layout.fragment_drones, container, false);
        refresh = v.findViewById(R.id.refresh);
        latitude = v.findViewById(R.id.latitude_text);
        longtitude = v.findViewById(R.id.longtitude_text);
        distance = v.findViewById(R.id.drone_station_text);
        progressBar = v.findViewById(R.id.progressBarGPS);
        progressBar.setVisibility(View.INVISIBLE);
        cardView = v.findViewById(R.id.cardView2);
        relativeLayout = v.findViewById(R.id.relativeLayout);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        token = prefs.getString("token",null);
        usernameText = prefs.getString("username",null);

        relativeLayout.bringToFront();
        cardView.invalidate();
        relativeLayout.invalidate();

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                smoothGPS = new KalmanLatLong(4);
                progressBar.setVisibility(View.VISIBLE);

                Location gpsLocation = appLocationService
                        .getLocation(LocationManager.GPS_PROVIDER);

                if (gpsLocation != null) {
                    smoothGPS.SetState(gpsLocation.getLatitude(),gpsLocation.getLongitude(),gpsLocation.getAccuracy(),gpsLocation.getTime());
                    lat = smoothGPS.get_lat();
                    longti = smoothGPS.get_lng();
                    latitude.setText(String.valueOf(lat));
                    longtitude.setText(String.valueOf(longti));

                }
            }
        });
        request = v.findViewById(R.id.request);
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater factory = LayoutInflater.from(getContext());
                final View deleteDialogView = factory.inflate(R.layout.dialog_delete, null);
                final AlertDialog deleteDialog = new AlertDialog.Builder(getContext()).create();
                deleteDialog.setView(deleteDialogView);
                deleteDialogView.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteDialogView.findViewById(R.id.progressBar12).setVisibility(View.VISIBLE);
                        TextInputEditText reenter = deleteDialogView.findViewById(R.id.reenter);

                        APIClient.getAPIInterface().requestDrone(usernameText,reenter.getText().toString(),lat,longti,token).enqueue(new Callback<RequestDroneResponseModel>() {
                            @Override
                            public void onResponse(Call<RequestDroneResponseModel> call, Response<RequestDroneResponseModel> response) {
                                Log.d("TAG","Success");
                                if(response.body().isSuccess()){
                                    distance.setText(String.valueOf(response.body().getDistance()));
                                }
                                deleteDialog.dismiss();

                            }

                            @Override
                            public void onFailure(Call<RequestDroneResponseModel> call, Throwable t) {
                                Log.d("TAG",t.getMessage());
                                deleteDialog.dismiss();

                            }
                        });

                    }
                });
                deleteDialogView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteDialog.dismiss();
                    }
                });

                deleteDialog.show();

            }
        });

        PermissionManager permissionManager = PermissionManager.getInstance(getContext());
        permissionManager.checkPermissions(singleton(Manifest.permission.ACCESS_FINE_LOCATION), new PermissionManager.PermissionRequestListener() {
            @Override
            public void onPermissionGranted() {
                isPermissionGranted = true;
            }

            @Override
            public void onPermissionDenied() {
                Toast.makeText(getContext(), "Drone service cannot work without GPS", Toast.LENGTH_LONG).show();
            }
        });

        return v;
    }

    public class locationClass implements LocationListener{

        protected LocationManager locationManager;
        Location location;

        private static final long MIN_DISTANCE_FOR_UPDATE = 0;
        private static final long MIN_TIME_FOR_UPDATE = 0;

        public locationClass(Context context) {
            locationManager = (LocationManager) context
                    .getSystemService(LOCATION_SERVICE);
        }

        @SuppressLint("MissingPermission")
        public Location getLocation(String provider) {
            if (locationManager.isProviderEnabled(provider)) {
                locationManager.requestLocationUpdates(provider,
                        MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE, this);
                Log.d("TAG","KKK");
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(provider);
                    Log.d("TAG","KKKK");
                    if (location!=null){
                     lat = location.getLatitude();
                     longti = location.getLongitude();
                    latitude.setText(String.valueOf(lat));
                    longtitude.setText(String.valueOf(longti));
                    }
                    return location;
                }
            }
            return null;
        }

        @Override
        public void onLocationChanged(Location location) {
            smoothGPS.Process(location.getLatitude(),location.getLongitude(),location.getAccuracy(),location.getTime());
             lat = smoothGPS.get_lat();
             longti = smoothGPS.get_lng();
            latitude.setText(String.valueOf(lat));
            longtitude.setText(String.valueOf(longti));
            Log.d("TAG","OnLocationChanged");
            progressBar.setVisibility(View.INVISIBLE);
            request.setEnabled(true);

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }

    public class KalmanLatLong {
        private final float MinAccuracy = 1;

        private float Q_metres_per_second;
        private long TimeStamp_milliseconds;
        private double lat;
        private double lng;
        private float variance; // P matrix.  Negative means object uninitialised.  NB: units irrelevant, as long as same units used throughout

        public KalmanLatLong(float Q_metres_per_second) { this.Q_metres_per_second = Q_metres_per_second; variance = -1; }

        public long get_TimeStamp() { return TimeStamp_milliseconds; }
        public double get_lat() { return lat; }
        public double get_lng() { return lng; }
        public float get_accuracy() { return (float)Math.sqrt(variance); }

        public void SetState(double lat, double lng, float accuracy, long TimeStamp_milliseconds) {
            this.lat=lat; this.lng=lng; variance = accuracy * accuracy; this.TimeStamp_milliseconds=TimeStamp_milliseconds;
        }

        /// <summary>
        /// Kalman filter processing for lattitude and longitude
        /// </summary>
        /// <param name="lat_measurement_degrees">new measurement of lattidude</param>
        /// <param name="lng_measurement">new measurement of longitude</param>
        /// <param name="accuracy">measurement of 1 standard deviation error in metres</param>
        /// <param name="TimeStamp_milliseconds">time of measurement</param>
        /// <returns>new state</returns>
        public void Process(double lat_measurement, double lng_measurement, float accuracy, long TimeStamp_milliseconds) {
            if (accuracy < MinAccuracy) accuracy = MinAccuracy;
            if (variance < 0) {
                // if variance < 0, object is unitialised, so initialise with current values
                this.TimeStamp_milliseconds = TimeStamp_milliseconds;
                lat=lat_measurement; lng = lng_measurement; variance = accuracy*accuracy;
            } else {
                // else apply Kalman filter methodology

                long TimeInc_milliseconds = TimeStamp_milliseconds - this.TimeStamp_milliseconds;
                if (TimeInc_milliseconds > 0) {
                    // time has moved on, so the uncertainty in the current position increases
                    variance += TimeInc_milliseconds * Q_metres_per_second * Q_metres_per_second / 1000;
                    this.TimeStamp_milliseconds = TimeStamp_milliseconds;
                    // TO DO: USE VELOCITY INFORMATION HERE TO GET A BETTER ESTIMATE OF CURRENT POSITION
                }

                // Kalman gain matrix K = Covarariance * Inverse(Covariance + MeasurementVariance)
                // NB: because K is dimensionless, it doesn't matter that variance has different units to lat and lng
                float K = variance / (variance + accuracy * accuracy);
                // apply K
                lat += K * (lat_measurement - lat);
                lng += K * (lng_measurement - lng);
                // new Covarariance  matrix is (IdentityMatrix - K) * Covarariance
                variance = (1 - K) * variance;
            }
        }
    }


}
