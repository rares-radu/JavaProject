package com.example.myapplication;

import android.os.AsyncTask;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class MunicipalityRetriever
{
    // NOTE: They only have data up to 2022.
    private static final String POPULATION_API_URL = "https://pxdata.stat.fi/PxWeb/api/v1/en/StatFin/synt/statfin_synt_pxt_12dy.px";
    private static final String POPULATION_QUERY   = "{\n" +
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

    // NOTE: They only have data up to 2022.
    private static final String EMPLOYMENT_API_URL = "https://pxdata.stat.fi/PxWeb/api/v1/en/StatFin/tyokay/statfin_tyokay_pxt_115x.px";
    private static final String EMPLOYMENT_QUERY   = "{\n" +
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
            "          \"tyollisyysaste\"\n" +
            "        ]\n" +
            "      }\n" +
            "    }\n" +
            "  ],\n" +
            "  \"response\": {\n" +
            "    \"format\": \"json-stat2\"\n" +
            "  }\n" +
            "}";

    // NOTE: They only have data up to 2022.
    private static final String WORKPLACE_API_URL = "https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/tyokay/statfin_tyokay_pxt_125s.px";
    private static final String WORKPLACE_QUERY   = "{\n" +
            "  \"query\": [\n" +
            "    {\n" +
            "      \"code\": \"Alue\",\n" +
            "      \"selection\": {\n" +
            "        \"filter\": \"item\",\n" +
            "        \"values\": [\n" +
            "          \"MUNICIPALITY_CODE\"\n" +
            "        ]\n" +
            "      }\n" +
            "    }\n" +
            "  ],\n" +
            "  \"response\": {\n" +
            "    \"format\": \"json-stat2\"\n" +
            "  }\n" +
            "}";

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
            new RetrieveJson( listener ).execute( municipalityName );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
    }

    // Why all of this? This is needed because Android does not allow network operations on the main thread.
    private static class RetrieveJson extends AsyncTask< String, Void, Municipality >
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
        protected Municipality doInBackground( String... params )
        {
            try
            {
                final String municipalityName = params[ 0 ];

                URL    populationApiUrl = new URL( POPULATION_API_URL );
                String municipalityCode = getMunicipalityCode( populationApiUrl, municipalityName );
                String populationQuery  = POPULATION_QUERY.replaceFirst( "MUNICIPALITY_CODE", municipalityCode );

                JSONObject populationResponse = makeApiRequest( populationApiUrl, populationQuery );
                if ( populationResponse == null )
                {
                    return null;
                }

                JSONArray                 jsonPopulation = populationResponse.getJSONArray( "value" );
                short                     year           = 1990; // Data about the population always starts from 1990.
                HashMap< Short, Integer > populationData = new HashMap<>();
                for ( int i = 0; i < jsonPopulation.length(); i++ )
                {
                    populationData.put( year++, jsonPopulation.getInt( i ) );
                }

                URL    employmentApiUrl = new URL( EMPLOYMENT_API_URL );
                String employmentQuery  = EMPLOYMENT_QUERY.replaceFirst( "MUNICIPALITY_CODE", municipalityCode );

                JSONObject employmentResponse = makeApiRequest( employmentApiUrl, employmentQuery );
                if ( employmentResponse == null )
                {
                    return null;
                }

                JSONArray jsonEmployment = employmentResponse.getJSONArray( "value" );
                year = 1987; // Data about the employment always starts from 1987.
                HashMap< Short, Float > employmentData = new HashMap<>();
                for ( int i = 0; i < jsonEmployment.length(); i++ )
                {
                    employmentData.put( year++, ( float )jsonEmployment.getDouble( i ) );
                }

                URL    workplaceApiUrl = new URL( WORKPLACE_API_URL );
                String workplaceQuery  = WORKPLACE_QUERY.replaceFirst( "MUNICIPALITY_CODE", municipalityCode );

                JSONObject workplaceResponse = makeApiRequest( workplaceApiUrl, workplaceQuery );
                if ( workplaceResponse == null )
                {
                    return null;
                }

                JSONArray jsonWorkplace = workplaceResponse.getJSONArray( "value" );
                year = 1987; // Data about the workplace self-sufficiency always starts from 1987.
                HashMap< Short, Float > workplaceData = new HashMap<>();
                for ( int i = 0; i < jsonWorkplace.length(); i++ )
                {
                    workplaceData.put( year++, ( float )jsonWorkplace.getDouble( i ) );
                }

                return new Municipality( municipalityName, populationData, employmentData, workplaceData );
            }
            catch ( IOException | JSONException e )
            {
                return null;
            }
        }

        private JSONObject makeApiRequest( URL apiUrl, String query ) throws IOException, JSONException
        {
            HttpsURLConnection connection = ( HttpsURLConnection )apiUrl.openConnection();
            connection.setRequestMethod( "POST" );
            connection.setRequestProperty( "Content-Type", "application/json" );
            connection.setDoOutput( true );

            OutputStream outputStream = connection.getOutputStream();
            byte[]       input        = query.getBytes( StandardCharsets.UTF_8 );
            outputStream.write( input, 0, input.length );
            outputStream.close();

            if ( connection.getResponseCode() != HttpsURLConnection.HTTP_OK )
            {
                return null;
            }

            StringBuilder  response = new StringBuilder();
            BufferedReader in       = new BufferedReader( new InputStreamReader( connection.getInputStream() ) );
            String         inputLine;
            while ( ( inputLine = in.readLine() ) != null )
            {
                response.append( inputLine );
            }
            in.close();
            connection.disconnect();

            return new JSONObject( response.toString() );
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
