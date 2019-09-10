package com.example.paint;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ShapesDialog extends DialogFragment {
    private RadioButton rb_picture;
    private RadioButton rb_round;
    private RadioButton rb_square;
    private RadioButton rb_delta;
    private RadioButton rb_arrow;
    private RadioButton rb_line;
    private TuyaView tuyaView;

private ShapesListener ShapesClick;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.shapes_dialog,container,false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        rb_round=(RadioButton)view.findViewById(R.id.rb_round);

        return view;
    }


    public void onAttach(Activity activity){
        super.onAttach(activity);
        ShapesClick = (ShapesListener)activity;

    }
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        rb_round.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShapesClick.ShapesClick();

            }
        });
    }


    public void onResume(){
        super.onResume();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();//显示窗口尺寸
        window.setGravity(Gravity.TOP);//窗口显示位置
        lp.x=155;
        lp.y=200;
        lp.width=490;
        lp.height=165;
        lp.alpha=1f;
        lp.dimAmount=0f;
        window.setAttributes(lp);
    }
}
