package com.qicheng.framework.ui;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/**
 * HorizontalScrollListView.java是启程APP的水平滚动自定义View类
 *
 * @author 花树峰
 * @version 1.0 2015年2月4日
 */
public class HorizontalScrollListView extends HorizontalScrollView {

    public HorizontalScrollListView(Context context) {
        super(context);
    }

    public HorizontalScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalScrollListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 记录当前滚动的初始
     */
    private int intitPosition = 0;

    /**
     * 滚动监听间隔
     */
    private int scrollDealy = 50;

    /**
     * 检查所有子视图总宽度
     */
    private int childWidth = 0;

    /**
     * 滚动停止监听器对象
     */
    private OnScrollStopListener onScrollStopListener;

    /**
     * 滚动停止监听器接口
     */
    public interface OnScrollStopListener {

        /**
         * scroll have stoped
         */
        void onScrollStoped();

        /**
         * scroll have stoped, and is at left edge
         */
        void onScrollToLeftEdge();

        /**
         * scroll have stoped, and is at right edge
         */
        void onScrollToRightEdge();

        /**
         * scroll have stoped, and is at middle
         */
        void onScrollToMiddle();
    }

    /**
     * 滚动监听runnable
     */
    private Runnable scrollRunnable = new Runnable() {

        @Override
        public void run() {
            int newPosition = getScrollX();
            if (newPosition == intitPosition) {
                // 滚动停止，取消监听线程
                if (onScrollStopListener != null) {
                    onScrollStopListener.onScrollStoped();
                    Rect outRect = new Rect();
                    getDrawingRect(outRect);
                    if(newPosition == 0) {
                        onScrollStopListener.onScrollToLeftEdge();
                    } else if(childWidth + getPaddingLeft() + getPaddingRight() == outRect.right) {
                        onScrollStopListener.onScrollToRightEdge();
                    } else {
                        onScrollStopListener.onScrollToMiddle();
                    }
                }
                getHandler().removeCallbacks(this);
                return;
            } else {
                // 手指离开屏幕，view还在滚动的时候
                intitPosition = getScrollX();
                getHandler().postDelayed(this, scrollDealy);
            }
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                // 手指在上面移动的时候，取消滚动监听线程
                getHandler().removeCallbacks(scrollRunnable);
                break;
            case MotionEvent.ACTION_UP:
                intitPosition = getScrollX();
                checkTotalWidth();
                getHandler().postDelayed(scrollRunnable, scrollDealy);
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 设置滚动停止监听器对象
     */
    public void setOnScrollStopListener(OnScrollStopListener onScrollStopListener) {
        this.onScrollStopListener = onScrollStopListener;
    }

    /**
     * 检查所有子视图总宽度
     */
    private void checkTotalWidth() {
        childWidth = 0;
        for(int i = 0, childCount = getChildCount(); i < childCount; i++) {
            childWidth += getChildAt(i).getWidth();
        }
    }
}
