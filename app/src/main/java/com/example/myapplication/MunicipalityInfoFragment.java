package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MunicipalityInfoFragment extends Fragment


{
    private TextView municipalityName, temperature, feelsLike, humidity, windSpeed, weatherState, population;
    private OnButtonClickListener mListener;

    public String roundToOneDecimalPlace( double value )
    {
        DecimalFormat decimalFormat = new DecimalFormat( "#.#" );
        return decimalFormat.format( value );
    }
    @Override
    public void onAttach( Context context )
    {
        super.onAttach( context );
        if ( context instanceof OnButtonClickListener )
        {
            mListener = ( OnButtonClickListener )context;
        }
        else
        {
            throw new RuntimeException( context.toString()
                    + " must implement OnButtonClickListener" );
        }
    }

    public void updateTextView( Municipality municipality, Weather weather )
    {
        municipalityName.setText( municipality.getName() );
        temperature.setText( "Temperature is "+ roundToOneDecimalPlace( weather.getTemperature() ) + "°C.");
        feelsLike.setText( "Feels like " + roundToOneDecimalPlace( weather.getFeelsLike() ) + "°C.");
        humidity.setText( "Humidity is " + Short.toString( weather.getHumidity() ) + "%.");
        windSpeed.setText( "Wind speed is " + roundToOneDecimalPlace( weather.getWindSpeed() ) + " m/s.");
        weatherState.setText( "Weather is " + weather.getweather() + ".");
        population.setText( "The population is " + QuizActivity.formatNumberWithSpaces( municipality.getPopulationData().get( ( short )2020 ) ) + " people." );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState )
    {
        View view = inflater.inflate( R.layout.fragment_municipality_info, container, false );
        municipalityName = view.findViewById( R.id.municipality_name_text_view );
        temperature      = view.findViewById( R.id.temperature_text_view );
        feelsLike        = view.findViewById( R.id.feels_like_text_view );
        humidity         = view.findViewById( R.id.humidity_text_view );
        windSpeed        = view.findViewById( R.id.wind_speed_text_view );
        weatherState     = view.findViewById( R.id.weather_state_text_view );
        population       = view.findViewById( R.id.population_text_view );
        return view;
    }


    public interface OnButtonClickListener
    {
        void onButtonClick( Municipality municipality, Weather weather );
    }
}