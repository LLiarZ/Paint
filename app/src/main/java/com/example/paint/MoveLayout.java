package com.example.paint;

import android.content.Context;
import androidx.annotation.Nullable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

/**
 * Created by Robert on 2017/6/20.
 */

public class MoveLayout extends RelativeLayout{

    private int dragDirection = 0;
    private static final int TOP = 0x15;
    private static final int LEFT = 0x16;
    private static final int BOTTOM = 0x17;
    private static final int RIGHT = 0x18;
    private static final int LEFT_TOP = 0x11;
    private static final int RIGHT_TOP = 0x12;
    private static final int LEFT_BOTTOM = 0x13;
    private static final int RIGHT_BOTTOM = 0x14;
    private static final int CENTER = 0x19;


    private int lastX;
    private int lastY;

    private int screenWidth;
    private int screenHeight;

    private int oriLeft;
    private int oriRight;
    private int oriTop;
    private int oriBottom;

    //初始旋转角度
    private float oriRotation = 0;
    protected Paint paint = new Paint();




    /**
     * 标示此类的每个实例的id
     */
    private int identity = 0;

    /**
     *触控区域设定
     */
    private int touchAreaLength = 60;

    private int minHeight = 120;
    private int minWidth= 120;
    private static final String TAG = "MoveLinearLayout";


    public MoveLayout(Context context) {
        super(context);
        init();
    }

    public MoveLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MoveLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private void init() {
        screenHeight = getResources().getDisplayMetrics().heightPixels - 40;
        screenWidth =  getResources().getDisplayMetrics().widthPixels;
    }

    public void setViewWidthHeight(int width , int height) {
        screenWidth = width;
        screenHeight = height;
    }

    public void setMinHeight(int height) {
        minHeight = height;
        if(minHeight < touchAreaLength*2) {
            minHeight = touchAreaLength*2;
        }
    }

    public void setMinWidth(int width) {
        minWidth = width;
        if (minWidth < touchAreaLength*3) {
            minWidth = touchAreaLength*3;
        }
    }

    private boolean mFixedSize = false;

    public void setFixedSize(boolean b) {
        mFixedSize = b;
    }

    private int mDeleteHeight = 0;
    private int mDeleteWidth = 0;
    private boolean isInDeleteArea = false;
    public void setDeleteWidthHeight(int width, int height) {
        mDeleteWidth = screenWidth - width;
        mDeleteHeight = height;
    }


