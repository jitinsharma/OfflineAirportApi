package io.github.jitinsharma.offlineairportapi.model;

/**
 * Created by jsharma on 13/05/17.
 */
public class AirportRequest {
    private String latitude;
    private String longitude;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
