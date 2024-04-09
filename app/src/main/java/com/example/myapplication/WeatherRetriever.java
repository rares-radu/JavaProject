package com.example.myapplication;

import android.util.Log;
import androidx.annotation.CheckResult;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class WeatherRetriever
{
    private static final String API_KEY = "a3f5554914fbb44159aa515637510ffe";
    private static final String API_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=" + API_KEY;

    public static @CheckResult Weather getWeather( @NotNull String cityName )
    {
        try
        {
            URL        url          = new URL( String.format( API_URL, cityName ) );
            JSONObject jsonResponse = getJsonWeather( url );

            String cloudiness  = jsonResponse.getJSONArray( "weather" ).getJSONObject( 0 ).getString( "main" );
            double temperature = jsonResponse.getJSONObject( "main" ).getDouble( "temp" );
            double feelsLike   = jsonResponse.getJSONObject( "main" ).getDouble( "feels_like" );
            int    humidity    = jsonResponse.getJSONObject( "main" ).getInt( "humidity" );
            double windSpeed   = jsonResponse.getJSONObject( "wind" ).getDouble( "speed" );

            return new Weather( cityName, cloudiness, Weather.kelvinToCelsius( temperature ), Weather.kelvinToCelsius( feelsLike ), humidity, windSpeed );
        }
        catch ( IOException | JSONException e )
        {
            Log.e( "[ERROR] WeatherRetriever", e.toString() );
            return null;
        }
    }

    private static @NotNull JSONObject getJsonWeather( URL url ) throws IOException, JSONException
    {
        HttpsURLConnection connection = ( HttpsURLConnection )url.openConnection();
        connection.setRequestMethod( "GET" );

        BufferedReader in       = new BufferedReader( new InputStreamReader( connection.getInputStream() ) );
        String         inputLine;
        StringBuilder  response = new StringBuilder();
        while ( ( inputLine = in.readLine() ) != null )
        {
            response.append( inputLine );
        }
        in.close();

        return new JSONObject( response.toString() );
    }
}
