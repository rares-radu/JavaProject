package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class QuizActivity extends AppCompatActivity
{
    static        int                 correctCounter;
    private final ArrayList< String > cities = new ArrayList< String >()
    {{
        add( "Helsinki" );
        add( "Lahti" );
        add( "Turku" );
        add( "Lappeenranta" );
        add( "Rovaniemi" );
        add( "Tampere" );
        add( "Oulu" );
        add( "Jyväskylä" );
        add( "Kuopio" );
        add( "Pori" );
        add( "Joensuu" );
        add( "Vaasa" );
        add( "Seinäjoki" );
        add( "Hämeenlinna" );
        add( "Porvoo" );
    }};
    int current;
    private TextView    question;
    private TextView    questionNumber;
    private RadioGroup  radioGroup;
    private RadioButton firstOption, secondOption, thirdOption;
    private Button          answerButton;
    private List< Integer > order;
    private int             correct;

    static String formatNumberWithSpaces( int x )
    {
        DecimalFormat formatter = new DecimalFormat( "#,###" );
        formatter.setGroupingSize( 3 );
        formatter.setGroupingUsed( true );
        formatter.setDecimalSeparatorAlwaysShown( false );

        String formattedNumber = formatter.format( x );
        formattedNumber = formattedNumber.replace( ",", " " );

        return formattedNumber;
    }

    private void generateQuestion()
    {
        Log.i( "ALOOO", "EVERYTHING STARTED" );
        if ( current == 10 )
        {
            Intent intent = new Intent( QuizActivity.this, QuizResultActivity.class );
            startActivity( intent );
            return;
        }
        String cityName = cities.get( order.get( current ) );

        MunicipalityRetriever.getMunicipality( cityName, new RetrieverListener< Municipality >()
        {
            @Override
            public void onSuccess( Municipality municipality )
            {
                String newQuestion = "What is the population of " + cityName + "?";
                Log.i( "Municipality", "Name: " + municipality.getName() );
                Log.i( "PopulationData", "Data: " + municipality.getPopulationData() );
                question.setText( newQuestion );
                String newQuestionNumber = current + 1 + "/10";
                questionNumber.setText( newQuestionNumber );
                List< Integer > options = Arrays.asList( R.id.quiz_option1_radio_button, R.id.quiz_option2_radio_button, R.id.quiz_option3_radio_button );
                Random          random  = new Random();
                correct = random.nextInt( 3 );
                int         correctOption     = options.get( correct );
                RadioButton correctButton     = findViewById( correctOption );
                String      correctPopulation = formatNumberWithSpaces( municipality.getPopulationData().get( ( short )2022 ) );
                correctButton.setText( correctPopulation );
                for ( int optionId : options )
                {
                    if ( optionId != correctOption )
                    {
                        RadioButton currentOption = findViewById( optionId );
                        String      population    = formatNumberWithSpaces( random.nextInt( 1255283 ) + 50610 );
                        currentOption.setText( population );
                    }
                }
            }

            @Override
            public void onFailure( Exception e )
            {
                Log.i( "API ERROR", "SOMETHING WENT WRONG WITH " + cityName );
            }
        } );
    }

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_quiz );
        current        = 0;
        correctCounter = 0;
        question       = findViewById( R.id.quiz_question_text_view );
        questionNumber = findViewById( R.id.quiz_question_number_text_view );
        radioGroup     = findViewById( R.id.quiz_radio_group );
        firstOption    = findViewById( R.id.quiz_option1_radio_button );
        secondOption   = findViewById( R.id.quiz_option2_radio_button );
        thirdOption    = findViewById( R.id.quiz_option3_radio_button );
        answerButton   = findViewById( R.id.quiz_answer_button );

        order = IntStream.rangeClosed( 0, 14 ).boxed().collect( Collectors.toList() );
        Collections.shuffle( order );
        order.subList( 10, 15 ).clear();

        generateQuestion();
        answerButton.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View view )
            {
                if ( radioGroup.getCheckedRadioButtonId() == firstOption.getId() )
                {
                    current++;
                    if ( correct == 0 )
                    {
                        correctCounter++;
                        Toast.makeText( QuizActivity.this, "Correct", Toast.LENGTH_SHORT ).show();
                    }
                    else
                    {
                        Toast.makeText( QuizActivity.this, "Wrong", Toast.LENGTH_SHORT ).show();
                    }
                    generateQuestion();
                }

                if ( radioGroup.getCheckedRadioButtonId() == secondOption.getId() )
                {
                    current++;
                    if ( correct == 1 )
                    {
                        correctCounter++;
                        Toast.makeText( QuizActivity.this, "Correct", Toast.LENGTH_SHORT ).show();
                    }
                    else
                    {
                        Toast.makeText( QuizActivity.this, "Wrong", Toast.LENGTH_SHORT ).show();
                    }
                    generateQuestion();
                }

                if ( radioGroup.getCheckedRadioButtonId() == thirdOption.getId() )
                {
                    current++;
                    if ( correct == 2 )
                    {
                        correctCounter++;
                        Toast.makeText( QuizActivity.this, "Correct", Toast.LENGTH_SHORT ).show();
                    }
                    else
                    {
                        Toast.makeText( QuizActivity.this, "Wrong", Toast.LENGTH_SHORT ).show();
                    }
                    generateQuestion();
                }
                radioGroup.clearCheck();
            }
        } );
    }
}
