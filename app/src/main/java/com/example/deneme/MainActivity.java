package com.example.deneme;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.inject.Inject;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;

import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActionBarActivity {

    /**
     * --------------------------------------------------
     * Injected Views from the activity_main layout
     * with RoboGuice
     * --------------------------------------------------
     */
    @InjectView(R.id.layout)
    FrameLayout layout;
    @InjectView(R.id.pager)
    CustomViewPager pager;
    @InjectView(R.id.btnNew)
    Button btnNew;
    @Inject
    LayoutInflater inflater;
    // Hello World

    /**
     * --------------------------------------------------
     * PageAdapter for the ViewPager
     * --------------------------------------------------
     */
    CustomFragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new CustomFragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(new CustomViewPager.OnPageChangeListener() {

            private int position;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //
            }

            @Override
            public void onPageSelected(int position) {
                this.position = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 0) {
                    if (position < adapter.getCount() - 1) {
                        adapter.removePage(position + 1);
                    }
                }
            }
        });
        // Bu metod'da false gelen yer Drawing Order'ı belirliyor
        // true: ilkten sona,
        // false: sondan ilke doğru çizim.
        // Bu da sayfaların birbiri üzerine geçme sırasını belirliyor
        pager.setPageTransformer(false, new ZoomOutPageTransformer());

        adapter.addPage(new MainFragment());
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPage(new BlankFragment());
            }
        });
    }

    /**
     * Add a new page to the layout
     * @param page
     */
    public void addPage(Fragment page) {
        int index = adapter.addPage(page);
        pager.setCurrentItem(index, true);
    }

    public void removePage(int position) {
        int index = adapter.removePage(position);
        if (index == adapter.getCount()) {
            index --;
        }
        pager.setCurrentItem(index);
    }

    private class SimplePagerAdapter extends PagerAdapter {

        /**
         * -------------------------------------------------------------
         * List of pages
         * This will be dynamically filled and emptied.
         * -------------------------------------------------------------
         */
        private List<View> pages = new ArrayList<>();

        /**
         * -------------------------------------------------------------
         * Position of the page
         * -------------------------------------------------------------
         * @param object
         * @return
         */
        @Override
        public int getItemPosition(Object object) {
            int index = pages.indexOf(object);
            if (index == -1) {
                return POSITION_NONE;
            }
            else {
                return index;
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = pages.get(position);
            container.addView(v);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(pages.get(position));
        }

//        public SimplePagerAdapter(FragmentManager supportFragmentManager) {
//            super(supportFragmentManager);
//        }

//        @Override
//        public Fragment getItem(int position) {
//            return new BlankFragment();
//        }

        @Override
        public int getCount() {
            return pages.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public int addView(View view) {
            return addView(view, pages.size());
        }

        public int addView(View view, int position) {
            pages.add(position, view);
            notifyDataSetChanged();
            return position;
        }

        public int removeView(ViewPager pager, View view) {
            return removeView(pager, pages.indexOf(view));
        }

        // ViewPager doesn't have a delete method; the closest is to set the adapter
        // again.  When doing so, it deletes all its views.  Then we can delete the view
        // from from the adapter and finally set the adapter to the pager again.  Note
        // that we set the adapter to null before removing the view from "views" - that's
        // because while ViewPager deletes all its views, it will call destroyItem which
        // will in turn cause a null pointer ref.
        public int removeView(ViewPager pager, int position) {
            pager.setAdapter(null);
            pages.remove(position);
            pager.setAdapter(this);

            return position;
        }

        public View getView(int position) {
            return pages.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() == 0) {
            super.onBackPressed();
        }
        else {
            pager.setCurrentItem(pager.getCurrentItem() - 1, true);
        }
    }

    private class ZoomOutPageTransformer implements CustomViewPager.PageTransformer {
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
}