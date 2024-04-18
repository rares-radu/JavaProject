package com.example.myapplication;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class MunicipalityRetriever
{
    // NOTE: They only have data up to 2022.
    private static final String API_URL = "https://pxdata.stat.fi/PxWeb/api/v1/en/StatFin/synt/statfin_synt_pxt_12dy.px";
    private static final String QUERY   = "{\n" +
            "  \"query\": [\n" +
            "    {\n" +
            "      \"code\": \"Alue\",\n" +
            "      \"selection\": {\n" +
            "        \"filter\": \"item\",\n" +
            "        \"values\": [\n" +
            "          \"MUNICIPALITY_CODE\"\n" +
            "        ]\n" +
            "      }\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"Tiedot\",\n" +
            "      \"selection\": {\n" +
            "        \"filter\": \"item\",\n" +
            "        \"values\": [\n" +
            "          \"vaesto\"\n" +
            "        ]\n" +
            "      }\n" +
            "    }\n" +
            "  ],\n" +
            "  \"response\": {\n" +
            "    \"format\": \"json-stat2\"\n" +
            "  }\n" +
            "}\n";

    /*
     * How to use:
     *
     * MunicipalityRetriever.getMunicipality( "Lahti", new RetrieverListener< Municipality >()
     * {
     *     @Override
     *     public void onSuccess( Municipality municipality )
     *     {
     *         Log.i( "[INFO]", String.format( "%d", municipality.getPopulationData().get( ( short )2022 ) ) );
     *     }
     *
     *     @Override
     *     public void onFailure( Exception e )
     *     {
     *     }
     * } );
     */
    public static void getMunicipality( @NotNull String municipalityName, @NotNull RetrieverListener< Municipality > listener )
    {
        try
        {
            URL url = new URL( API_URL );
            new RetrieveJson( listener ).execute( new Pair<>( url, municipalityName ) );
        }
        catch ( MalformedURLException e )
        {
            Log.e( "[ERROR] MunicipalityRetriever", e.toString() );
            listener.onFailure( e );
        }
    }

    // Why all of this? This is needed because Android does not allow network operations on the main thread.
    private static class RetrieveJson extends AsyncTask< Pair< URL, String >, Void, Municipality >
    {
        private final RetrieverListener< Municipality > listener;

        public RetrieveJson( RetrieverListener< Municipality > listener )
        {
            this.listener = listener;
        }

        private String getMunicipalityCode( URL url, String municipalityName )
        {
            try
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

                JSONObject jsonResponse = new JSONObject( response.toString() );
                JSONArray  variables    = jsonResponse.getJSONArray( "variables" );

                for ( int i = 0; i < variables.length(); i++ )
                {
                    JSONObject variable = variables.getJSONObject( i );
                    if ( variable.getString( "code" ).equals( "Alue" ) )
                    {
                        JSONArray codes        = variable.getJSONArray( "values" );
                        JSONArray municipality = variable.getJSONArray( "valueTexts" );

                        for ( int j = 0; j < municipality.length(); j++ )
                        {
                            if ( municipality.getString( j ).equals( municipalityName ) )
                            {
                                return codes.getString( j );
                            }
                        }
                    }
                }

                // Not found.
                return null;
            }
            catch ( IOException | JSONException e )
            {
                return null;
            }
        }

        // Index 0 will be used. If you pass more, they'll simply be ignored.
        @Override
        protected Municipality doInBackground( Pair< URL, String >... params )
        {
            try
            {
                String query = QUERY.replaceFirst( "MUNICIPALITY_CODE", getMunicipalityCode( params[ 0 ].first, params[ 0 ].second ) );

                HttpsURLConnection connection = ( HttpsURLConnection )params[ 0 ].first.openConnection();
                connection.setRequestMethod( "POST" );
                connection.setRequestProperty( "Content-Type", "application/json" );
                connection.setDoOutput( true );

                OutputStream outputStream = connection.getOutputStream();
                byte[]       input        = query.getBytes( StandardCharsets.UTF_8 );
                outputStream.write( input, 0, input.length );

                if ( connection.getResponseCode() != HttpsURLConnection.HTTP_OK )
                {
                    return null;
                }

                BufferedReader in       = new BufferedReader( new InputStreamReader( connection.getInputStream() ) );
                String         inputLine;
                StringBuilder  response = new StringBuilder();
                while ( ( inputLine = in.readLine() ) != null )
                {
                    response.append( inputLine );
                }
                in.close();

                JSONObject jsonResponse = new JSONObject( response.toString() );

                JSONArray jsonPopulation = jsonResponse.getJSONArray( "value" );

                short                     year           = 1990;  // Data about the population always starts from the year 1990.
                HashMap< Short, Integer > populationData = new HashMap<>();
                for ( int i = 0; i < jsonPopulation.length(); i++ )
                {
                    populationData.put( year++, jsonPopulation.getInt( i ) );
                }

                return new Municipality( params[ 0 ].second, populationData );
            }
            catch ( IOException | JSONException e )
            {
                return null;
            }
        }

        @Override
        protected void onPostExecute( Municipality result )
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
