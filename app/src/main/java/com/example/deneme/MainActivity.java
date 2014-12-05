package com.example.deneme;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

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
        pager.setPageTransformer(false, new CustomPageTransformer());

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

    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() == 0) {
            super.onBackPressed();
        }
        else {
            pager.setCurrentItem(pager.getCurrentItem() - 1, true);
        }
    }
}