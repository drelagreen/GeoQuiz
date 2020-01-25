package com.crafsed.geoquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AnswersActivity extends AppCompatActivity {
    double mPercents = 0;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedInstanceState = getIntent().getExtras();
        setContentView(R.layout.activity_answers);
        TextView mPercentsTextView = findViewById(R.id.mPercents);
        Button mTryAgainButton = findViewById(R.id.mTryAgainButton);
        if (savedInstanceState!=null)
        mPercents = savedInstanceState.getDouble("PERCENTS");
        @SuppressLint("DefaultLocale") String s = String.format("%.2f", mPercents);
        mPercentsTextView.setText(s+"%");
        if (mPercents >= 85 ) {
            mPercentsTextView.setTextColor(getResources().getColor(R.color.green));
        } else if (mPercents>=48){
            mPercentsTextView.setTextColor(getResources().getColor(R.color.yellow));
        } else
            mPercentsTextView.setTextColor(getResources().getColor(R.color.red));



        mTryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AnswersActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }



    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble("PERCENTS",mPercents);
    }
}
