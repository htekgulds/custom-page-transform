package com.example.deneme;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HASAN TEKGÜL on 12/4/2014.
 */
public class CustomFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> pages = new ArrayList<>();

    public CustomFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return pages.get(position);
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    public int addPage(Fragment frg) {
        return addPage(frg, pages.size());
    }

    public int addPage(Fragment frg, int position) {
        pages.add(position, frg);
        notifyDataSetChanged();
        return position;
    }

    public int removePage(int position) {
        pages.remove(position);
        notifyDataSetChanged();
        return position;
    }

    /**
     * -------------------------------------------------------------------
     * Bu metod olmazsa son sayfa silinemiyor ama sayfaya geçilmesi de engelleniyor.
     * -------------------------------------------------------------------
     * @param object
     * @return
     */
    @Override
    public int getItemPosition(Object object) {
        int i = pages.indexOf(object);
        if (i == -1) {
            return POSITION_NONE;
        }
        else return i;
    }
}
