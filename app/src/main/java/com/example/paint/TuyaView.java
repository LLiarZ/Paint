package com.example.paint;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class TuyaView extends View {

//////////////////////////////////////////////////////////////////////////////////////////////
    //控制类
    private int preX = 0;
    private int preY = 0;
//////////////////////////////////////////////////////////////////////////////////////////////


//////////////////////////////////////////////////////////////////////////////////////////
    //画图类
    private Context context;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;// 画布的画笔
    private Paint mPaint;// 真实的画笔
    private float mX, mY;// 临时点坐标
    private static final float TOUCH_TOLERANCE = 4;
    // 保存Path路径的集合
    private static List<DrawPath> savePath;
    // 保存已删除Path路径的集合
    private static List<DrawPath> deletePath;
    // 记录Path路径的对象
    private DrawPath dp;
    private int screenWidth, screenHeight;
    private int currentColor = Color.RED;
    private int currentSize = 5;
    private int currentStyle = 1;
    private int[] paintColor;//颜色集合

    //设置画图样式
    private static final int DRAW_PATH = 01;

    private int[] graphics = new int[]{DRAW_PATH};
    private int currentDrawGraphics = graphics[0];//默认画线
//////////////////////////////////////////////////////////////////////////////////////


//////////////////////////////////////////////////////////////////////////////////////////////////
    private Bitmap mBitmap2;
    private Canvas mCanvas2;
    private Paint mBitmapPaint2;// 画布的画笔
    private Paint mPaint2;// 真实的画笔

///////////////////////////////////////////////////////////////////////////////////////////////


    private class DrawPath {
        public Path path;// 路径
        public Paint paint;// 画笔
    }

    public TuyaView(Context context, int w, int h) {
        super(context);
        this.context = context;
        screenWidth = w;
        screenHeight = h;
        paintColor = new int[]{
                Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.BLACK, Color.GRAY, Color.CYAN
        };
        setLayerType(LAYER_TYPE_SOFTWARE, null);//设置默认样式，去除dis-in的黑色方框以及clear模式的黑线效果
        initCanvas();

        initCanvas2();
        savePath = new ArrayList<DrawPath>();
        deletePath = new ArrayList<DrawPath>();


    }
//////////////////////////////////////////////////////////////////////////////////////////
    //绘制图形画布操作
    public void  initCanvas2(){
        mPaint2 = new Paint();
        mBitmapPaint2 = new Paint(Paint.DITHER_FLAG);
        mPaint2.setColor(Color.RED);
        mPaint2.setAntiAlias(true);
        mPaint2.setStrokeWidth(10);
        mPaint2.setStyle(Paint.Style.STROKE);
        mBitmap2 = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
        mCanvas2 = new Canvas(mBitmap2);
        mCanvas2.drawColor(Color.TRANSPARENT);

    }
/////////////////////////////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////////////////////////
    //基础画板画布操作
    @SuppressLint("WrongThread")
    public void initCanvas() {
        setPaintStyle();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        //画布大小
        mBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
        mBitmap.eraseColor(Color.argb(0, 0, 0, 0));
        mCanvas = new Canvas(mBitmap);  //所有mCanvas画的东西都被保存在了mBitmap中
        mCanvas.drawColor(Color.TRANSPARENT);
    }
///////////////////////////////////////////////////////////////////////////////////////////




    public TuyaView(Context context, AttributeSet attrs){
        super(context,attrs);
    }
    public TuyaView(Context context,AttributeSet attrs,int defStyleAttr){
        super(context,attrs,defStyleAttr);
    }



//////////////////////////////////////////////////////////////////////////////////////////
    //基础画板画笔操作
    //初始化画笔样式
    private void setPaintStyle() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);//画笔类型
        mPaint.setStrokeJoin(Paint.Join.ROUND);// 设置外边缘
        mPaint.setStrokeCap(Paint.Cap.ROUND);// 形状
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setDither(true);//防抖动
        if (currentStyle == 1) {
            mPaint.setStrokeWidth(currentSize);
            mPaint.setColor(currentColor);
        } else {//橡皮擦
            mPaint.setAlpha(0);
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            mPaint.setColor(Color.TRANSPARENT);
            mPaint.setStrokeWidth(50);
        }

    }
