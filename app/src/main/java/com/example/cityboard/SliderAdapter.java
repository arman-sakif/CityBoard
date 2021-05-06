package com.example.cityboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapter extends PagerAdapter{

    Context context;
    LayoutInflater layoutInflater;
    String[] slideDesc;

    public SliderAdapter(Context context, String[] slideDesc) {
        this.context = context;
        this.slideDesc = slideDesc;
    }

    public int[] slideImages = {R.drawable.ic_shopping_cart, R.drawable.ic_house, R.drawable.ic_school, R.drawable.ic_people};

    public String[] slideHeadings = {"Buy&Sell","To Let","Tuition Wanted","Community"};


    //public String[] slideDesc = {"Want to sell something that you do not need anymore? \n    Or want to buy used products at a low price? \n         Ask the community. ", "Want to sell something that you do not need anymore? \\n    Or want to buy used products at a low price? \\n         Ask the community ", "Want to sell something that you do not need anymore? \\n    Or want to buy used products at a low price? \\n         Ask the community ", "Want to sell something that you do not need anymore? \\n    Or want to buy used products at a low price? \\n         Ask the community "};

    @Override
    public int getCount() {
        return slideHeadings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater)  context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.slideImageViewID);
        TextView slideTextView = (TextView) view.findViewById(R.id.slideTextViewID);
        TextView slideDescTextView = (TextView) view.findViewById(R.id.slideDescTextViewID);

        slideImageView.setImageResource(slideImages[position]);
        slideTextView.setText(slideHeadings[position]);
        slideDescTextView.setText(slideDesc[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
