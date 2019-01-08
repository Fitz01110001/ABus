package com.fitz.abus.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fitz.abus.R;
import com.fitz.abus.adapter.FragmentListAdapter;
import com.fitz.abus.bean.BusLineDB;
import com.fitz.abus.fitzview.FitzRecyclerView;
import com.fitz.abus.utils.FitzDBUtils;
import com.fitz.abus.utils.MessageEvent;
import com.fitz.abus.utils.OnSlideItemTouch;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @ProjectName: ABus
 * @Package: com.fitz.abus.base
 * @ClassName: BaseFragment
 * @Author: Fitz
 * @CreateDate: 2018/12/20 13:10
 */
public class BaseFragment extends Fragment {

    public static final String TAG = "fitz"+BaseFragment.class.getSimpleName();
    private static final boolean isDebug = true;
    private static final String ARG_TAG = "arg_tag";
    public Activity mActivity;
    private String defaultCityKey;
    private View mRootView;
    private LinearLayout linearLayout;
    private FitzRecyclerView fg_container;
    private static FragmentListAdapter fragmentListAdapter;
    private Unbinder mUnBinder;
    private List<BusLineDB> currentBusList = new ArrayList<>();
    private final String DELETE = "delete";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FLOG("onCreate");
        mActivity = getActivity();
        defaultCityKey = getArguments().getString(ARG_TAG);
        updateList();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutResId(), container, false);
        mUnBinder = ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fg_container = mRootView.findViewById(R.id.fg_container);
        fragmentListAdapter = new FragmentListAdapter(mActivity, currentBusList);
        initRecycleView();
        /*linearLayout = mRootView.findViewById(R.id.fg_container);
        for (BusLineDB busline : currentBusList) {
            addBusCard(busline);
        }*/
    }

    private void initRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        //设置布局管理器
        fg_container.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置Adapter
        fg_container.setAdapter(fragmentListAdapter);
        //设置分隔线
        fg_container.addItemDecoration(new DividerItemDecoration(mActivity, OrientationHelper.VERTICAL));
        //设置增加或删除条目的动画
        fg_container.setItemAnimator(new DefaultItemAnimator());
        fg_container.addOnItemTouchListener(new OnSlideItemTouch(mActivity));
    }

    @Override
    public void onStart() {
        super.onStart();
        FLOG("onStart");
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        FLOG("onResume");
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /*public void addBusCard(BusLineDB busline) {
        FitzBusCard card = new FitzBusCard(mActivity, busline);
        linearLayout.addView(card);
    }*/

    private void updateList(){
        currentBusList.clear();
        currentBusList.addAll(FitzDBUtils.getInstance().queryRawBusWhereCityID(defaultCityKey));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        FLOG("onMessageEvent");
        if(DELETE.equals(event.message)){
            updateList();
            fragmentListAdapter.notifyDataSetChanged();
        }
    }

    public int getLayoutResId() {
        return R.layout.fragment_main;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FLOG("onDestroyView");
        mUnBinder.unbind();
    }

    public void FLOG(String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
        }
    }

}
