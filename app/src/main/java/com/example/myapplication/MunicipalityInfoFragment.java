package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

public class MunicipalityInfoFragment extends Fragment


{
    private TextView municipalityName, temperature, feelsLike, humidity, windSpeed, weatherState, population, employment, workplace;
    private LineChart             graph;
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
        temperature.setText( "Temperature is " + roundToOneDecimalPlace( weather.getTemperature() ) + "°C." );
        feelsLike.setText( "Feels like " + roundToOneDecimalPlace( weather.getFeelsLike() ) + "°C." );
        humidity.setText( "Humidity is " + Short.toString( weather.getHumidity() ) + "%." );
        windSpeed.setText( "Wind speed is " + roundToOneDecimalPlace( weather.getWindSpeed() ) + " m/s." );
        weatherState.setText( "Weather is " + weather.getweather() + "." );
        population.setText( "The population is " + QuizActivity.formatNumberWithSpaces( municipality.getPopulationData().get( ( short )2022 ) ) + " people." );
        employment.setText( "Employment rate is " + roundToOneDecimalPlace( municipality.getEmploymentData().get( ( short )2022 ) ) + "%." );
        workplace.setText( "Workplace self-sufficiency is " + roundToOneDecimalPlace( municipality.getWorkplaceData().get( ( short )2022 ) ) + "%." );
        drawGraph( municipality );
    }

    private void drawGraph( Municipality municipality )
    {

        ArrayList< Entry > values = new ArrayList<>();
        for ( Map.Entry< Short, Integer > set : municipality.getPopulationData().entrySet() )
        {
            values.add( new Entry( set.getKey(), set.getValue() ) );
        }
        ValueFormatter xAxisFormatter = new ValueFormatter()
        {
            @Override
            public String getFormattedValue( float value )
            {
                return String.valueOf( ( int )value ); // Cast to int to remove decimal part
            }
        };
        graph.getXAxis().setValueFormatter( xAxisFormatter );
        LineDataSet dataSet = new LineDataSet( values, "Population" );
        dataSet.setAxisDependency( YAxis.AxisDependency.LEFT );
        dataSet.setColor( Color.BLACK );
        dataSet.setLineWidth( 1.0f );
        dataSet.enableDashedLine( 10.0f, 5.0f, 0.0f );
        dataSet.setDrawCircles( true );
        dataSet.setDrawCircleHole( false );
        dataSet.setDrawValues( false );
        dataSet.setCircleColor( Color.BLACK );
        dataSet.setCircleRadius( 3.0f );

        // Create a nice red-white gradient for our graph.
        GradientDrawable gradient = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{ Color.RED, Color.WHITE }
        );
        dataSet.setFillDrawable( gradient );
        dataSet.setDrawFilled( true );

        // Remove useless information we don't need.
        graph.getDescription().setEnabled( false );
        graph.getAxisLeft().setDrawGridLines( false );
        graph.getXAxis().setDrawGridLines( false );
        graph.getAxisRight().setDrawGridLines( false );
        graph.getLegend().setEnabled( false );
        dataSet.setDrawValues( false );

        LineData lineData = new LineData( dataSet );
        graph.setData( lineData );
        graph.invalidate(); // refresh
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
        employment       = view.findViewById( R.id.employment_text_view );
        workplace        = view.findViewById( R.id.workplace_text_view );
        graph            = view.findViewById( R.id.populationGraph );
        return view;
    }


    public interface OnButtonClickListener
    {
        void onButtonClick( Municipality municipality, Weather weather );
    }
}