package com.fitz.abus.fitzView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import com.fitz.abus.R;

public class fitzTextView extends android.support.v7.widget.AppCompatTextView {
    private final String ATTR_ICON_SRC = "iconSrc";
    private final String NAMESPACE = "http://fitz.com/view";
    private       Bitmap bitMap;
    private Paint mPaint  = new Paint(Paint.ANTI_ALIAS_FLAG);
    private String mText = getText().toString();
    private Rect src = new Rect();      //源图像资源
    private Rect target = new Rect();   //缩放后图像资源

    public fitzTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        int resouceId = attrs.getAttributeResourceValue(NAMESPACE, ATTR_ICON_SRC, R.drawable.location);
        bitMap = BitmapFactory.decodeResource(getResources(), resouceId);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //获取宽高的模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec) ;
        int heightMode = MeasureSpec.getMode(heightMeasureSpec) ;

        if(bitMap != null){
            //源 将图像截取部分内容，这里为整张图像
            src.top = 0;
            src.left = 0;
            src.right = bitMap.getWidth();
            src.bottom = bitMap.getHeight();

            int textHeight =  (int) getTextSize();

            target.left = 0;
            //  计算图像复制到目录区域的纵坐标。由于TextView中文本内容并不是从最顶端开始绘制的，因此，需要重新计算绘制图像的纵坐标
            target.top = (int) ((getMeasuredHeight() - getTextSize()) / 2) + 1;
            target.bottom = target.top + textHeight;
            //  为了保证图像不变形，需要根据图像高度重新计算图像的宽度
            target.right = (int) (textHeight * (bitMap.getWidth() / (float) bitMap
                    .getHeight()));
        }


        //如果在布局中你设置文字的宽高是wrap_content[对应MeasureSpec.AT_MOST] , 则需要使用模式来计算
        if (widthMode != MeasureSpec.AT_MOST){
            //设置文字控件的宽和高
            setMeasuredDimension(widthMeasureSpec + target.right , heightMeasureSpec);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(bitMap != null){

            canvas.drawBitmap(bitMap, src, target, getPaint());
            canvas.translate(target.right + 2, 0);
        }
        super.onDraw(canvas);
    }


}
