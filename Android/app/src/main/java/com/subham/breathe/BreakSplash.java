package com.subham.breathe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class BreakSplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.breaksplash);
        setRandowmImage();
    }


    private void setRandowmImage() {
        ArrayList<Exercise> exercises = new ArrayList<Exercise>();
        exercises.add(new Exercise(R.raw.healthyyogafox, "Just Relax and Sit"));
        exercises.add(new Exercise(R.raw.workoutmonkeystayhealthy, "Do some Workout"));
        exercises.add(new Exercise(R.raw.meditate, "Meditate For Now"));
        exercises.add(new Exercise(R.raw.rollingeyes, "Roll Your Eyes"));
        exercises.add(new Exercise(R.raw.drinkingwater, "Drink Water"));
        exercises.add(new Exercise(R.raw.walkman, "Let's Go For Walk"));


        Random r = new Random();
        int randomExercise = r.nextInt(exercises.size());

        ((LottieAnimationView) findViewById(R.id.animatedExercise))
                .setAnimation(exercises.get(randomExercise).resource);

        ((TextView) findViewById(R.id.exerciseMessage)).setText(exercises.get(randomExercise).text);
    }


    public void endBreak(View view) {
        this.finish();
    }
}