# Offline Airport API
A simple Android Library to get Nearest Airport(s) from latitude,longitude using a local json data
(~28K airports).

Local data is sourced from [here.](https://github.com/mwgg/Airports)

## Why(s) and Good(s)
- Ability to get nearest airport without a webservice call.
- Can be used as a cache mechanism in case webservice call takes longer time.
- Can be used as a fallback mechanism if webservice fails.(Server error, user is not connected to
 internet)

## Bad(s)
- Although data set is exhaustive, it's not an updated one.
- Library size (~ 1.6 MB)

## Usage
Create a Request

        AirportRequest airportRequest = new AirportRequest();
        airportRequest.setLatitude("28.632907");
        airportRequest.setLongitude("77.219536");

Initialize library

        OfflineAirportApi offlineAirportApi = new OfflineAirportApi(this);

Get Single Nearest Airport

        //Must be called asynchronously
        AirportResponse airportResponse = offlineAirportApi.getNearestAirport(airportRequest);

Get Multiple Nearest Airports

        //Set a minimum distance limit from current location. Has a default value of 50,000m
        offlineAirportApi.setDistanceLimit(25000);
        //Must be called asynchronously
        ArrayList<AirportResponse> airportResponses = offlineAirportApi.getNearestAirports(airportRequest);

**Check Sample App for Usage.**

## TODO
- Check for more sources of data.
- Handle calculation asynchronously within the library.
- Ability to detect location using GPS from library.

![screen](../master/img/1.png) ![screen](../master/img/2.png) ![screen](../master/img/3.png)