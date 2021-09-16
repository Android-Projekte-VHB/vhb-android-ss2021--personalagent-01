package com.ristudios.personalagent.data.api;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherDataProvider implements WeatherDataRequestListener, CoordinatesProvider.CoordinatesListener {


    private final Context context;
    private final WeatherDataListener listener;
    public Weather currentWeather;
    private CoordinatesProvider coordinatesProvider;
    private WeatherDataRequest request;


    /**
     * The WeatherDataProvider handles the interaction with a WeatherDataRequest.
     * It registers itself as a CoordinatesListener to receive the current location of the device to then create a WeatherDataRequest for it.
     * The latest response is then stored in the currentWeather Object and can be accessed by calling getWeather().
     *
     * @param context  The Application Context
     * @param listener A listener that is called when the Weather request has successfully been answered.
     */
    public WeatherDataProvider(Context context, WeatherDataListener listener) {
        this.context = context;
        this.listener = listener;
        coordinatesProvider = new CoordinatesProvider(context, this);
    }


    /**
     * Calling this method will start the process of accessing the current Location to creating the according WeatherDataRequest.
     */
    public void update() {
        coordinatesProvider.start();
    }

    /**
     * Returns the latest stored response from OpenWeatherAPI as an instance of the Weather Class.
     */
    public Weather getWeather() {
        return currentWeather;
    }

    /**
     * Handles the response from the CoordinatesProvider. It is used to create and start the WeatherDataRequest for the devices location.
     *
     * @param longitude Current Location's Longitude
     * @param latitude  Current Location's Latitude
     */
    @Override
    public void onCoordinatesReady(double longitude, double latitude) {
        request = new WeatherDataRequest(context, this, longitude, latitude);
        request.start();
    }

    /**
     * Handles the response from the WeatherDataRequest that is started in onCoordinatesReady.
     * If successful, it stores the response as an instance of the Weather Class and then informs the attached WeatherDataListener of the result.
     *
     * @param object the response in the form of a JSONObject. Used in the static method of the Weather Class to transform it into a Weather Object.
     */
    @Override
    public void onDataRequestFinished(JSONObject object) {
        try {
            currentWeather = Weather.fromJSONObject(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listener.onWeatherDataUpdated();
    }

}
