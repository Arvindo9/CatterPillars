package com.solution.catterpillars.ui.splash;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.solution.catterpillars.R;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 01-Nov-18
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 * About : I am a mathematician
 * Quote : Only brain can make anything possible
 * Strength : Never give up
 */
public class WelcomeAdapter extends android.support.v4.view.PagerAdapter {

    private Context context;
    private LayoutInflater inflater;

    WelcomeAdapter(Context context){
        this.context = context;
    }

    //Array
    public int[] list_images={
            R.drawable.splash,
            R.drawable.splash2,
            R.drawable.splash3
    };

    public String[] list_title={
            "Phone",
            "Flight",
            "Bus",
            "Train"
    };

    public String[] list_description={
            "Big discounts on Smart Phones",
            "Upto 25% off on Domestic Flights",
            "Enjoy Travelling on bus with flat 100 off",
            "10% cashback on first train booking"
    };
    public int[] list_color={
            Color.rgb(193, 66, 44),
            Color.rgb(193, 172, 44),
            Color.rgb(193, 41, 249),
            Color.rgb(68, 83, 242)

    };

    @Override
    public int getCount() {
        return list_images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view== o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.welcome_slider,container,false);

        RelativeLayout linearLayout = view.findViewById(R.id.slidelinearlayout);
        ImageView img = (ImageView)view.findViewById(R.id.image);

        img.setImageResource(list_images[position]);
//        linearLayout.setBackgroundColor(list_color[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
