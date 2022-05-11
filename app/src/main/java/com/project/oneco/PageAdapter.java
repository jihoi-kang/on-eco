package com.project.oneco;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class PageAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> mData;
    public PageAdapter(@NonNull FragmentManager fm) {
        super(fm);
        // 페이지 항목들을 ArrayList에 추가
        mData = new ArrayList<>();
        mData.add(new SetTrashSatistic());
        mData.add(new ItemFragment());
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position){
        return(position + 1) + "번째";
    }

    @NonNull
    @Override
    public Fragment getItem(int position){
        return mData.get(position);
    }

    @Override
    public int getCount(){
        return mData.size();
    }

}
