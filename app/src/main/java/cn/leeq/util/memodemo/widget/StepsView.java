package cn.leeq.util.memodemo.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.leeq.util.memodemo.R;


public class StepsView extends LinearLayout implements StepsViewIndicator.OnDrawListener {

    private StepsViewIndicator mStepsViewIndicator;
    private FrameLayout mLabelsLayout;
    private List<String> mLabels;
    private List<String> mLabelsDate;
    private int mProgressColorIndicator = Color.YELLOW;
    private int mLabelColorIndicator = Color.BLACK;
    private int mBarColorIndicator = Color.BLACK;
    private int mCompletedPosition = 0;
    private FrameLayout mDateLayout;

    public StepsView(Context context) {
        this(context, null);
    }

    public StepsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepsView(Context context, AttributeSet attrs,
            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.widget_steps_view, this);
        mStepsViewIndicator = (StepsViewIndicator) rootView.findViewById(R.id.steps_indicator_view);
        mStepsViewIndicator.setDrawListener(this);
        mLabelsLayout = (FrameLayout) rootView.findViewById(R.id.labels_container);
        mDateLayout = (FrameLayout) rootView.findViewById(R.id.labels_date_container);
    }


    public StepsView setLabels(List<String> labels) {
        mLabels = labels;
        mStepsViewIndicator.setStepSize(labels.size());
        return this;
    }

    public StepsView setLabelsDate(List<String> labelsDate) {
        mLabelsDate = labelsDate;
        return this;
    }

    public StepsView setProgressColorIndicator(int progressColorIndicator) {
        mProgressColorIndicator = progressColorIndicator;
        mStepsViewIndicator.setProgressColor(mProgressColorIndicator);
        return this;
    }


    public StepsView setLabelColorIndicator(int labelColorIndicator) {
        mLabelColorIndicator = labelColorIndicator;
        return this;
    }


    public StepsView setBarColorIndicator(int barColorIndicator) {
        mBarColorIndicator = barColorIndicator;
        mStepsViewIndicator.setBarColor(mBarColorIndicator);
        return this;
    }

    public int getCompletedPosition() {
        return mCompletedPosition;
    }

    public StepsView setCompletedPosition(int completedPosition) {
        mCompletedPosition = completedPosition;
        mStepsViewIndicator.setCompletedPosition(mCompletedPosition);
        return this;
    }

    public void drawView() {
        if (mLabels == null || mLabelsDate == null) {
            throw new IllegalArgumentException("labels must not be null.");
        }

        if (mCompletedPosition < 0 || mCompletedPosition > mLabels.size() - 1) {
            throw new IndexOutOfBoundsException(String.format("Labels Index : %s, Size : %s", mCompletedPosition, mLabels.size()));
        }

        mStepsViewIndicator.invalidate();
    }

    @Override
    public void onReady() {
        drawLabels();
    }

    private void drawLabels() {
        List<Float> indicatorPosition = mStepsViewIndicator.getThumbContainerXPosition();
        if (mLabels != null) {
            for (int i = 0; i < mLabels.size(); i++) {
                //标准指示
                TextView textView = new TextView(getContext());
                textView.setText(mLabels.get(i));
                textView.setTextSize(8);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setX(indicatorPosition.get(i)+3);
                textView.setLayoutParams(
                        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));

                /*if (i <= mCompletedPosition) {
                    textView.setTextColor(mLabelColorIndicator);
                    textView.setTypeface(null, Typeface.BOLD);
                } else {
                    textView.setTextColor(Color.parseColor("#DBDBDB"));
                }*/
                textView.setTextColor(Color.parseColor("#DBDBDB"));
                mLabelsLayout.addView(textView);
            }
        }
        if (mLabelsDate != null) {
            for (int i = 0; i < mLabelsDate.size(); i++) {
                //日期
                TextView tv = new TextView(getContext());
                tv.setText(mLabelsDate.get(i));
                tv.setTextSize(8);
                tv.setGravity(Gravity.CENTER_HORIZONTAL);
                tv.setX(indicatorPosition.get(i)+3);
                tv.setLayoutParams(
                        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                tv.setTextColor(Color.parseColor("#DBDBDB"));
                mDateLayout.addView(tv);
            }
        }
    }
}
