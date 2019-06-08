package com.hardcopy.blechat.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;


import com.hardcopy.blechat.FirstStepActivity;
import com.hardcopy.blechat.fragments.FirstStepFragment;

import java.util.ArrayList;

public class ViewPagerFragmentAdapter extends FragmentStateAdapter {

    private ArrayList<Fragment> arrayList = new ArrayList<>();


    public ViewPagerFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }


    public void addFragment(Fragment fragment) {
        arrayList.add(fragment);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return arrayList.get(position);

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public FirstStepFragment getItem(int position){
        return (FirstStepFragment) arrayList.get(position);
    }



}

