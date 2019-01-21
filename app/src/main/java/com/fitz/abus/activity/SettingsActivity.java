package com.fitz.abus.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.fitz.abus.R;
import com.fitz.abus.base.BaseActivity;
import com.fitz.abus.fitzview.FitzActionBar;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUILoadingView;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initGroupListView();
    }


    private void initGroupListView() {
        QMUICommonListItemView normalItem = mGroupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.mipmap.ic_launcher),
                "Item 1",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE);
        normalItem.setOrientation(QMUICommonListItemView.VERTICAL);

        QMUICommonListItemView itemWithDetail = mGroupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.mipmap.ic_launcher),
                "Item 2",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE);
        itemWithDetail.setDetailText("在右方的详细信息");

        QMUICommonListItemView itemWithDetailBelow = mGroupListView.createItemView("Item 3");
        itemWithDetailBelow.setOrientation(QMUICommonListItemView.VERTICAL);
        itemWithDetailBelow.setDetailText("在标题下方的详细信息");

        QMUICommonListItemView itemWithChevron = mGroupListView.createItemView("Item 4");
        itemWithChevron.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView itemWithSwitch = mGroupListView.createItemView("Item 5");
        itemWithSwitch.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_SWITCH);
        itemWithSwitch.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(context, "checked = " + isChecked, Toast.LENGTH_SHORT).show();
            }
        });

        QMUICommonListItemView itemWithCustom = mGroupListView.createItemView("Item 6");
        itemWithCustom.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        QMUILoadingView loadingView = new QMUILoadingView(context);
        itemWithCustom.addAccessoryCustomView(loadingView);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof QMUICommonListItemView) {
                    CharSequence text = ((QMUICommonListItemView) v).getText();
                    Toast.makeText(context, text + " is Clicked", Toast.LENGTH_SHORT).show();
                }
            }
        };

        int size = QMUIDisplayHelper.dp2px(getContext(), 20);
        QMUIGroupListView.newSection(getContext())
                         .setTitle("Section 1: 默认提供的样式")
                         .setDescription("Section 1 的描述")
                         .addItemView(normalItem, onClickListener)
                         .addItemView(itemWithDetail, onClickListener)
                         .addItemView(itemWithDetailBelow, onClickListener)
                         .addItemView(itemWithChevron, onClickListener)
                         .addItemView(itemWithSwitch, onClickListener)
                         .addTo(mGroupListView);

        QMUIGroupListView.newSection(getContext())
                         .setTitle("Section 2: 自定义右侧 View")
                         .addItemView(itemWithCustom, onClickListener)
                         .addTo(mGroupListView);
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
