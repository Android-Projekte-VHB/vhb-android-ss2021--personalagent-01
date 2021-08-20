package com.ristudios.personalagent.data.api;

import org.json.JSONObject;

public interface WeatherDataRequestListener {

    void onDataRequestFinished(JSONObject object);

}
