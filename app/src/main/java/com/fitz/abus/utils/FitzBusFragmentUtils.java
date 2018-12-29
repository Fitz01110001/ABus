package com.fitz.abus.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.fitz.abus.R;
import com.fitz.abus.base.BaseFragment;

/**
 * @ProjectName: ABus
 * @Package: com.fitz.abus.utils
 * @ClassName: FitzBusFragmentUtils
 * @Author: Fitz
 * @CreateDate: 2018/12/28
 */
public class FitzBusFragmentUtils {

    private AppCompatActivity activity;
    private static final String ARG_TAG = "arg_tag";
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private static FitzBusFragmentUtils fitzBusFragmentUtils;

    public FitzBusFragmentUtils(AppCompatActivity activity) {
        this.activity = activity;
        manager = activity.getSupportFragmentManager();
        transaction = manager.beginTransaction();
    }

    public void addFragment(Fragment fragment, String tag) {
        transaction.add(R.id.main_fragment_container, fragment, tag);
        transaction.commit();
    }

    public void replaceFragment(Fragment fragment, String tag){
        transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment_container, fragment,tag)
                .show(fragment).commit();
    }


}
