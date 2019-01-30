package com.fitz.abus.fitzview;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.fitz.abus.FitzApplication;
import com.fitz.abus.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.view.animation.Animation.REVERSE;

/**
 * @ProjectName: ABus
 * @Package: com.fitz.abus.fitzview
 * @Author: Fitz
 * @CreateDate: 2019/1/28
 */
@SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
public class GuideView extends View {
    private final int TOP = 0x01;
    private final int BOTTOM = 0x02;
    private final int LEFT = 0x03;
    private final int RIGHT = 0x04;
    public View guideParent;
    private Context context;
    private float density;
    private boolean isDebug = FitzApplication.Debug;
    private String TAG = "GuideView";
    //背景宽
    private int bgWidth;
    //背景高
    private int bgHeight;
    //需要引导的view的x坐标
    private int guideX;
    //需要引导的view的y坐标
    private int guideY;
    //需要引导的view的宽
    private int guideWidth;
    //需要引导的view的高
    private int guideHeight;
    private Paint sourcePaint;
    private Paint bgPaint;
    private Paint guidePaint;
    private Paint textPaint;
    private String guideStr;
    private float distance;
    private ValueAnimator animator;
    private boolean added = false;
    private int redundantSpace = 10;


    public GuideView(Context context) {
        super(context);
        this.context = context;
        initSourcePaint();
        initBgPaint();
        initGuidePaint();
        initTextPaint();
        //用到Xfermode时必要的设置
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    private void initSize() {
        if (context instanceof Activity) {
            DisplayMetrics dm = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
            /*bgWidth = dm.widthPixels;//设置背景宽为屏幕宽
            bgHeight = dm.heightPixels;//设置背景高为屏幕高*/
            Point outSize = new Point();
            ((Activity) context).getWindowManager().getDefaultDisplay().getRealSize(outSize);
            bgWidth = outSize.x;
            bgHeight = outSize.y;
            int[] location = new int[2];
            guideParent.getLocationOnScreen(location);//得到需要引导的view在整个屏幕中的坐标
            guideX = location[0];
            guideY = location[1];
            guideWidth = guideParent.getWidth();
            guideHeight = guideParent.getHeight();
            density = dm.density;//获取像素密度 2.0，2.5，3.0
            distance = 40 * density;//箭头与需要引导的view之间的距离
            FLOG("width:" + bgWidth + " heigth:" + bgHeight + " density" + density);
        }
    }

    public GuideView setGuideParent(View guideParent) {
        this.guideParent = guideParent;
        FLOG("guideParent:" + guideParent.toString());
        initSize();
        invalidate();
        return this;
    }

    public GuideView setGuideStr(String guideStr) {
        this.guideStr = guideStr;
        invalidate();
        return this;
    }

    /**
     * 绘制需要引导的view的区域的画笔
     */
    private void initSourcePaint() {
        sourcePaint = new Paint();
        sourcePaint.setColor(Color.BLUE);
        sourcePaint.setStyle(Paint.Style.FILL);
        sourcePaint.setAntiAlias(true);
        sourcePaint.setAlpha(255);
    }

    /**
     * 半透明背景的画笔
     */
    private void initBgPaint() {
        bgPaint = new Paint();
        bgPaint.setColor(Color.BLACK);
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setAntiAlias(true);
        bgPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));//遮盖效果详细信息可以百度搜索PorterDuffXfermode
        bgPaint.setAlpha(225);
    }

    /**
     * 引导箭头的画笔
     */
    private void initGuidePaint() {
        guidePaint = new Paint();
        guidePaint.setColor(Color.WHITE);
        guidePaint.setStyle(Paint.Style.FILL);
        guidePaint.setAntiAlias(true);
        guidePaint.setAlpha(255);
    }

    /**
     * 文字的画笔
     */
    private void initTextPaint() {
        textPaint = new Paint();
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setColor(Color.WHITE);
        textPaint.setStrokeWidth(density * 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bgPaint == null) {
            return;
        }
        if (sourcePaint == null) {
            return;
        }
        int layerID = canvas.saveLayer(0, 0, bgWidth, bgHeight, bgPaint, Canvas.ALL_SAVE_FLAG);
        canvas.drawRect(guideX - redundantSpace, guideY - redundantSpace, guideX + guideWidth + redundantSpace,
                guideY + guideHeight + redundantSpace, sourcePaint);
        canvas.drawRect(0, 0, bgWidth, bgHeight, bgPaint);
        int direction;
        if (guideY < bgHeight / 4) {
            direction = TOP;
            drawArrow(direction, canvas, guideX + guideWidth / 2, guideY + guideHeight + distance + redundantSpace);
        } else if (guideY > bgHeight / 4 * 3) {
            direction = BOTTOM;
            drawArrow(direction, canvas, guideX + guideWidth / 2, guideY - distance - redundantSpace);
        } else if (guideX < bgWidth / 4) {
            direction = LEFT;
            drawArrow(direction, canvas, guideX + guideWidth + distance, guideY + guideHeight / 2);
        } else if (guideX > bgWidth / 4 * 3) {
            direction = RIGHT;
            drawArrow(direction, canvas, guideX - distance, guideY + guideHeight / 2);
        }
        drawGuideText(canvas);
        canvas.restoreToCount(layerID);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    private void drawGuideText(Canvas canvas) {
        if (!TextUtils.isEmpty(guideStr)) {
            Rect bounds = new Rect();
            float textSize = density * 20.0f;
            textPaint.setTextSize(textSize);
            textPaint.getTextBounds(guideStr, 0, guideStr.length(), bounds);
            canvas.drawText(guideStr, bgWidth / 2 - bounds.width() / 2, bgHeight / 2 + bounds.height() / 2, textPaint);
        }
    }

    /**
     * 画箭头
     *
     * @param direction 上或者下方向的箭头
     * @param canvas    画布
     * @param x         箭头顶点x坐标
     * @param y         箭头顶点y坐标
     */
    private void drawArrow(int direction, Canvas canvas, float x, float y) {
        final float triangleWidth = 20 * density;
        final float triangleHeight = 20 * density;
        final float rectWidth = 10 * density;
        final float rectHeight = 35 * density;
        float vertexOneX = x;
        float vertexOneY = y;
        float vertexTwoX = x;
        float vertexTwoY = y;
        float rectOneX = x;
        float rectOneY = y;
        float rectTwoX = x;
        float rectTwoY = y;
        float rectThreeX = x;
        float rectThreeY = y;
        float rectFourX = x;
        float rectFourY = y;
        switch (direction) {
            case TOP:
                vertexOneX = x - triangleWidth / 2;
                vertexOneY = y + triangleHeight;
                vertexTwoX = x + triangleWidth / 2;
                vertexTwoY = y + triangleHeight;
                rectOneX = x - rectWidth / 2;
                rectOneY = y + triangleHeight;
                rectTwoX = x + rectWidth / 2;
                rectTwoY = y + triangleHeight;
                rectThreeX = x + rectWidth / 2;
                rectThreeY = y + triangleHeight + rectHeight;
                rectFourX = x - rectWidth / 2;
                rectFourY = y + triangleHeight + rectHeight;
                break;
            case BOTTOM:
                vertexOneX = x + triangleWidth / 2;
                vertexOneY = y - triangleHeight;
                vertexTwoX = x - triangleWidth / 2;
                vertexTwoY = y - triangleHeight;
                rectOneX = x + rectWidth / 2;
                rectOneY = y - triangleHeight;
                rectTwoX = x - rectWidth / 2;
                rectTwoY = y - triangleHeight;
                rectThreeX = x - rectWidth / 2;
                rectThreeY = y - triangleHeight - rectHeight;
                rectFourX = x + rectWidth / 2;
                rectFourY = y - triangleHeight - rectHeight;
                break;
            case LEFT:
                vertexOneX = x + triangleHeight;
                vertexOneY = y + triangleWidth / 2;
                vertexTwoX = x + triangleHeight;
                vertexTwoY = y - triangleWidth / 2;
                rectOneX = x + triangleHeight;
                rectOneY = y + rectWidth / 2;
                rectTwoX = x + triangleHeight;
                rectTwoY = y - rectWidth / 2;
                rectThreeX = x + triangleHeight + rectHeight;
                rectThreeY = y - rectWidth / 2;
                rectFourX = x + triangleHeight + rectHeight;
                rectFourY = y + rectWidth / 2;
                break;
            case RIGHT:
                vertexOneX = x - triangleHeight;
                vertexOneY = y - triangleWidth / 2;
                vertexTwoX = x - triangleHeight;
                vertexTwoY = y + triangleWidth / 2;
                rectOneX = x - triangleHeight;
                rectOneY = y - rectWidth / 2;
                rectTwoX = x - triangleHeight;
                rectTwoY = y + rectWidth / 2;
                rectThreeX = x - triangleHeight - rectHeight;
                rectThreeY = y + rectWidth / 2;
                rectFourX = x - triangleHeight - rectHeight;
                rectFourY = y - rectWidth / 2;
                break;
        }
        Path trianglePath = new Path();
        trianglePath.moveTo(x, y);
        trianglePath.lineTo(vertexOneX, vertexOneY);
        trianglePath.lineTo(vertexTwoX, vertexTwoY);
        trianglePath.close();
        canvas.drawPath(trianglePath, guidePaint);
        Path rectPath = new Path();
        rectPath.moveTo(rectOneX, rectOneY);
        rectPath.lineTo(rectTwoX, rectTwoY);
        rectPath.lineTo(rectThreeX, rectThreeY);
        rectPath.lineTo(rectFourX, rectFourY);
        rectPath.close();
        canvas.drawPath(rectPath, guidePaint);
    }

    @Override
    public String toString() {
        return "guideParent:" + (guideParent == null) + " guideStr:" + guideStr;
    }

    /**
     * 重写onTouchEvent，实现触摸之后就销毁GuideView的功能
     * 因为重写了onTouchEvent 所有View 的OnClickListener等将不能正常使用
     *
     * @param event 触摸
     * @return boolean true-触摸被自己消耗掉，false-触摸传到下一层
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (added) {
            ((ViewGroup) ((Activity) context).getWindow().getDecorView()).removeView(this);
            added = false;
        }
        if (animator != null) {
            if (animator.isRunning()) {
                animator.end();
            }
        }
        return true;
    }

    private void initAnim() {
        animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cValue = (float) animation.getAnimatedValue();
                distance = density * 10 * cValue;
                invalidate();
            }
        });
        animator.setDuration(350);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setRepeatMode(REVERSE);
        animator.setRepeatCount(-1);
    }

    public void startAnim() {
        if (animator == null) {
            initAnim();
        }
        if (!added) {
            //将guideView直接添加到window中，获取DecorView并把guideView作为child添加到其中，从而实现全屏显示
            setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            ((ViewGroup) ((Activity) context).getWindow().getDecorView()).addView(this);
            added = true;
        }
        if (!animator.isRunning()) {
            animator.start();
        }
    }

    public void FLOG(String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
        }
    }

    public static class GuideViewParams {
        public View targetView;
        public String targetIntroduction;

        public GuideViewParams() {}
    }

    public static class Builder {
        private List<GuideView> guideViewList = new ArrayList<>();
        private Context context;

        public Builder(Context context) {
            this.context = context;
        }

        public GuideView.Builder addGuideView(GuideView guideView) {
            if (guideView == null) {
                return this;
            } else {
                this.guideViewList.add(guideView);
                return this;
            }
        }

        public void show() {
            Iterator<GuideView> iterator = guideViewList.iterator();
            show(iterator);
        }

        public void show(Iterator<GuideView> iterator) {
            if (iterator.hasNext()) {
                GuideView guideView = iterator.next();
                guideView.startAnim();
                guideView.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (guideView.added) {
                            ((ViewGroup) ((Activity) context).getWindow().getDecorView()).removeView(guideView);
                            guideView.added = false;
                        }
                        if (guideView.animator != null) {
                            if (guideView.animator.isRunning()) {
                                guideView.animator.end();
                            }
                        }
                        show(iterator);
                        return true;
                    }
                });
            }
        }


    }
}