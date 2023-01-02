package com.statuses.flavoured.welcome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.statuses.flavoured.R;

public class SliderAdapter extends PagerAdapter {
    /**
     * declare the context variables
     * @return
     */

    Context mContext;
    LayoutInflater layoutInflater;

    /**
     * go ahead and generate the constructor of the class
     * @param mContext
     */
    public SliderAdapter(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * We use arrays to add images to our slider.
     * @return
     */

    int imagesone[] = {
            R.drawable.music,
            R.drawable.wedding
    };

    int imagestwo[] = {
            R.drawable.fish,
            R.drawable.node
    };

    int imagesthree[] = {
            R.drawable.face,
            R.drawable.interior
    };

    int imagesfour[] = {
            R.drawable.skate,
            R.drawable.woman
    };

    int imagesfive[] = {
            R.drawable.car,
            R.drawable.jet
    };

    /**
     * We then import the neccessary overidden methods
     * @return
     */
    @Override
    public int getCount() {
        /**
         * We return the number of slides we will be displaying
         */
        return imagesfive.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        /**
         * we go ahead to inflate our slider layout
         */

        layoutInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        /**
         * We then declare what we have been using.
         */

        ImageView imageone = (ImageView) view.findViewById(R.id.imageone);
        ImageView imagetwo = (ImageView) view.findViewById(R.id.imagetwo);
        ImageView imagethree = (ImageView) view.findViewById(R.id.imagethree);
        ImageView imagefour = (ImageView) view.findViewById(R.id.imagefour);
        ImageView imagefive = (ImageView) view.findViewById(R.id.imagefive);

        /**
         * We then set the position for the images
         */

        imageone.setImageResource(imagesone[position]);
        imagetwo.setImageResource(imagestwo[position]);
        imagethree.setImageResource(imagesthree[position]);
        imagefour.setImageResource(imagesfour[position]);
        imagefive.setImageResource(imagesfive[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
