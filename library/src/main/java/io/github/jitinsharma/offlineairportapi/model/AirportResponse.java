package io.github.jitinsharma.offlineairportapi.model;

/**
 * Created by jsharma on 13/05/17.
 */
public class AirportResponse {
    private String iataAirportCode;
    private String icaoAirportCode;
    private String airportName;
    private String countryCode;
    private String cityName;
    private String airportLatitude;
    private String airportLongitude;
    private String timezone;
    private float distance;

    public String getIataAirportCode() {
        return iataAirportCode;
    }

    public void setIataAirportCode(String iataAirportCode) {
        this.iataAirportCode = iataAirportCode;
    }

    public String getIcaoAirportCode() {
        return icaoAirportCode;
    }

    public void setIcaoAirportCode(String icaoAirportCode) {
        this.icaoAirportCode = icaoAirportCode;
    }

    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAirportLatitude() {
        return airportLatitude;
    }

    public void setAirportLatitude(String airportLatitude) {
        this.airportLatitude = airportLatitude;
    }

    public String getAirportLongitude() {
        return airportLongitude;
    }

    public void setAirportLongitude(String airportLongitude) {
        this.airportLongitude = airportLongitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
