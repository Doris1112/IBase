package com.doris.ibase.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Doris
 * @date 2019/10/17
 */
@SuppressWarnings("WeakerAccess")
public class IFragmentAdapter extends FragmentPagerAdapter {

    private FragmentManager mFragmentManager;
    private final List<Fragment> mFragments;
    private final List<String> mTitles;

    public IFragmentAdapter(@NonNull FragmentManager fm) {
        this(fm, 0);
    }

    public IFragmentAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        mFragmentManager = fm;
        mFragments = new ArrayList<>();
        mTitles = new ArrayList<>();
    }

    @NonNull
    @Override
    public Fragment getItem(int i) {
        return mFragments.get(i);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitles.size() == mFragments.size()) {
            return mTitles.get(position);
        }
        return super.getPageTitle(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        // 为让notifyDataSetChanged生效
        return POSITION_NONE;
    }

    public void add(Fragment fragment) {
        mFragments.add(fragment);
    }

    public void add(Fragment fragment, String title) {
        mFragments.add(fragment);
        mTitles.add(title);
    }

    /**
     * 刷新某个Fragment
     */
    public void updateFragment(Fragment fragment) {
        int index = -1;
        if (mFragments != null) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            for (int i = 0; i < mFragments.size(); i++) {
                if (fragment.getClass().getName().equals(
                        mFragments.get(i).getClass().getName())) {
                    index = i;
                    transaction.remove(mFragments.get(i));
                }
            }
            transaction.commit();
            transaction = null;
            mFragmentManager.executePendingTransactions();
        }
        if (index >= 0) {
            mFragments.set(index, fragment);
            notifyDataSetChanged();
        }
    }

}
