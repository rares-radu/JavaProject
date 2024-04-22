package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;


public class MunicipalityBriefInfoFragment extends Fragment
{
    private TextView cityName, temperature;
    private ImageView weatherState, populationFirst, populationSecond, populationThird;
    private OnButtonClickListenerBrief mListenerBrief;

    public String roundToOneDecimalPlace( double value )
    {
        DecimalFormat decimalFormat = new DecimalFormat( "#.#" );
        return decimalFormat.format( value );
    }
    @Override
    public void onAttach( Context context )
    {
        super.onAttach( context );
        if ( context instanceof OnButtonClickListenerBrief )
        {
            mListenerBrief = ( OnButtonClickListenerBrief )context;
        }
        else
        {
            throw new RuntimeException( context.toString()
                    + " must implement OnButtonClickListenerBrief" );
        }
    }

    public void updateTextViewBrief( Municipality municipality, Weather weather )
    {
        cityName.setText( municipality.getName() );
        temperature.setText( "Temperature is "+ roundToOneDecimalPlace( weather.getTemperature() ) + "°C." );
        String imageUrl = "https://openweathermap.org/img/wn/" + weather.getWeatherIcon() + ".png";
        Glide.with( this ).load( imageUrl ).into( weatherState );
        int population = municipality.getPopulationData().get( (short) 2020);
        if(population >= 500000){
            populationThird.setImageResource( R.drawable.person_filled );
        }
        if(population >= 100000){
            populationSecond.setImageResource( R.drawable.person_filled );
        }
        populationFirst.setImageResource( R.drawable.person_filled );
    }


    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState )
    {
        View view = inflater.inflate( R.layout.fragment_municipality_brief_info, container, false );
        cityName = view.findViewById( R.id.brief_city_name_text_view );
        temperature = view.findViewById( R.id.brief_temperature_text_view );
        weatherState = view.findViewById( R.id.brief_weather_state_icon );
        populationFirst = view.findViewById( R.id.brief_person_first_image );
        populationSecond = view.findViewById( R.id.brief_person_second_image );
        populationThird = view.findViewById( R.id.brief_person_third_image );
        return view;
    }
    public interface OnButtonClickListenerBrief
    {
        void onButtonClickBrief( Municipality municipality, Weather weather );
    }
}