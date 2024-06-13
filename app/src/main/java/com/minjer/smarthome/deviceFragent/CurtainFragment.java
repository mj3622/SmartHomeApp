package com.minjer.smarthome.deviceFragent;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.minjer.smarthome.R;
import com.minjer.smarthome.http.ActionClient;
import com.minjer.smarthome.pojo.Action;
import com.minjer.smarthome.pojo.Device;
import com.minjer.smarthome.utils.ParamUtil;
import com.minjer.smarthome.utils.TimeUtil;

public class CurtainFragment extends Fragment {

    private static final String TAG = "CurtainFragment";
    private Device device;

    ImageView curtainClose;
    ImageView curtainOpen;

    Spinner curtainSpeed;

    TextView curtainPosition;

    SeekBar curtainOpenSeekBar;

    Button curtainOpenMinus;

    Button curtainOpenPlus;

    EditText curtainOpenTime;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            device = getArguments().getParcelable("device");
        }
    }

    public static CurtainFragment newInstance(Device device) {
        CurtainFragment fragment = new CurtainFragment();
        Bundle args = new Bundle();
        args.putParcelable("device", device);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_curtain, container, false);

        curtainClose = rootView.findViewById(R.id.curtainCloseImage);
        curtainOpen = rootView.findViewById(R.id.curtainOpenImage);
        curtainSpeed = rootView.findViewById(R.id.curtainSpeed);
        curtainOpenTime = rootView.findViewById(R.id.curtainTime);

        curtainPosition = rootView.findViewById(R.id.curtainOpenSetting);
        curtainOpenSeekBar = rootView.findViewById(R.id.curtainOpenSeekBar);
        curtainOpenMinus = rootView.findViewById(R.id.curtainOpenMinus);
        curtainOpenPlus = rootView.findViewById(R.id.curtainOpenPlus);

        String curtainPosition = ParamUtil.getString(getContext(), ParamUtil.CURTAIN_POSITION, "100");
        if (curtainPosition.equals("0")) {
            curtainClose.setVisibility(View.VISIBLE);
            curtainOpen.setVisibility(View.GONE);
        } else {
            curtainClose.setVisibility(View.GONE);
            curtainOpen.setVisibility(View.VISIBLE);
        }

        // 设定窗帘运行速度
        initCurtainSpeed();

        // 控制窗帘开启程度
        initCurtainOpenSetting();

        // 控制窗帘开启暂停关闭
        initCuratinControl(rootView);

        // 窗帘的配置页面
        rootView.findViewById(R.id.settingCurtain).setOnClickListener(v -> {
            // 分别配置上升和下降时间
            LinearLayout curtainSettingDialogLayout = new LinearLayout(getContext());
            curtainSettingDialogLayout.setOrientation(LinearLayout.VERTICAL);

            TextView upTimeSetting = new TextView(getContext());
            upTimeSetting.setText("上升时间");
            curtainSettingDialogLayout.addView(upTimeSetting);
            EditText upTime = new EditText(getContext());
            upTime.setHint("上升时间");
            // 设置输入类型为数字
            upTime.setInputType(2);
            curtainSettingDialogLayout.addView(upTime);

            TextView downTimeSetting = new TextView(getContext());
            downTimeSetting.setText("下降时间");
            curtainSettingDialogLayout.addView(downTimeSetting);
            EditText downTime = new EditText(getContext());
            downTime.setHint("下降时间");
            // 设置输入类型为数字
            downTime.setInputType(2);
            curtainSettingDialogLayout.addView(downTime);

            new android.app.AlertDialog.Builder(getContext())
                    .setTitle("窗帘配置")
                    .setView(curtainSettingDialogLayout)
                    .setPositiveButton("确定", (dialog, which) -> {
                        String upTimeStr = upTime.getText().toString();
                        String downTimeStr = downTime.getText().toString();
                        if (!upTimeStr.isEmpty() && !downTimeStr.isEmpty()) {
                            Action action = new Action(device.getID(), Action.ACTION_TYPE_CONFIG_SPEED, TimeUtil.getNowMillis(), device.getType(), upTimeStr + "," + downTimeStr);
                            ActionClient.sendAction(getContext(), action);
                        }
                    })
                    .setNegativeButton("取消", (dialog, which) -> {
                    })
                    .show();

        });

        return rootView;
    }

    private void initCurtainOpenSetting() {
        String savePosition = ParamUtil.getString(this.getContext(), ParamUtil.CURTAIN_OPEN_SETTING, "100");
        curtainPosition.setText(savePosition + "%");
        curtainOpenSeekBar.setProgress(Integer.parseInt(savePosition) / 10);
        curtainOpenSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                curtainPosition.setText(progress * 10 + "%");
                ParamUtil.saveString(getContext(), ParamUtil.CURTAIN_OPEN_SETTING, progress * 10 + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        curtainOpenMinus.setOnClickListener(v -> {
            int progress = curtainOpenSeekBar.getProgress();
            if (progress > 0) {
                curtainOpenSeekBar.setProgress(progress - 1);
                curtainPosition.setText((progress - 1) * 10 + "%");
                ParamUtil.saveString(getContext(), ParamUtil.CURTAIN_OPEN_SETTING, (progress - 1) * 10 + "");
            }
        });

        curtainOpenPlus.setOnClickListener(v -> {
            int progress = curtainOpenSeekBar.getProgress();
            if (progress < 10) {
                curtainOpenSeekBar.setProgress(progress + 1);
                curtainPosition.setText((progress + 1) * 10 + "%");
                ParamUtil.saveString(getContext(), ParamUtil.CURTAIN_OPEN_SETTING, (progress + 1) * 10 + "");
            }
        });
    }

    private void initCuratinControl(View rootView) {
        // 关闭窗帘
        rootView.findViewById(R.id.closeCurtain).setOnClickListener(v -> {
            Action action = new Action(device.getID(), Action.ACTION_TYPE_CLOSE, TimeUtil.getNowMillis(), device.getType());
            ActionClient.sendAction(rootView.getContext(), action);
            curtainClose.setVisibility(View.VISIBLE);
            curtainOpen.setVisibility(View.GONE);

            ParamUtil.saveString(getContext(), ParamUtil.CURTAIN_POSITION, "0");
        });

        // 开启窗帘
        rootView.findViewById(R.id.openCurtain).setOnClickListener(v -> {
            String position = ParamUtil.getString(this.getContext(), ParamUtil.CURTAIN_OPEN_SETTING, "100");
            Action action = new Action(device.getID(), Action.ACTION_TYPE_OPEN, TimeUtil.getNowMillis(), device.getType(), position);
            ActionClient.sendAction(rootView.getContext(), action);
            curtainClose.setVisibility(View.GONE);
            curtainOpen.setVisibility(View.VISIBLE);

            ParamUtil.saveString(getContext(), ParamUtil.CURTAIN_POSITION, position);
        });

        // 暂停窗帘
        rootView.findViewById(R.id.pauseCurtain).setOnClickListener(v -> {
            Action action = new Action(device.getID(), Action.ACTION_TYPE_PAUSE, TimeUtil.getNowMillis(), device.getType());
            ActionClient.sendAction(rootView.getContext(), action);
        });

    }

    private void initCurtainSpeed() {
        // 获取窗帘速度
        String saveSpeed = ParamUtil.getString(this.getContext(), ParamUtil.CURTAIN_SPEED, "20");
        curtainOpenTime.setText(saveSpeed);
//        假如使用选项卡的初始化设定
//        if (saveSpeed.equals("慢速")) curtainSpeed.setSelection(0);
//        else if (saveSpeed.equals("快速")) curtainSpeed.setSelection(2);
//        else curtainSpeed.setSelection(1);

        // TODO 设置窗帘速度 (废弃了，看后面再测试的时候能不能重新用回来吧)
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String speed = (String) curtainSpeed.getSelectedItem();
                ParamUtil.saveString(getContext(), ParamUtil.CURTAIN_SPEED, speed);
                // TODO 发送窗帘速度 此处速度要做一些转换(已废弃，现在使用输入框输入，保留不影响功能)
                if (speed.equals("慢速")) speed = "10";
                else if (speed.equals("中速")) speed = "5";
                else if (speed.equals("快速")) speed = "2";
                else speed = "5";
                ActionClient.sendAction(getContext(), new Action(device.getID(), Action.ACTION_TYPE_CONFIG_SPEED, TimeUtil.getNowMillis(), device.getType(), speed));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        curtainSpeed.setOnItemSelectedListener(listener);

        curtainOpenTime.addTextChangedListener(new TextWatcher() {
            private Handler handler = new Handler();
            private Runnable delayedAction;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 清除之前的延迟任务
                handler.removeCallbacksAndMessages(null);

                // 设置延迟执行的任务
                try {
                    delayedAction = new Runnable() {
                        @Override
                        public void run() {
                            // 这里放置你希望在用户停止输入1秒后执行的代码
                            String speed = curtainOpenTime.getText().toString();
                            if (!speed.isEmpty()) {
                                ParamUtil.saveString(getContext(), ParamUtil.CURTAIN_SPEED, speed);
                                ActionClient.sendAction(getContext(), new Action(device.getID(), Action.ACTION_TYPE_CONFIG_SPEED, TimeUtil.getNowMillis(), device.getType(), speed));
                            }
                        }
                    };

                    // 延迟800毫秒后执行延迟任务
                    handler.postDelayed(delayedAction, 800);
                } catch (Exception e) {
                    Log.e(TAG, "afterTextChanged: ", e);
                    String speed = curtainOpenTime.getText().toString();
                    if (!speed.isEmpty()) {
                        ParamUtil.saveString(getContext(), ParamUtil.CURTAIN_SPEED, speed);
                        ActionClient.sendAction(getContext(), new Action(device.getID(), Action.ACTION_TYPE_CONFIG_SPEED, TimeUtil.getNowMillis(), device.getType(), speed));
                    }
                }
            }
        });
    }
}