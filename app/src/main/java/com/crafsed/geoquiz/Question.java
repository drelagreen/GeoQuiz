package com.crafsed.geoquiz;

public class Question {
    private int mQuestionResId;
    private boolean mQuestionAnswerTrue;
    public Question(int questionResId, boolean questionAnswerTrue){
        mQuestionAnswerTrue = questionAnswerTrue;
        mQuestionResId = questionResId;
    }

    public boolean isQuestionAnswerTrue() {
        return mQuestionAnswerTrue;
    }

    public int getQuestionResId() {
        return mQuestionResId;
    }

    public void setQuestionAnswerTrue(boolean questionAnswerTrue) {
        mQuestionAnswerTrue = questionAnswerTrue;
    }

    public void setQuestionResId(int questionResId) {
        mQuestionResId = questionResId;
    }

}
