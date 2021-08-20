package com.ristudios.personalagent.data.api;
import org.json.JSONException;
import org.json.JSONObject;

public class Weather {

    public static final String BASE_IMAGE_URL = "https://openweathermap.org/img/wn/$IMAGEID@2x.png";
    private final String imageURL;
    private final int temperature;
    private final int minTemp;
    private final int maxTemp;
    private final double precipitation;

    public Weather(int temperature, String imageURL, int minTemp, int maxTemp, double precipitation){
        this.temperature = temperature;
        this.imageURL = imageURL;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.precipitation = minTemp;
    }

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