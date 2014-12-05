package com.example.deneme;

import android.view.View;

import com.nineoldandroids.view.ViewHelper;

/**
 * Created by HASAN TEKGÜL on 12/5/2014.
 */
public class CustomPageTransformer implements CustomViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.85f;

    @Override
    public void transformPage(View page, float position) {
        int pageWidth = page.getWidth();

        if (position < -1) {
            // For pages out of screen:
            // Make them disappear
            ViewHelper.setAlpha(page, 0);
        }
        else if(position <= 0) {
            // For pages going out of screen:
            // Make them stay where they are and scale them down
            // as if they are going back in Z direction
            float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
            ViewHelper.setAlpha(page, 1 - position);
            ViewHelper.setTranslationX(page, pageWidth * -position);
            ViewHelper.setScaleX(page, scaleFactor);
            ViewHelper.setScaleY(page, scaleFactor);
        }
        else if(position <= 1) {
            // For pages coming into the screen:
            // Make them come in as usual but should be on top
            ViewHelper.setAlpha(page, 1);
            ViewHelper.setTranslationX(page, 0);
            ViewHelper.setScaleX(page, 1);
            ViewHelper.setScaleX(page, 1);
        }
        else {
            ViewHelper.setAlpha(page, 0);
        }
    }
}
