package com.nonemin.carlightcontroller;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.nonemin.carlightcontroller.databinding.FragmentFirstBinding;


public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    //记录状态
    int lm,fm;

    boolean blon = false; //大灯状态
    boolean breakon = false; //刹车状态
    boolean ReOn = false;   //倒车状态

    boolean Lon = false; //左转开启状态
    boolean warning = false;//警示灯开启状态
    boolean Ron = false; //右转开启状态

    boolean beeon = false;//蜂鸣器状态

    boolean l1on = false;
    boolean l2on = false;
    boolean l3on = false;
    boolean l4on = false;
    boolean l5on = false;

    MainActivity mainActivity;// = (MainActivity) getActivity();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        //buttonConnect = binding.TBTStatus;
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        binding.btnLm0.setOnClickListener(btnClick);
        binding.btnLm1.setOnClickListener(btnClick);
        binding.btnLm2.setOnClickListener(btnClick);
        binding.btnFm0.setOnClickListener(btnClick);
        binding.btnFm1.setOnClickListener(btnClick);
        binding.btnFm2.setOnClickListener(btnClick);
        binding.btnBlon.setOnClickListener(btnClick);
        binding.btnBl.setOnClickListener(btnClick);
        binding.btnRl.setOnClickListener(btnClick);
        binding.btnL.setOnClickListener(btnClick);
        binding.btnR.setOnClickListener(btnClick);
        binding.btnW.setOnClickListener(btnClick);
        binding.btnBee.setOnClickListener(btnClick);
        binding.btnL1.setOnClickListener(btnClick);
        binding.btnL2.setOnClickListener(btnClick);
        binding.btnL3.setOnClickListener(btnClick);
        binding.btnL4.setOnClickListener(btnClick);
        binding.btnL5.setOnClickListener(btnClick);

    }

    View.OnClickListener btnClick = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View v) {
            Log.i("btn",v.getId() + "");
            switch (v.getId()){
                case R.id.btn_lm0:send(0);lm = 0;break;
                case R.id.btn_lm1:send(1);lm = 1;break;
                case R.id.btn_lm2:send(2);lm = 2;break;
                case R.id.btn_fm0:send(3);fm = 0;break;
                case R.id.btn_fm1:send(4);fm = 1;break;
                case R.id.btn_fm2:send(5);fm = 2;break;
                //case R.id.btn_bloff:send(6);break;
                case R.id.btn_blon:
                    if(blon){
                        send(6);
                        blon = false;
                    }else{
                        send(7);
                        blon = true;
                    }
                    break;
                case R.id.btn_bl:
                    if(breakon){
                        send(8);
                        breakon = false;
                    }else{
                        send(9);
                        breakon = true;
                    }
                    break;
                case R.id.btn_rl:
                    if(ReOn){
                        send(10);
                        ReOn = false;
                    }else{
                        send(11);
                        ReOn = true;
                    }
                    break;
                case R.id.btn_l:
                    if(Lon){
                        send(12);
                        Lon = false;
                    }else{
                        send(13);
                        Lon = true;
                    }
                    break;
                case R.id.btn_r:
                    if(Ron){
                        send(12);
                        Ron = false;
                    }else{
                        send(14);
                        Ron = true;
                    }
                    break;
                case R.id.btn_w:
                    if(warning){
                        send(15);
                        warning = false;
                    }else{
                        send(16);
                        warning = true;
                    }
                    break;
                case R.id.btn_bee:
                    if(beeon){
                        send(17);
                        beeon = false;
                    }else{
                        send(18);
                        beeon = true;
                    }
                    break;
                case R.id.btn_l1:
                    if(l1on){
                        send(19);
                        l1on = false;
                    }else{
                        send(20);
                        l1on = true;
                    }
                    break;
                case R.id.btn_l2:
                    if(l2on){
                        send(21);
                        l2on = false;
                    }else{
                        send(22);
                        l2on = true;
                    }
                    break;
                case R.id.btn_l3:
                    if(l3on){
                        send(23);
                        l3on = false;
                    }else{
                        send(24);
                        l3on = true;
                    }
                    break;
                case R.id.btn_l4:
                    if(l4on){
                        send(25);
                        l4on = false;
                    }else{
                        send(26);
                        l4on = true;
                    }
                    break;
                case R.id.btn_l5:
                    if(l5on){
                        send(27);
                        l5on = false;
                    }else{
                        send(28);
                        l5on = true;
                    }
                    break;
            }
            updateButton();
        }
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public void send(int command) {
        mainActivity.send(command);
    }


    //更新按钮显示
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void updateButton(){
        Drawable btn = new ColorDrawable(Color.argb(80,3,218,197));
        AlphaAnimation alphaAnimation1 = new AlphaAnimation(0.1f, 1.0f);
        alphaAnimation1.setDuration(500);
        alphaAnimation1.setRepeatCount(Animation.INFINITE);
        alphaAnimation1.setRepeatMode(Animation.RESTART);
        alphaAnimation1.setStartTime(500);
        //view.setAnimation(alphaAnimation1);
        //alphaAnimation1.start();
        if(lm == 0){
            binding.btnLm1.setForeground(null);
            binding.btnLm2.setForeground(null);
        }
        if(lm == 1){
            binding.btnLm1.setForeground(btn);
            binding.btnLm2.setForeground(null);
        }
        if(lm == 2){
            binding.btnLm1.setForeground(null);
            binding.btnLm2.setForeground(btn);
        }
        if(fm == 0){
            binding.btnFm1.setForeground(null);
            binding.btnFm2.setForeground(null);
        }
        if(fm == 1){
            binding.btnFm1.setForeground(btn);
            binding.btnFm2.setForeground(null);
        }
        if(fm == 2){
            binding.btnFm1.setForeground(null);
            binding.btnFm2.setForeground(btn);
        }
        binding.btnBlon.setForeground(blon ? btn : null);
        binding.btnBl.setForeground(breakon ? btn : null);
        binding.btnRl.setForeground(ReOn ? btn : null);
        binding.btnL.setForeground(Lon ? btn : null);
        binding.btnW.setForeground(warning ? btn : null);
        binding.btnR.setForeground(Ron ? btn : null);
        binding.btnBee.setForeground(beeon ? btn : null);
        binding.btnL1.setForeground(l1on ? btn : null);
        binding.btnL2.setForeground(l2on ? btn : null);
        binding.btnL3.setForeground(l3on ? btn : null);
        binding.btnL4.setForeground(l4on ? btn : null);
        binding.btnL5.setForeground(l5on ? btn : null);
    }




}