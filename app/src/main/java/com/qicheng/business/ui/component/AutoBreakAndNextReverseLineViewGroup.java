package com.qicheng.business.ui.component;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by NO1 on 2015/2/7.
 */
public class AutoBreakAndNextReverseLineViewGroup extends LinearLayout {

    private final static String TAG = "ABANRLViewGroup";

    public final static int VIEW_MARGIN = 100;

    private final static int SIDE_MARGIN=80;

    private int hStartPoint;

    private int vStartPoint;

    private ArrayList<View> rightTurnedNodes = new ArrayList<View>();

    private ArrayList<View> leftTurnedNodes = new ArrayList<View>();

    public AutoBreakAndNextReverseLineViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoBreakAndNextReverseLineViewGroup(Context context) {
        super(context);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "widthMeasureSpec = " + widthMeasureSpec + " heightMeasureSpec" + heightMeasureSpec);

        for (int index = 0; index < getChildCount(); index++) {
            final View child = getChildAt(index);
            // measure
            child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        }
        // DisplayMetrics dm = new DisplayMetrics();
        int childCount = this.getChildCount();
        int height = 0 ;
        if(this.getChildCount()>=1){
            View endView  = this.getChildAt(childCount-1);
            View beginView = this.getChildAt(0);
            float e = endView.getY();
            float b = beginView.getY();
            String length  =(e-b+endView.getHeight()+VIEW_MARGIN)+"";
            height = Integer.parseInt(length.substring(0,length.length()-2));
        }

        setMeasuredDimension(widthMeasureSpec, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG, "changed = " + changed + " left = " + l + " top = " + t + " right = " + r + " botom = " + b);
        final int count = getChildCount();
        int row = 0;// which row lay you view relative to parent
        /**
         * ViewGroup起始点
         */
        this.hStartPoint = l;
        this.vStartPoint = t;
        /**
         * 子View排布计算所用参数
         */
        int lengthX = l;    // right position of child relative to parent
        int lengthY = t;    // bottom position of child relative to parent
        /**
         * 子View排列方向标识
         */
        boolean leftToRight = true;
        for (int i = 0; i < count; i++) {
            final View child = this.getChildAt(i);
            if (leftToRight){
                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();
                /**
                 * 子View的l,r,t,b位置参数
                 */
                int cl = 0;
                if(i==0){
                    cl = l + SIDE_MARGIN;
                }else {
                    cl = lengthX+VIEW_MARGIN;
                }
                int cr = cl+width;
                /**
                 * 被用掉的X,Y区域
                 */
                lengthX =cr;
                lengthY = row * (height + VIEW_MARGIN) + VIEW_MARGIN + height ;
                int ct = lengthY-height;
                int cb = lengthY;

                //if it can't drawing on a same line , skip to next line
                if (cr > r) {
                    //上一节点为右转节点
                    rightTurnedNodes.add(this.getChildAt(i-1));
                    cl = r-SIDE_MARGIN-width;
                    cr = cl+width;
                    lengthX = cl;
                    row++;
                    lengthY = row * (height + VIEW_MARGIN) + VIEW_MARGIN + height ;
                    ct = lengthY-height;
                    cb = lengthY;
                    /**
                     * 换行，换方向
                     */
                    leftToRight=false;
                }
                child.layout(cl,ct,cr,cb);
            }else {

                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();


                /**
                 * 被用掉的X,Y区域
                 */
                lengthX =lengthX- width - VIEW_MARGIN;
                lengthY = row * (height + VIEW_MARGIN) + VIEW_MARGIN + height ;
                int cl = lengthX;
                int cr = lengthX+width;
                int ct = lengthY-height;
                int cb = lengthY;
                //if it can't drawing on a same line , skip to next line
                if (cl < l) {
                    //上一节点为左转节点
                    leftTurnedNodes.add(this.getChildAt(i-1));
                    row++;
                    lengthY = row * (height + VIEW_MARGIN) + VIEW_MARGIN + height;
                    cl=l+SIDE_MARGIN;
                    cr=cl+width;
                    ct = lengthY-height;
                    cb = lengthY;
                    lengthX = cr;
                    /**
                     * 换行，换方向
                     */
                    leftToRight = true;
                }
                child.layout(cl, ct, cr, cb);
            }


        }

    }

    public ArrayList<View> getRightTurnedNodes() {
        return rightTurnedNodes;
    }


    public ArrayList<View> getLeftTurnedNodes() {
        return leftTurnedNodes;
    }

}
