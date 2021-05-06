package com.example.cityboard;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class OnBoardScreenActivity extends AppCompatActivity {

    private ViewPager obsViewPager;
    private LinearLayout obsLinearLayout;
    private SliderAdapter sliderAdapter;
    private TextView[] mDots;
    private Button previousButton, nextButton;
    private int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board_screen);

        getSupportActionBar().hide();
        obsViewPager = (ViewPager) findViewById(R.id.onBoardScreenViewPagerLayoutID);
        obsLinearLayout = (LinearLayout) findViewById(R.id.onBoardScreenLinearLayoutID);

        previousButton = (Button) findViewById(R.id.previousButtonID);
        nextButton = (Button) findViewById(R.id.nextButtonID);

        String[] slideDesc = getResources().getStringArray(R.array.slide_desc);
        sliderAdapter = new SliderAdapter(this, slideDesc);

        obsViewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);

        obsViewPager.addOnPageChangeListener(viewListener);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrentPage +1 == mDots.length){
                    OnBoardScreenActivity.super.finish();
                    Intent intent = new Intent(OnBoardScreenActivity.this, LoginSignUpActivity.class);
                    startActivity(intent);
                }else
                obsViewPager.setCurrentItem(mCurrentPage + 1);

            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obsViewPager.setCurrentItem(mCurrentPage - 1);
            }
        });



    }

    public void addDotsIndicator(int position){
        mDots = new TextView[4];
        obsLinearLayout.removeAllViews();
        for(int i=0; i<mDots.length; i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(40);
            mDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));
            obsLinearLayout.addView(mDots[i]);
        }

        if(mDots.length>0){
            mDots[position].setTextColor(getResources().getColor(R.color.white));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            mCurrentPage = position;
            if (position == 0) {

                nextButton.setEnabled(true);
                previousButton.setEnabled(false);
                previousButton.setVisibility(View.INVISIBLE);
                nextButton.setText("Next");
                previousButton.setText("");

            } else if (position == mDots.length - 1) {
                nextButton.setEnabled(true);
                previousButton.setEnabled(true);
                previousButton.setVisibility(View.VISIBLE);
                nextButton.setText("Finish");
                previousButton.setText("Previous");

            } else {
                nextButton.setEnabled(true);
                previousButton.setEnabled(true);
                previousButton.setVisibility(View.VISIBLE);
                nextButton.setText("Next");
                previousButton.setText("Previous");

            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}