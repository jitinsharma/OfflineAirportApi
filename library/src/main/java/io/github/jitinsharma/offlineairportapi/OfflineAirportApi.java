package io.github.jitinsharma.offlineairportapi;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import io.github.jitinsharma.offlineairportapi.model.AirportRequest;
import io.github.jitinsharma.offlineairportapi.model.AirportResponse;

/**
 * Created by jsharma on 13/05/17.
 */
public class OfflineAirportApi {
    private static final String TAG = OfflineAirportApi.class.getSimpleName();
    private Context context;
    private float smallestDistance = 0.0f;
    private float distanceLimit = 50000;
    private boolean getList = false;

    /**
     * Initialize library
     * @param context
     */
    public OfflineAirportApi(Context context) {
        this.context = context;
    }

    /**
     * Returns nearest airport from the list
     * @param airportRequest Should have latitude and longitude
     * @return #AirportResponse containing airport related data
     */
    public AirportResponse getNearestAirport(AirportRequest airportRequest) {
        getList = false;
        return calculateNearestAirport(airportRequest.getLatitude(), airportRequest.getLongitude()).get(0);
    }

    /**
     * Returns list of nearest airports from the list
     * A minimum distance can be set using #{setDistanceLimit} to limit the size
     * @param airportRequest Should have latitude and longitude
     * @return List of #AirportResponse containing airport related data
     */
    public ArrayList<AirportResponse> getNearestAirports(AirportRequest airportRequest) {
        getList = true;
        return calculateNearestAirport(airportRequest.getLatitude(),
            airportRequest.getLongitude());
    }

    /**
     * Set minimum distance for when getting list of nearest airports
     * @param distanceLimit Limit in meters. Default is 50,000m
     */
    public void setDistanceLimit(float distanceLimit) {
        this.distanceLimit = distanceLimit;
    }

    private ArrayList<AirportResponse> calculateNearestAirport(String latitude, String longitude) {
        ArrayList<AirportResponse> airportResponses = new ArrayList<>();
        Location userLocation = new Location("User Location");
        userLocation.setLatitude(Double.valueOf(latitude));
        userLocation.setLongitude(Double.valueOf(longitude));
        String localAirportData = loadJSONFromAsset(context, "data.json");
        try {
            JSONObject localAirportObject = new JSONObject(localAirportData);
            Iterator<?> keys = localAirportObject.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                Location airportLocation = new Location("Airport Location");
                String[] coordinates = key.split(",");
                airportLocation.setLatitude(Double.valueOf(coordinates[0]));
                airportLocation.setLongitude(Double.valueOf(coordinates[1]));
                AirportResponse airportResponse = getNearest(
                    userLocation, airportLocation, localAirportObject.getJSONObject(key));
                if (airportResponse != null) {
                    if (getList) {
                        airportResponses.add(airportResponse);
                    }
                    else {
                        airportResponses.add(0, airportResponse);
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(TAG , Log.getStackTraceString(e));
        }
        Collections.sort(airportResponses, new Comparator<AirportResponse>() {
            @Override
            public int compare(AirportResponse o1, AirportResponse o2) {
                return o1.getDistance() < o2.getDistance() ? -1 : 1;
            }
        });
        return airportResponses;
    }

    private AirportResponse getNearest(Location userLocation, Location airportLocation, JSONObject airportProp)
        throws JSONException {
        AirportResponse airportResponse = null;
        float distance = userLocation.distanceTo(airportLocation);
        if (getList) {
            if (distance < distanceLimit) {
                airportResponse = createResponse(airportProp, distance);
            }
        }
        else {
            if (smallestDistance == 0.0f && distance > 0.0f) {
                smallestDistance = distance;
            }
            if (smallestDistance > 0.0f && distance < smallestDistance) {
                smallestDistance = distance;
                airportResponse = createResponse(airportProp, distance);
            }
        }
        return airportResponse;
    }

    private String loadJSONFromAsset(Context context, String jsonName) {
        String json = null;
        try {
            InputStream inputStream = context.getAssets().open(jsonName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            Log.e(TAG, String.valueOf(e));
        }
        return json;
    }

    private AirportResponse createResponse(JSONObject airportPropObject, float distance) throws JSONException{
        AirportResponse airportResponse = new AirportResponse();
        airportResponse.setAirportLatitude(airportPropObject.getString("lat"));
        airportResponse.setAirportLongitude(airportPropObject.getString("lon"));
        airportResponse.setIataAirportCode(airportPropObject.getString("iata"));
        airportResponse.setIcaoAirportCode(airportPropObject.getString("icao"));
        airportResponse.setAirportName(airportPropObject.getString("name"));
        airportResponse.setTimezone(airportPropObject.getString("tz"));
        airportResponse.setCityName(airportPropObject.getString("city"));
        airportResponse.setCountryCode(airportPropObject.getString("country"));
        airportResponse.setDistance(distance);
        return airportResponse;
    }
}
