package com.ristudios.personalagent.data.api;

import android.content.Context;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

public class WeatherDataRequest implements Response.Listener<String>, Response.ErrorListener {

    private static final String API_URL = "https://api.openweathermap.org/data/2.5/onecall?lat=$LAT&lon=$LONG&exclude=minutely,hourly,alerts&units=metric&appid=4dfbc20d903cef3b79f82903ae70e89b";
    private final Context context;
    private final WeatherDataRequestListener listener;
    private JSONObject result;
    private final double longitude;
    private final double latitude;

    /**
     * A WeatherDataRequest sends an API Request to OpenWeatherAPI and eventually returns the result as a JSON-Object.
     *
     * @param context   The Application Context
     * @param listener  A listener to be notified when the Request is answered
     * @param longitude The longitude of the Location for the Weather forecast
     * @param latitude  The latitude of the Location for the Weather forecast
     */
    public WeatherDataRequest(Context context, WeatherDataRequestListener listener, double longitude, double latitude) {
        this.context = context;
        this.listener = listener;
        this.result = new JSONObject();
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * Sends an API-Request to OpenWeatherAPI through the Volley Framework.
     * Starts by creating a new RequestQueue and then adding the request for the right location.
     */
    public void start() {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = createVolleyRequestForCoordinates(longitude, latitude, this, this);
        queue.add(request);
        queue.start();
    }

    /**
     * This method creates a Volley StringRequest for a location by replacing the placeholder of longitude and latitude in the OpenWeatherAPI Base-URL with the right values.
     *
     * @param longitude       Longitude of the Location to receive a weather forecast
     * @param latitude        Latitude of the Location to receive a weather forecast
     * @param successListener called when the request was successful
     * @param errorListener   called when the request failed
     * @return a new StringRequest for the desired Location
     */
    private StringRequest createVolleyRequestForCoordinates(double longitude, double latitude, Response.Listener<String> successListener, Response.ErrorListener errorListener) {
        String url = API_URL.replace("$LAT", String.valueOf(round(latitude, 2)));
        String url2 = url.replace("$LONG", String.valueOf(round(longitude, 2)));
        return new StringRequest(Request.Method.GET, url2, successListener, errorListener);
    }

    /**
     * Used to inform the registered listener about the received response and hands out the result as a JSONObject.
     */
    public void notifyListenerReady() {
        listener.onDataRequestFinished(result);
    }


    @Override
    public void onErrorResponse(VolleyError error) {

    }

    /**
     * After receiving a response from OpenWeatherAPI, this method stores the result as a JSONObject and calls the listeners.
     *
     * @param response the response from OpenWeatherAPI as an unformatted String
     */
    @Override
    public void onResponse(String response) {
        try {
            result = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            notifyListenerReady();
        }
    }

    private double round(double value, int decimalPoints) {
        double d = Math.pow(10, decimalPoints);
        return (Math.round(value * d) / d);
    }

}
