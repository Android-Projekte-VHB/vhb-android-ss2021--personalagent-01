package com.ristudios.personalagent.data.api;

import org.json.JSONObject;

/**
 * Informs the implementing Class that a WeatherDataRequest has been finished and answered.
 */
public interface WeatherDataRequestListener {

    /**
     * Called when the API-Response is ready.
     *
     * @param object The response as a JSONObject
     */
    void onDataRequestFinished(JSONObject object);

}