   @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction()&MotionEvent.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "onTouchEvent: down height="+ getHeight());
                oriLeft = getLeft();
                oriRight = getRight();
                oriTop = getTop();
                oriBottom = getBottom();

                lastY = (int) event.getRawY();
                lastX = (int) event.getRawX();

                oriRotation = getRotation();
                Log.d(TAG,"ACTION_DOWN:"+oriRotation);
                dragDirection = getDirection((int) event.getX(), (int) event.getY());
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "onTouchEvent: up");
                spotL = false;
                spotT = false;
                spotR = true;
                spotB = true;
                requestLayout();
                mDeleteView.setVisibility(View.INVISIBLE);
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, "onTouchEvent: cancel");
                spotL = false;
                spotT = false;
                spotR = true;
                spotB = true;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "onTouchEvent: move");
                int dx = (int) event.getRawX() - lastX;
                int dy = (int) event.getRawY() - lastY;
           //   lastX = tempRawX;
           //   lastY = tempRawY;


           //   lastX = (int) event.getRawX();
           //   lastY = (int) event.getRawY();
                // int dx =(int)event.getRawX() - lastX;
                // int dy =(int)event.getRawY() - lastY;
                switch (dragDirection) {
                   // case LEFT:
                    //  left( dx);
                    //  lastX = (int) event.getRawX();
                    //  lastY = (int) event.getRawY();
                    //  break;
                    case RIGHT:
                        right( dx);
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;
                    case BOTTOM:
                        bottom(dy);
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;
                 //   case TOP:
                 //       top( dy);
                 //       lastX = (int) event.getRawX();
                 //       lastY = (int) event.getRawY();
                 //       break;
                    case CENTER:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        center( dx, dy);
                        break;
                //    case LEFT_BOTTOM:
                //        left( dx);
                //        bottom( dy);
                //        lastX = (int) event.getRawX();
                //        lastY = (int) event.getRawY();
                //        break;
                //    case LEFT_TOP:
                //        left( dx);
                //        top(dy);
                //        lastX = (int) event.getRawX();
                //        lastY = (int) event.getRawY();
                //        break;
                    case RIGHT_BOTTOM:
                        right( dx);
                        bottom( dy);
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;
                    case RIGHT_TOP:
                        Point center = new Point(oriLeft+(oriRight-oriLeft)/2,oriTop+(oriBottom-oriTop)/2);
                        Point first = new Point(lastX,lastY);
                        Point second = new Point((int) event.getRawX(),(int) event.getRawY());
                        oriRotation += angle(center,first,second);
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        setRotation(oriRotation);
                        break;
                }

                //new pos l t r b is set into oriLeft, oriTop, oriRight, oriBottom
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(oriRight - oriLeft, oriBottom - oriTop);
                lp.setMargins(oriLeft,oriTop,0,0);
                setLayoutParams(lp);
                Log.d(TAG, "onTouchEvent: set layout width="+(oriRight - oriLeft)+" height="+(oriBottom - oriTop));
                Log.d(TAG, "onTouchEvent: marginLeft="+oriLeft+"  marginTop"+oriTop);
                break;
        }
        return super.onTouchEvent(event);
    }

    public float angle(Point cen , Point first, Point second)
    {
        float dx1, dx2, dy1, dy2;

        dx1 = first.x - cen.x;
        dy1 = -first.y + cen.y;
        dx2 = second.x - cen.x;
        dy2 = second.y - cen.y;

        //计算三边的平方
        float ab2 = (second.x - first.x) * (second.x - first.x)+(second.y - first.y) * (second.y - first.y);
        float oa2 = dx1*dx1 + dy1*dy1;
        float ob2 = dx2 * dx2 + dy2 * dy2;

        //根据两向量的叉乘来判断顺逆时针
        boolean isClockwise = ((first.x - cen.x) * (second.y - cen.y) - (first.y - cen.y) * (second.x - cen.x)) > 0;

        // 根据余弦定理计算旋转角的余弦值
        double cosDegree = (oa2 + ob2 - ab2) / (2 * Math.sqrt(oa2) * Math.sqrt(ob2));

        // 异常处理，因为算出来会有误差绝对值可能会超过一，所以需要处理一下
        if (cosDegree > 1){
            cosDegree = 1;
        }else if (cosDegree < -1){
            cosDegree = -1;
        }

        //计算弧度
        double radian = Math.acos(cosDegree);

        //计算旋转过的角度，顺时针为正，逆时针为负
        return (float)(isClockwise ? Math.toDegrees(radian) : -Math.toDegrees(radian));
    }




    /**
     * 触摸点为中心->>移动
     */
    private void center(int dx, int dy) {
        int left = getLeft() + dx;
        int top = getTop() + dy;
        int right = getRight() + dx;
        int bottom = getBottom() + dy;

        if (left < 0) {
            left = 0;
            right = left + getWidth();
        }
        if (right > screenWidth ) {
            right = screenWidth ;
            left = right - getWidth();
        }
        if (top < 0) {
            top = 0;
            bottom = top + getHeight();
        }
        if (bottom > screenHeight ) {
            bottom = screenHeight ;
            top = bottom - getHeight();
        }

        oriLeft = left;
        oriTop = top;
        oriRight = right;
        oriBottom = bottom;

        //todo: show delete icon
        mDeleteView.setVisibility(View.VISIBLE);
        //do delete
        if(isInDeleteArea == false && oriRight > mDeleteWidth && oriTop < mDeleteHeight) {//delete
            Log.e(TAG, "center: oriRight"+oriRight+  "  mDeleteWidth"+mDeleteWidth  +"  oriTop"+ oriTop+ "  mDeleteHeightv"+ mDeleteHeight);
            if(mListener != null) {
                mListener.onDeleteMoveLayout(identity);
                mDeleteView.setVisibility(View.INVISIBLE);
                isInDeleteArea = true;//this object is deleted ,only one-time-using
            }
        }

    }



    /**
     * 触摸点为上边缘
     */
    private void top(int dy) {
        oriTop += dy;
        if (oriTop < 0) {
            oriTop = 0;
        }
        if (oriBottom - oriTop  < minHeight) {
            oriTop = oriBottom  - minHeight;
        }
    }

    /**
     * 触摸点为下边缘
     */
    private void bottom(int dy) {

        oriBottom += dy;
        if (oriBottom > screenHeight ) {
            oriBottom = screenHeight ;
        }
        if (oriBottom - oriTop  < minHeight) {
            oriBottom = minHeight + oriTop;
        }
    }

    /**
     * 触摸点为右边缘
     */
    private void right(int dx) {
        oriRight += dx;
        if (oriRight > screenWidth ) {
            oriRight = screenWidth ;
        }
        if (oriRight - oriLeft  < minWidth) {
            oriRight = oriLeft  + minWidth;
        }
    }

    /**
     * 触摸点为左边缘
     */
    private void left(int dx) {
        oriLeft += dx;
        if (oriLeft < 0) {
            oriLeft = 0;
        }
        if (oriRight - oriLeft  < minWidth) {
            oriLeft = oriRight - minWidth;
        }
    }

    private int getDirection( int x, int y) {
        int left = getLeft();
        int right = getRight();
        int bottom = getBottom();
        int top = getTop();
        if (x < touchAreaLength && y < touchAreaLength) {
            return LEFT_TOP;
        }

        if (y < touchAreaLength && right - left - x < touchAreaLength) {
            return RIGHT_TOP;
        }

        if (x < touchAreaLength && bottom - top - y < touchAreaLength) {
            return LEFT_BOTTOM;
        }

        if (right - left - x < touchAreaLength && bottom - top - y < touchAreaLength) {
            return RIGHT_BOTTOM;
        }
        if (x < touchAreaLength) {
            spotL = true;
            requestLayout();
            return LEFT;
        }
        if (y < touchAreaLength) {
            spotT = true;
            requestLayout();
            return TOP;
        }
        if (right - left - x < touchAreaLength) {
            spotR = true;
            requestLayout();
            return RIGHT;
        }
        if (bottom - top - y < touchAreaLength) {
            spotB = true;
            requestLayout();
            return BOTTOM;
        }


        if (mFixedSize == true) {
            return CENTER;
        }


        return CENTER;
    }

    private boolean spotL = false;
    private boolean spotT = false;
    private boolean spotR = true;
    private boolean spotB = true;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);


        RelativeLayout rlt = (RelativeLayout) getChildAt(0);
        int count = rlt.getChildCount();

        for (int a = 0; a < count; a ++) {
            if(a == 1) {        //l
                if(spotL)
                    rlt.getChildAt(a).setVisibility(View.VISIBLE);
                else
                    rlt.getChildAt(a).setVisibility(View.INVISIBLE);
            } else if(a == 2) { //t
                if(spotT)
                    rlt.getChildAt(a).setVisibility(View.VISIBLE);
                else
                    rlt.getChildAt(a).setVisibility(View.INVISIBLE);
            } else if(a == 3) { //r
                if(spotR)
                    rlt.getChildAt(a).setVisibility(View.VISIBLE);
                else
                    rlt.getChildAt(a).setVisibility(View.INVISIBLE);
            } else if(a == 4) { //b
                if(spotB)
                    rlt.getChildAt(a).setVisibility(View.VISIBLE);
                else
                    rlt.getChildAt(a).setVisibility(View.INVISIBLE);
            }
            Log.d(TAG, "onLayout: "+rlt.getChildAt(a).getClass().toString());
        }

    }



    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    //set the main delete area object (to change visibility)
    private View mDeleteView = null;
    public void setDeleteView(View v) {
        mDeleteView = v;
    }

    //delete listener
    private DeleteMoveLayout mListener = null;
    public interface DeleteMoveLayout {
        void onDeleteMoveLayout(int identity);
    }
    public void setOnDeleteMoveLayout(DeleteMoveLayout l) {
        mListener = l;
    }



    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        paint.setAntiAlias(false);
        paint.setColor(Color.RED);
        canvas.drawColor(Color.TRANSPARENT);
        paint.setStrokeWidth((float)3.0);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawCircle(50,50,50,paint);
    }


}

