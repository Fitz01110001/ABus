package com.fitz.abus.fitzview;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @ProjectName: ABus
 * @Package: com.fitz.abus.fitzview
 * @Author: Fitz
 * @CreateDate: 2019/1/21
 */
public class FitzAlwaysMarqueeTextView extends android.support.v7.widget.AppCompatTextView {
    public FitzAlwaysMarqueeTextView(Context context) {
        super(context);
    }

    public FitzAlwaysMarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FitzAlwaysMarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
