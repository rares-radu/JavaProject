package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.FragmentContainerView;

public class SearchActivity extends AppCompatActivity implements MunicipalityInfoFragment.OnButtonClickListener, MunicipalityBriefInfoFragment.OnButtonClickListenerBrief
{
    static Municipality currentValueMunicipality;
    static Weather      currentValueWeather;
    Button                searchButton;
    FragmentContainerView fragmentContainerView;
    SwitchCompat          switchCompat;
    EditText              searchEditText;

    @Override
    public void onButtonClick( Municipality municipality, Weather weather )
    {
        MunicipalityInfoFragment fragment = ( MunicipalityInfoFragment )getSupportFragmentManager().findFragmentById( R.id.fragment_container_view );
        if ( fragment != null && fragment.isAdded() )
        {
            fragment.updateTextView( municipality, weather );
        }
    }

    @Override
    public void onButtonClickBrief( Municipality municipality, Weather weather )
    {
        MunicipalityBriefInfoFragment fragment = ( MunicipalityBriefInfoFragment )getSupportFragmentManager().findFragmentById( R.id.fragment_container_view );
        if ( fragment != null && fragment.isAdded() )
        {
            fragment.updateTextViewBrief( municipality, weather );
        }
    }

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_search );
        searchEditText        = findViewById( R.id.search_edit_text );
        searchButton          = findViewById( R.id.searchButton );
        fragmentContainerView = findViewById( R.id.fragment_container_view );
        switchCompat          = findViewById( R.id.search_switch );

        searchButton.setOnClickListener( view ->
        {
            String prompt = searchEditText.getText().toString();
            MunicipalityRetriever.getMunicipality( prompt, new RetrieverListener< Municipality >()
            {
                @Override
                public void onSuccess( Municipality valueMunicipality )
                {
                    WeatherRetriever.getWeather( prompt, new RetrieverListener< Weather >()
                    {
                        @Override
                        public void onSuccess( Weather valueWeather )
                        {
                            if ( switchCompat.isChecked() )
                            {
                                // Second fragment is visible
                                if ( ( valueWeather == null ) || ( valueMunicipality == null ) )
                                {
                                    fragmentContainerView.setVisibility( View.INVISIBLE );
                                    Toast.makeText( SearchActivity.this, "Try another municipality name", Toast.LENGTH_SHORT ).show();
                                }
                                else
                                {
                                    currentValueMunicipality = valueMunicipality;
                                    currentValueWeather      = valueWeather;
                                    onButtonClickBrief( valueMunicipality, valueWeather );
                                    fragmentContainerView.setVisibility( View.VISIBLE );
                                }
                            }
                            else
                            {
                                // First fragment is visible
                                if ( ( valueWeather == null ) || ( valueMunicipality == null ) )
                                {
                                    fragmentContainerView.setVisibility( View.INVISIBLE );
                                    Toast.makeText( SearchActivity.this, "Try another municipality name", Toast.LENGTH_SHORT ).show();
                                }
                                else
                                {
                                    currentValueMunicipality = valueMunicipality;
                                    currentValueWeather      = valueWeather;
                                    onButtonClick( valueMunicipality, valueWeather );
                                    fragmentContainerView.setVisibility( View.VISIBLE );
                                }
                            }
                        }

                        @Override
                        public void onFailure( Exception e )
                        {
                            fragmentContainerView.setVisibility( View.INVISIBLE );
                            Toast.makeText( SearchActivity.this, "Try another municipality name", Toast.LENGTH_SHORT ).show();
                        }
                    } );
                }

                @Override
                public void onFailure( Exception e )
                {
                    fragmentContainerView.setVisibility( View.INVISIBLE );
                    Toast.makeText( SearchActivity.this, "Try another municipality name", Toast.LENGTH_SHORT ).show();
                }
            } );
        } );

        switchCompat.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged( CompoundButton compoundButton, boolean isChecked )
            {
                if ( isChecked )
                {
                    MunicipalityBriefInfoFragment fragment = new MunicipalityBriefInfoFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace( R.id.fragment_container_view, fragment, "brief_fragment" )
                            .setReorderingAllowed( true )
                            .addToBackStack( null )
                            .commit();
                    switchCompat.setText( "Brief" );
                    String prompt = searchEditText.getText().toString();
                    fragmentContainerView.setVisibility( View.INVISIBLE );
                    MunicipalityRetriever.getMunicipality( prompt, new RetrieverListener< Municipality >()
                    {
                        @Override
                        public void onSuccess( Municipality valueMunicipality )
                        {
                            WeatherRetriever.getWeather( prompt, new RetrieverListener< Weather >()
                            {
                                @Override
                                public void onSuccess( Weather valueWeather )
                                {
                                    onButtonClickBrief( valueMunicipality, valueWeather );
                                    fragmentContainerView.setVisibility( View.VISIBLE );
                                }

                                @Override
                                public void onFailure( Exception e )
                                {
                                    fragmentContainerView.setVisibility( View.INVISIBLE );
                                    Toast.makeText( SearchActivity.this, "Try another municipality name", Toast.LENGTH_SHORT ).show();
                                }
                            } );
                        }

                        @Override
                        public void onFailure( Exception e )
                        {
                            fragmentContainerView.setVisibility( View.INVISIBLE );
                            Toast.makeText( SearchActivity.this, "Try another municipality name", Toast.LENGTH_SHORT ).show();
                        }
                    } );
                }
                else
                {
                    MunicipalityInfoFragment fragment = new MunicipalityInfoFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace( R.id.fragment_container_view, fragment, "info_fragment" )
                            .setReorderingAllowed( true )
                            .addToBackStack( null )
                            .commit();
                    fragmentContainerView.setVisibility( View.INVISIBLE );
                    switchCompat.setText( "Detailed" );
                    String prompt = searchEditText.getText().toString();
                    MunicipalityRetriever.getMunicipality( prompt, new RetrieverListener< Municipality >()
                    {
                        @Override
                        public void onSuccess( Municipality valueMunicipality )
                        {
                            WeatherRetriever.getWeather( prompt, new RetrieverListener< Weather >()
                            {
                                @Override
                                public void onSuccess( Weather valueWeather )
                                {
                                    onButtonClick( valueMunicipality, valueWeather );
                                    fragmentContainerView.setVisibility( View.VISIBLE );
                                }

                                @Override
                                public void onFailure( Exception e )
                                {
                                    fragmentContainerView.setVisibility( View.INVISIBLE );
                                    Toast.makeText( SearchActivity.this, "Try another municipality name", Toast.LENGTH_SHORT ).show();
                                }
                            } );
                        }

                        @Override
                        public void onFailure( Exception e )
                        {
                            fragmentContainerView.setVisibility( View.INVISIBLE );
                            Toast.makeText( SearchActivity.this, "Try another municipality name", Toast.LENGTH_SHORT ).show();
                        }
                    } );
                }
            }
        } );
    }
}
