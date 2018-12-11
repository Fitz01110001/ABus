package com.fitz.abus.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {

    public static final String   TAG = BaseFragment.class.getSimpleName();
    private             View     mRootView;
    private             Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutResId(), container, false);
        mUnbinder = ButterKnife.bind(this, mRootView);
        init(savedInstanceState);
        return mRootView;
    }

    public abstract void init(Bundle savedInstanceState);

    public abstract int getLayoutResId();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

}