//////////////////////////////////////////////////////////////////////////////////////////





    @Override
    public void onDraw(Canvas canvas) {

        //////////////////////////////////////////////////////////////////////
        //基础画板
        // 将前面已经画过得显示出来
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        if (mPath != null) {
            // 实时的显示+
            canvas.drawPath(mPath, mPaint);
        }
        /////////////////////////////////////////////////////////////////////


        ////////////////////////////////////////////////////////////////////
        //绘制图形
       //  canvas.drawBitmap(mBitmap2,500,500,mBitmapPaint2);
        ////////////////////////////////////////////////////////////////////
    }


////////////////////////////////////////////////////////////////////////////////////
    //绘制圆形
    public Bitmap drawCircle(){
        mCanvas2.drawCircle(250,200,150,mPaint2);
        return mBitmap2;
    }
////////////////////////////////////////////////////////////////////////////////////






////////////////////////////////////////////////////////////////////////////////////
    //基础画板操作
    private void touch_start(float x, float y) {
        mPath.moveTo(x, y);
        mX = x;
        mY = y;

    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(mY - y);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            if (currentDrawGraphics == DRAW_PATH) {// 从x1,y1到x2,y2画一条贝塞尔曲线，更平滑(直接用mPath.lineTo也可以)
                mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                //mPath.lineTo(mX,mY);
                mX = x;
                mY = y;
            }
        }
    }

    private void touch_up() {
        if (currentDrawGraphics == DRAW_PATH) {
            mPath.lineTo(mX, mY);
        }
        mCanvas.drawPath(mPath, mPaint);
        //将一条完整的路径保存下来
        savePath.add(dp);
        mPath = null;// 重新置空
    }

    // 撤销
    public void undo() {
        if (savePath != null && savePath.size() > 0) {
            DrawPath drawPath = savePath.get(savePath.size() - 1);
            deletePath.add(drawPath);
            savePath.remove(savePath.size() - 1);
            redrawOnBitmap();
        }
    }

    // 重做
    public void redo() {
        if (savePath != null && savePath.size() > 0) {
            savePath.clear();
            redrawOnBitmap();
        }
    }

    private void redrawOnBitmap() {
        initCanvas();
        Iterator<DrawPath> iter = savePath.iterator();
        while (iter.hasNext()) {
            DrawPath drawPath = iter.next();
            mCanvas.drawPath(drawPath.path, drawPath.paint);
        }
        invalidate();// 刷新
    }

    // 恢复
    public void recover() {
        if (deletePath.size() > 0) {
            //将删除的路径列表中的最后一个，也就是最顶端路径取出（栈）,并加入路径保存列表中
            DrawPath dp = deletePath.get(deletePath.size() - 1);
            savePath.add(dp);
            //将取出的路径重绘在画布上
            mCanvas.drawPath(dp.path, dp.paint);
            //将该路径从删除的路径列表中去除
            deletePath.remove(deletePath.size() - 1);
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 每次down下去重新new一个Path
                mPath = new Path();
                //每一次记录的路径对象是不一样的
                dp = new DrawPath();
                dp.path = mPath;
                dp.paint = mPaint;
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);

                preX = (int)x;
                preY = (int)y;

                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }

    //设置画笔样式
    public void selectPaintStyle(int which) {
        if (which == 0) {
            currentStyle = 1;
            setPaintStyle();
        }
        //当选择的是橡皮擦时，设置颜色为白色
        if (which == 1) {
            currentStyle = 2;
            setPaintStyle();
        }
    }
    //选择画笔大小
    public void selectPaintSize(int which) {
        currentSize = which+10;
        setPaintStyle();
    }
    //设置画笔颜色
    public void selectPaintColor(int which) {
        currentColor = paintColor[which];
        setPaintStyle();
    }
}
/////////////////////////////////////////////////////////////////////