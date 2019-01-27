package com.fitz.abus.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.fitz.abus.FitzApplication;
import com.fitz.abus.R;
import com.fitz.abus.base.BaseActivity;
import com.fitz.abus.fitzview.FitzActionBar;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;

import java.util.ArrayList;
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
    private QMUICommonListItemView itemSetTimeAutoRefresh;
    private QMUICommonListItemView itemSwitchAutoRefresh;
    private QMUICommonListItemView itemCheckUpdates;
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
        Beta.checkUpgrade();
    }


    private void initGroupListView() {
        itemSwitchAutoRefresh = mGroupListView.createItemView(getString(R.string.auto_refresh));
        itemSwitchAutoRefresh.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_SWITCH);
        itemSwitchAutoRefresh.getSwitch().setChecked(FitzApplication.getInstance().isAutoRefresh());
        itemSwitchAutoRefresh.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                FitzApplication.getInstance().setAutoRefresh(isChecked);
            }
        });

        itemSetTimeAutoRefresh = mGroupListView.createItemView(getString(R.string.auto_refresh_time));
        itemSetTimeAutoRefresh.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        itemSetTimeAutoRefresh.setDetailText(REFRESH_TIME_MAP.get(FitzApplication.getInstance().getRefreshTime()));
        itemSetTimeAutoRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSimpleBottomSheetList();
            }
        });

        itemCheckUpdates = mGroupListView.createItemView(getResources().getString(R.string.check_for_updates));
        itemCheckUpdates.setRedDotPosition(QMUICommonListItemView.REDDOT_POSITION_RIGHT);
        itemCheckUpdates.setDetailText(getResources().getString(R.string.click_check_updates));
        itemCheckUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Beta.checkUpgrade();
                loadUpgradeInfo();
            }
        });


        QMUIGroupListView
                .newSection(getContext())
                .setTitle(getResources().getString(R.string.auto_refresh_settings))
                .addItemView(itemSwitchAutoRefresh, null)
                .addItemView(itemSetTimeAutoRefresh, null)
                .addItemView(itemCheckUpdates, null)
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
                        FLOG("time:" + cityCode.get(position));
                        FitzApplication.getInstance().setRefreshTime(cityCode.get(position));
                        itemSetTimeAutoRefresh.setDetailText(REFRESH_TIME_MAP.get(FitzApplication.getInstance().getRefreshTime()));
                    }
                })
                .build()
                .show();
    }

    private void loadUpgradeInfo() {

        /***** 获取升级信息 *****/
        UpgradeInfo upgradeInfo = Beta.getUpgradeInfo();

        if (upgradeInfo == null) {
            itemCheckUpdates.setDetailText(getResources().getString(R.string.already_latest));
            return;
        }
        itemCheckUpdates.showRedDot(true);
        StringBuilder info = new StringBuilder();
        info.append("id: ").append(upgradeInfo.id).append("\n");
        info.append("标题: ").append(upgradeInfo.title).append("\n");
        info.append("升级说明: ").append(upgradeInfo.newFeature).append("\n");
        info.append("versionCode: ").append(upgradeInfo.versionCode).append("\n");
        info.append("versionName: ").append(upgradeInfo.versionName).append("\n");
        info.append("发布时间: ").append(upgradeInfo.publishTime).append("\n");
        info.append("安装包Md5: ").append(upgradeInfo.apkMd5).append("\n");
        info.append("安装包下载地址: ").append(upgradeInfo.apkUrl).append("\n");
        info.append("安装包大小: ").append(upgradeInfo.fileSize).append("\n");
        info.append("弹窗间隔（ms）: ").append(upgradeInfo.popInterval).append("\n");
        info.append("弹窗次数: ").append(upgradeInfo.popTimes).append("\n");
        info.append("发布类型（0:测试 1:正式）: ").append(upgradeInfo.publishType).append("\n");
        info.append("弹窗类型（1:建议 2:强制 3:手工）: ").append(upgradeInfo.upgradeType);

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
