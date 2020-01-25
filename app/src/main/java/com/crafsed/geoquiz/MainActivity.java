package com.crafsed.geoquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    double percents = 0;
    final String TAG = "MAIN_ACTIVITY";
    static int sCurrentQuestionIndex;
    Question[] mQuestionsBank;
    TextView mQuestionTextView;
    boolean[] mAnsweredQuestions;
    boolean[] mAnswers;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mTrueButton = findViewById(R.id.mTrueButton);
        Button mFalseButton = findViewById(R.id.mFalseButton);
        Button mNextButton = findViewById(R.id.mNextButton);
        Button mPreviousButton = findViewById(R.id.mPreviousButton);
        Button mResultsButton = findViewById(R.id.mResultsButton);
        final LinearLayout mTopLinearLayout = findViewById(R.id.mTopLinearLayout);


        mQuestionTextView = findViewById(R.id.mQuestionTextView);

        if (savedInstanceState==null)
        sCurrentQuestionIndex = 0;

        mQuestionsBank = new Question[]{
                new Question(R.string.question_australia, true),
                new Question(R.string.question_oceans, true),
                new Question(R.string.question_mideast, false),
                new Question(R.string.question_africa, false),
                new Question(R.string.question_americas, true),
                new Question(R.string.question_asia, true),
        };
        mAnsweredQuestions = new boolean[mQuestionsBank.length];
        mAnswers = new boolean[mQuestionsBank.length];

        Arrays.fill(mAnsweredQuestions,false);
        for (int i = 0; i<mQuestionsBank.length; i++){
            ImageView imageView = new ImageView(MainActivity.this);
            imageView.setImageResource(R.drawable.empty);
            imageView.setId(i);
            imageView.setPadding(10,10,10,10);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            mTopLinearLayout.addView(imageView);
        }
        if (savedInstanceState==null)
        markQuestionAsMain();

        mQuestionTextView.setText(mQuestionsBank[sCurrentQuestionIndex].getQuestionResId());
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               nextQuestion();
            }
        });
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousQuestion();
            }
        });
        View.OnClickListener mClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (v.getId()==R.id.mTrueButton){
                    if (!mAnsweredQuestions[sCurrentQuestionIndex]){
                        markQuestionAsAnsweredTrue();
                        mAnswers[sCurrentQuestionIndex]=true;
                    } else if (mAnswers[sCurrentQuestionIndex]){
                        deMarkQuestionAsAnswered();
                    } else {
                        markQuestionAsAnsweredTrue();
                        mAnswers[sCurrentQuestionIndex]=true;
                    }
                } else {
                    if (!mAnsweredQuestions[sCurrentQuestionIndex]){
                        markQuestionAsAnsweredFalse();
                        mAnswers[sCurrentQuestionIndex]=false;
                    } else if (!mAnswers[sCurrentQuestionIndex]){
                        deMarkQuestionAsAnswered();
                    } else {
                        markQuestionAsAnsweredTrue();
                        mAnswers[sCurrentQuestionIndex]=false;
                    }
                }
            }
        };
        mTrueButton.setOnClickListener(mClickListener);
        mFalseButton.setOnClickListener(mClickListener);
        recreateActivityInfo(savedInstanceState);

        mResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,AnswersActivity.class);
                double summ = 0;
                for (int j = 0; j < mQuestionsBank.length; j++) {
                    if (mAnsweredQuestions[j]){
                        if (mAnswers[j]==mQuestionsBank[j].isQuestionAnswerTrue()){
                            summ+=1;
                        }
                    }
                }
                summ = (summ / mQuestionsBank.length) * 100;
                percents = summ;
                i.putExtra("PERCENTS",percents);
                startActivity(i);
                finish();
            }
        });
    }

    public void nextQuestion(){
        deMarkQuestionAsMain();
        sCurrentQuestionIndex = (sCurrentQuestionIndex + 1) % mQuestionsBank.length;
        setQuestionTextViewFromIndex();
        markQuestionAsMain();
    }

    public void previousQuestion(){
        deMarkQuestionAsMain();
        sCurrentQuestionIndex = (sCurrentQuestionIndex - 1+mQuestionsBank.length) % mQuestionsBank.length;
        setQuestionTextViewFromIndex();
        markQuestionAsMain();
    }
    public void setQuestionTextViewFromIndex(){
        mQuestionTextView.setText(mQuestionsBank[sCurrentQuestionIndex].getQuestionResId());
    }
    void markQuestionAsMain(){
        ((ImageView)findViewById(sCurrentQuestionIndex)).setBackgroundColor(getResources().getColor(R.color.background1));
    }
    void deMarkQuestionAsMain(){
        ((ImageView)findViewById(sCurrentQuestionIndex)).setBackgroundColor(getResources().getColor(R.color.transparent));

    }
    void markQuestionAsAnsweredTrue(){
        ((ImageView)findViewById(sCurrentQuestionIndex)).setImageResource(R.drawable.green);
        mAnsweredQuestions[sCurrentQuestionIndex] = true;
    }
    void markQuestionAsAnsweredFalse(){
        ((ImageView)findViewById(sCurrentQuestionIndex)).setImageResource(R.drawable.red);
        mAnsweredQuestions[sCurrentQuestionIndex] = true;
    }
    void deMarkQuestionAsAnswered(){
        ((ImageView)findViewById(sCurrentQuestionIndex)).setImageResource(R.drawable.empty);
        mAnsweredQuestions[sCurrentQuestionIndex] = false;

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("CURRENT_INDEX",sCurrentQuestionIndex);
        savedInstanceState.putBooleanArray("ANSWERS",mAnswers);
        savedInstanceState.putBooleanArray("ANSWERED_QUESTIONS",mAnsweredQuestions);
    }
    void recreateActivityInfo(Bundle bundle){
        if (bundle!=null&&!bundle.isEmpty()){
            mAnswers = bundle.getBooleanArray("ANSWERS");
            mAnsweredQuestions= bundle.getBooleanArray("ANSWERED_QUESTIONS");
            for(int i = 0; i<mAnswers.length; i++){
                if (mAnsweredQuestions[i]){
                    if (mAnswers[i]){
                        sCurrentQuestionIndex=i;
                        markQuestionAsAnsweredTrue();
                    } else {
                        sCurrentQuestionIndex=i;
                        markQuestionAsAnsweredFalse();
                    }
                }
            }
            sCurrentQuestionIndex = bundle.getInt("CURRENT_INDEX");
            markQuestionAsMain();
        }

    }
}
