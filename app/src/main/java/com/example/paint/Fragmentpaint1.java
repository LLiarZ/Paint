package com.example.paint;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Fragmentpaint1 extends Fragment {

    View RootView;
    private TextView red;

    //声明接口
    private OnColorListener onColorClick;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        RootView = layoutInflater.inflate(R.layout.fp1,container,false);
        red = (TextView)RootView.findViewById(R.id.paint1_red);

        return RootView;
    }



    //声明接口
    public void onAttach(Activity activity){
        super.onAttach(activity);
        onColorClick = (OnColorListener)activity;
    }



    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onColorClick.onColorClick(0);

            }
        });

    }
}
