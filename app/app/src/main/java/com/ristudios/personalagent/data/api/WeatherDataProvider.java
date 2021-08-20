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

        public WeatherDataProvider(Context context, WeatherDataListener listener){
            this.context = context;
            this.listener = listener;
            coordinatesProvider = new CoordinatesProvider(context, this);
        }

        public void update(){
            coordinatesProvider.start();
        }

        public Weather getWeather(){
            return currentWeather;
        }

        @Override
        public void onDataRequestFinished(JSONObject object) {
            try {
                currentWeather = Weather.fromJSONObject(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            listener.onWeatherDataUpdated();
        }

        @Override
        public void onCoordinatesReady(double longitude, double latitude) {
            request = new WeatherDataRequest(context, this, longitude, latitude);
            request.start();
        }

}
