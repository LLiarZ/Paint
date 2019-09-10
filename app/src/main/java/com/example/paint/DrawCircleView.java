package com.example.paint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class DrawCircleView extends View {
    private Paint mpaint;// 真实的画笔
   // private Bitmap mBitmap;
   // private Canvas mCanvas;
    //private Paint mBitmapPaint;// 画布的画笔


    public DrawCircleView(Context context){
        super(context);
        initCanvas();
        //drawCircle();
    }
    public DrawCircleView(Context context, AttributeSet attrs){
        super(context,attrs);
    }
    public DrawCircleView(Context context,AttributeSet attrs,int defStyleAttr){
        super(context,attrs,defStyleAttr);
    }

    //绘制图形画布操作
    public void  initCanvas(){
        mpaint = new Paint();
        mpaint.setColor(Color.RED);
        mpaint.setAntiAlias(true);
        mpaint.setStrokeWidth(10);
        mpaint.setStyle(Paint.Style.STROKE);
  //      mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    //    mBitmap = Bitmap.createBitmap(getWidth(),getHeight(),Bitmap.Config.ARGB_8888);
     //   mCanvas = new Canvas(mBitmap);
     //   mCanvas.drawColor(Color.TRANSPARENT);
    }

    public void onDraw(Canvas canvas){
      super.onDraw(canvas);
      //  canvas.drawBitmap(mBitmap,0,0,mBitmapPaint);
      canvas.drawCircle(0,0,150,mpaint);
    }
   /// public Bitmap drawCircle(){
  //      mCanvas.drawCircle(0,0,150,paint);
   //     return mBitmap;
   // }
}
