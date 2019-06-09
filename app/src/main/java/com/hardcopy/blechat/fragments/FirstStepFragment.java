package com.hardcopy.blechat.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;


import com.google.android.material.textfield.TextInputEditText;
import com.hardcopy.blechat.FirstStepActivity;
import com.hardcopy.blechat.MainActivity;
import com.hardcopy.blechat.R;
import com.hardcopy.blechat.adapters.ViewPagerFragmentAdapter;
import com.hardcopy.blechat.databinding.FragmentFirststepBinding;
import com.hardcopy.blechat.setting.UserSetting;

public class FirstStepFragment extends Fragment {

    private String message;
    private boolean isEditextOn = false;
    private TextInputEditText intputtext;

    public FragmentFirststepBinding layout;
    String hintMsg;




    public FirstStepFragment(){

    }

    public FirstStepFragment(String message){
        this.message = message;
    }

    public FirstStepFragment(String message , boolean iseditextOn){
        this(message);
        this.isEditextOn = iseditextOn;

    }

    public FirstStepFragment(String message ,String hintMsg, boolean iseditextOn){
        this(message , iseditextOn);
        this.hintMsg = hintMsg;
    }

    public String getInputText(){
        return intputtext.getText().toString();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

/*
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_firststep, container, false);
        return view;
*/
        //Data Binding
        layout  = DataBindingUtil.inflate(inflater, R.layout.fragment_firststep,container,false);

        layout.textView.setText(message);

        if(isEditextOn){
            layout.textInputLayout.setVisibility(View.VISIBLE);
            layout.textInputLayout.setHint(hintMsg);
        } else {
            layout.textInputLayout.setVisibility(View.INVISIBLE);
        }

        intputtext =layout.textInputEditText;

        final ViewPager2 pager = FirstStepActivity.viewpager;

        if (pager.getCurrentItem() == 0){
            layout.buttonLeft.setVisibility(View.INVISIBLE);
        } else layout.buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(pager.getCurrentItem() - 1);
            }
        });

        if (pager.getCurrentItem() == 5){
            layout.buttonLeft.setVisibility(View.INVISIBLE);
            layout.buttonRight.setVisibility(View.INVISIBLE);


        }else layout.buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(pager.getCurrentItem() + 1);
            }
        });

        return layout.getRoot();
    }

}
