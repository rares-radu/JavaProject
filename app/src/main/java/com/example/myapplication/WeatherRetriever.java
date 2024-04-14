package com.example.myapplication;

import android.os.AsyncTask;
import android.util.Log;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherRetriever
{
    private static final String API_KEY = "a3f5554914fbb44159aa515637510ffe";
    private static final String API_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=" + API_KEY;

    /*
     * How to use:
     *
     * WeatherRetriever.getWeather( "Lahti", new RetrieverListener()
     * {
     *     @Override
     *     public void onSuccess( Weather weather )
     *     {
     *         Log.i( "[INFO]", String.format( "Weather [%s %s %f]", weather.getName(), weather.getCloudiness(), weather.getTemperature() ) );
     *     }
     *
     *     @Override
     *     public void onFailure( Exception e )
     *     {
     *     }
     * } );
     */
    public static void getWeather( @NotNull String cityName, @NotNull RetrieverListener listener )
    {
        try
        {
            URL url = new URL( String.format( API_URL, cityName ) );
            new RetrieveJson( listener ).execute( url );
        }
        catch ( MalformedURLException e )
        {
            Log.e( "[ERROR] WeatherRetriever", e.toString() );
            listener.onFailure( e );
        }
    }

    // Why all of this? This is needed because Android does not allow network operations on the main thread.
    private static class RetrieveJson extends AsyncTask< URL, Void, Weather >
    {
        private final RetrieverListener listener;

        public RetrieveJson( RetrieverListener listener )
        {
            this.listener = listener;
        }

        // Only one URL supported. Index 0 will be used. If you pass more, they'll simply be ignored.
        @Override
        protected Weather doInBackground( URL... urls )
        {
            try
            {
                HttpsURLConnection connection = ( HttpsURLConnection )urls[ 0 ].openConnection();
                connection.setRequestMethod( "GET" );

                BufferedReader in       = new BufferedReader( new InputStreamReader( connection.getInputStream() ) );
                String         inputLine;
                StringBuilder  response = new StringBuilder();
                while ( ( inputLine = in.readLine() ) != null )
                {
                    response.append( inputLine );
                }
                in.close();

                JSONObject jsonResponse = new JSONObject( response.toString() );

                String cityName    = jsonResponse.getString( "name" );
                String cloudiness  = jsonResponse.getJSONArray( "weather" ).getJSONObject( 0 ).getString( "main" );
                double temperature = jsonResponse.getJSONObject( "main" ).getDouble( "temp" );
                double feelsLike   = jsonResponse.getJSONObject( "main" ).getDouble( "feels_like" );
                short  humidity    = ( short )jsonResponse.getJSONObject( "main" ).getInt( "humidity" );
                double windSpeed   = jsonResponse.getJSONObject( "wind" ).getDouble( "speed" );

                return new Weather( cityName, cloudiness, Weather.kelvinToCelsius( temperature ),
                        Weather.kelvinToCelsius( feelsLike ), humidity, windSpeed );
            }
            catch ( IOException | JSONException e )
            {
                return null;
            }
        }

        @Override
        protected void onPostExecute( Weather result )
        {
            if ( result != null )
            {
                listener.onSuccess( result );
            }
            else
            {
                listener.onFailure( new Exception( "Failed to retrieve weather data" ) );
            }
        }
    }
}
