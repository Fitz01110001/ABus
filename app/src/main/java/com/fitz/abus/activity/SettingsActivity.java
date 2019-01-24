package com.fitz.abus.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.fitz.abus.FitzApplication;
import com.fitz.abus.R;
import com.fitz.abus.base.BaseActivity;
import com.fitz.abus.fitzview.FitzActionBar;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUILoadingView;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;

/**
 * @ProjectName: ABus
 * @Package: com.fitz.abus.activity
 * @ClassName: SettingsActivity
 * @Author: Fitz
 * @CreateDate: 2019/01/21 23:10
 */
public class SettingsActivity extends BaseActivity {

    @BindView(R.id.settings_fitzactionbar) FitzActionBar settingsFitzactionbar;
    @BindView(R.id.groupListView) QMUIGroupListView mGroupListView;
    private Context context;
    private QMUICommonListItemView itemWithChevron;
    private QMUICommonListItemView itemWithSwitch;
    private static final LinkedHashMap<Long, String> REFRESH_TIME_MAP = new LinkedHashMap<Long, String>() {
        {
            put(10000L, "10s");
            put(15000L, "15s");
            put(20000L, "20s");
            put(25000L, "25s");
            put(30000L, "30s");
        }
    };
    final List<Long> cityCode = new ArrayList<>(REFRESH_TIME_MAP.keySet());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initGroupListView();
    }


    private void initGroupListView() {
        itemWithSwitch = mGroupListView.createItemView(getString(R.string.auto_refresh));
        itemWithSwitch.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_SWITCH);
        itemWithSwitch.getSwitch().setChecked(FitzApplication.getInstance().isAutoRefresh());
        itemWithSwitch.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                FitzApplication.getInstance().setAutoRefresh(isChecked);
            }
        });

        itemWithChevron = mGroupListView.createItemView(getString(R.string.auto_refresh_time));
        itemWithChevron.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        itemWithChevron.setDetailText(REFRESH_TIME_MAP.get(FitzApplication.getInstance().getRefreshTime()));

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof QMUICommonListItemView) {
                    CharSequence text = ((QMUICommonListItemView) v).getText();
                    Toast.makeText(context, text + " is Clicked", Toast.LENGTH_SHORT).show();
                    showSimpleBottomSheetList();
                }
            }
        };

        int size = QMUIDisplayHelper.dp2px(getContext(), 20);
        QMUIGroupListView
                .newSection(getContext())
                .setTitle("Section 1: 默认提供的样式")
                .addItemView(itemWithSwitch, onClickListener)
                .addItemView(itemWithChevron, onClickListener)
                .addTo(mGroupListView);

    }

    private void showSimpleBottomSheetList() {
        new QMUIBottomSheet.BottomListSheetBuilder(context)
                .addItem("10s")
                .addItem("15s")
                .addItem("20s")
                .addItem("25s")
                .addItem("30s")
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();
                        FLOG("time:"+cityCode.get(position));
                        FitzApplication.getInstance().setRefreshTime(cityCode.get(position));
                        itemWithChevron.setDetailText(REFRESH_TIME_MAP.get(FitzApplication.getInstance().getRefreshTime()));
                    }
                })
                .build()
                .show();
    }

    /**
     * 获取子类 context
     */
    @Override
    protected Context getContext() {
        return context;
    }

    /**
     * 获取子类view资源
     */
    @Override
    protected int getContentViewResId() {
        return R.layout.settings_activity_layout;
    }

    /**
     * 主界面不应显示
     */
    @Override
    protected int isBackVisible() {
        return View.VISIBLE;
    }

    /**
     * 应始终显示
     */
    @Override
    protected int isCityVisible() {
        return View.VISIBLE;
    }

    /**
     * 应始终显示
     */
    @Override
    protected int isOptionVisible() {
        return View.GONE;
    }

    /**
     * 只在主界面可点击
     */
    @Override
    protected boolean isCitySelectable() {
        return false;
    }

    /**
     * 获取当前activity包含的 actionbar res
     */
    @Override
    protected int getContentActionBarResId() {
        return R.id.settings_fitzactionbar;
    }

    @Override
    protected int isLocationImageVisible() {
        return View.GONE;
    }
}
