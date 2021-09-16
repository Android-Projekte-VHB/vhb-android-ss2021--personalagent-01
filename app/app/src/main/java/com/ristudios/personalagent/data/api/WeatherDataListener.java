package com.ristudios.personalagent.data.api;

/**
 * Informs the implementing Class that the Weather Data in a WeatherDataProvider has been updated.
 */
public interface WeatherDataListener {
    /**
     * Called when the Weather Data in a WeatherDataProvider has benn updated.
     */
    void onWeatherDataUpdated();

}
