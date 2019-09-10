package com.example.paint;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class PaintStyleDialog extends DialogFragment {
    private RadioButton paint1;
    private RadioButton paint2;
    private RadioButton paint3;
    private View view;
    private ViewPager myviewpager;
    private RadioGroup myradiogroup;
    private MyFragmentPagerAdapter myadapter;

   @Override
   public void onCreate(@Nullable Bundle savedInstanceState){
       super.onCreate(savedInstanceState);
       setStyle(DialogFragment.STYLE_NO_FRAME,R.style.Dialog2);

   }

   @Override
   public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
       view = inflater.inflate(R.layout.dialogfragment,container);
       return view;
   }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater Inflater = getActivity().getLayoutInflater();
        paint1 = (RadioButton) view.findViewById(R.id.rb_paint1);
        paint2 = (RadioButton)view.findViewById(R.id.rb_paint2);
        paint3 = (RadioButton)view.findViewById(R.id.rb_rubber);

        myviewpager = (ViewPager) view.findViewById(R.id.viewpager2);
        myradiogroup = (RadioGroup)view.findViewById(R.id.rg2);
        myradiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                switch (checkId){
                    case R.id.rb_paint1:myviewpager.setCurrentItem(0);break;
                    case R.id.rb_paint2:myviewpager.setCurrentItem(1);break;
                    case R.id.rb_rubber:myviewpager.setCurrentItem(2);break;
                }
            }
        });
        myviewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:myradiogroup.check(R.id.rb_paint1);break;
                    case 1:myradiogroup.check(R.id.rb_paint2);break;
                    case 2:myradiogroup.check(R.id.rb_rubber);break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        List<Fragment>myfragment = new ArrayList<>();
        myfragment.add(new Fragmentpaint1());
        myfragment.add(new Fragmentpaint2());
        myfragment.add(new Fragmentpaint3());
        myadapter = new MyFragmentPagerAdapter(getChildFragmentManager(),myfragment);
        myviewpager.setAdapter(myadapter);

        //按钮点击响应
        paint1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paint1.setBackgroundResource(R.drawable.selector_text_background1);
                paint2.setBackgroundResource(R.drawable.selector_text_background2);
                paint3.setBackgroundResource(R.drawable.selector_text_background2);
            }
        });
        paint2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paint2.setBackgroundResource(R.drawable.selector_text_background1);
                paint1.setBackgroundResource(R.drawable.selector_text_background2);
                paint3.setBackgroundResource(R.drawable.selector_text_background2);
            }
        });
        paint3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paint3.setBackgroundResource(R.drawable.selector_text_background1);
                paint1.setBackgroundResource(R.drawable.selector_text_background2);
                paint2.setBackgroundResource(R.drawable.selector_text_background2);
            }
        });
        builder.setView(view);
    }



public void onResume(){
    super.onResume();
    Window window = getDialog().getWindow();
    WindowManager.LayoutParams lp = window.getAttributes();
    window.setGravity(Gravity.LEFT|Gravity.TOP);
    lp.x=0;
    lp.y=300;
    lp.width=800;
    lp.height=600;
    lp.alpha=1f;
    lp.dimAmount=0f;
    window.setAttributes(lp);
}
}
