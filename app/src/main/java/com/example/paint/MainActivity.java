package com.example.paint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;


        public class MainActivity extends AppCompatActivity implements View.OnClickListener,OnColorListener, ShapesListener {
    private FrameLayout frameLayout;
    private RadioGroup  mGroup;
    private RadioButton btn_undo;
    private RadioButton btn_redo;
    private RadioButton btn_save;
    private RadioButton btn_recover;
    private TuyaView tuyaView;//自定义涂鸦板
    private RadioButton btn_paintcolor;
    private RadioButton btn_paintsize;
    private RadioButton btn_paintstyle;

    private SeekBar sb_size;

    private DragView dragView;
    private Context mContext;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initListener();

        mContext =this;

        dragView =(DragView)findViewById(R.id.dragview);

        btn_save = (RadioButton)findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPaintStyleDialog(view);
            }
        });

        btn_paintcolor = (RadioButton)findViewById(R.id.btn_paintcolor);
        btn_paintcolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            showShapesDialog(view);
            }
        });

    }

    public void showPaintStyleDialog(View view){
        PaintStyleDialog dialog = new PaintStyleDialog();
        dialog.show(getSupportFragmentManager(),"paintstyledialog");
    }


    private void initView() {
        frameLayout = (FrameLayout) findViewById(R.id.fl_boardcontainer);
        mGroup = (RadioGroup)findViewById(R.id.rd_group);
        btn_undo = (RadioButton)findViewById(R.id.btn_undo);
        btn_redo = (RadioButton)findViewById(R.id.btn_redo);
        btn_recover = (RadioButton)findViewById(R.id.btn_recover);
        btn_save = (RadioButton)findViewById(R.id.btn_save);
        btn_paintcolor = (RadioButton) findViewById(R.id.btn_paintcolor);
        btn_paintsize = (RadioButton) findViewById(R.id.btn_paintsize);
        btn_paintstyle = (RadioButton) findViewById(R.id.btn_paintstyle);
        sb_size = (SeekBar) findViewById(R.id.sb_size);
    }
    private void initData() {
        //虽然此时获取的是屏幕宽高，但是我们可以通过控制framlayout来实现控制涂鸦板大小
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        int screenWidth = defaultDisplay.getWidth();
        int screenHeight = defaultDisplay.getHeight();
        tuyaView = new TuyaView(this,screenWidth,screenHeight);
        frameLayout.addView(tuyaView);
        tuyaView.requestFocus();
        tuyaView.selectPaintSize(sb_size.getProgress());


    }


    private void initListener() {
        sb_size.setOnSeekBarChangeListener(new MySeekChangeListener());
        mGroup.setOnClickListener(this);
        btn_undo.setOnClickListener(this);
        btn_redo.setOnClickListener(this);
        btn_recover.setOnClickListener(this);
        btn_paintcolor.setOnClickListener(this);
        btn_paintsize.setOnClickListener(this);
        btn_paintstyle.setOnClickListener(this);
    }
    class MySeekChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            tuyaView.selectPaintSize(seekBar.getProgress());
            //Toast.makeText(MainActivity.this,"当前画笔尺寸为"+seekBar.getProgress(),Toast.LENGTH_SHORT ).show();
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            tuyaView.selectPaintSize(seekBar.getProgress());
            //Toast.makeText(MainActivity.this,"当前画笔尺寸为"+seekBar.getProgress(),Toast.LENGTH_SHORT ).show();
        }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_undo://撤销
                tuyaView.undo();
                break;
            case R.id.btn_redo://重做
                tuyaView.redo();
                break;
            case R.id.btn_recover://恢复
                tuyaView.recover();
                break;
            case R.id.btn_paintcolor://选择画笔颜色
                showShapesDialog(v);
                break;
            case R.id.btn_paintsize://选择画笔尺寸
                sb_size.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_paintstyle://选择画笔类型
                sb_size.setVisibility(View.GONE);
                showMoreDialog(v);
                break;
        }
   }

    public void showShapesDialog(View v){
        ShapesDialog dialog = new ShapesDialog();
        dialog.show(getSupportFragmentManager(),"shapesdialog");
    }


    private int select_paint_style_index = 0;
//弹出选择画笔或橡皮擦的对话框
    public void showMoreDialog(View group){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("选择画笔或橡皮擦：");
        alertDialogBuilder.setSingleChoiceItems(R.array.paintstyle, select_paint_style_index, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                select_paint_style_index = which;
                tuyaView.selectPaintStyle(which);
                dialog.dismiss();
            }
        });
        alertDialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.create().show();
    }

    //实现抽象方法
    public void onColorClick(int which){
       tuyaView.selectPaintColor(which);

    }
    public void ShapesClick(){
       dragView.addDragView(R.layout.my_self_view,350,500,750,900,true,false);
       tuyaView.drawCircle();
    }
   }
