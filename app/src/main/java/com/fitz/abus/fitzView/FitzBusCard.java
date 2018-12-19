package com.fitz.abus.fitzView;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.fitz.abus.R;

/**
 * @ProjectName: ABus
 * @Package: com.fitz.abus.fitzView
 * @ClassName: FitzBusCard
 * @Author: Fitz
 * @CreateDate: 2018/12/16 20:10
 */
public class FitzBusCard extends LinearLayout {

    private boolean isDebug = true;
    private String TAG = "FitzBusCard";
    private Context mContext;
    private View contentView;

    public FitzBusCard(Context context) {
        super(context);
        FLOG("FitzBusCard");
        mContext = context;
        initBusCard();
    }

    private void initBusCard() {
        FLOG("initBusCard");
        contentView = inflate(mContext,R.layout.bus_info_card,this);

    }

    public void FLOG(String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
        }
    }

}
