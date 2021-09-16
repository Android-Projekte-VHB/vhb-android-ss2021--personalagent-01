package com.ristudios.personalagent.data.api;

import org.json.JSONException;
import org.json.JSONObject;

/*
Represents the Weather forecast for the current day.
 */
public class Weather {

    public static final String BASE_IMAGE_URL = "https://openweathermap.org/img/wn/$IMAGEID@2x.png";
    private final String imageURL;
    private final int temperature;
    private final int minTemp;
    private final int maxTemp;
    private final double precipitation;


    /**
     * Creates a new Instance of the Weather Class.
     *
     * @param temperature   Current Temperature
     * @param imageURL      Image URL for the GLIDE-Library. Shows a picture of the current weather
     * @param minTemp       Highest Temperature of the day
     * @param maxTemp       Lowest Temperature of the day
     * @param precipitation Chance of Precipitation in %
     */
    public Weather(int temperature, String imageURL, int minTemp, int maxTemp, double precipitation) {
        this.temperature = temperature;
        this.imageURL = imageURL;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.precipitation = precipitation;
    }


    /**
     * Static Method to convert the response from OpenWeatherAPI into a Weather Object.
     *
     * @param object The API response as JSON object
     * @return a new instance of the Weather Class
     */
    public static Weather fromJSONObject(JSONObject object) throws JSONException {
        String imageCode = object.getJSONArray("daily").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("icon");
        String imageURL = BASE_IMAGE_URL.replace("$IMAGEID", imageCode);
        int maxTemp = object.getJSONArray("daily").getJSONObject(0).getJSONObject("temp").getInt("max");
        int minTemp = object.getJSONArray("daily").getJSONObject(0).getJSONObject("temp").getInt("min");
        double precipitation = object.getJSONArray("daily").getJSONObject(0).getDouble("pop");
        int temperature = (int) object.getJSONObject("current").getInt("temp");
        return new Weather(temperature, imageURL, minTemp, maxTemp, precipitation);
    }

    public int getTemperature() {
        return temperature;
    }

    public String getImageURL() {
        return imageURL;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public double getPrecipitation() {
        return precipitation;
    }
}