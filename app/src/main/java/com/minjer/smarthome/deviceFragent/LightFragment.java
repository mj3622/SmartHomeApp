package com.minjer.smarthome.deviceFragent;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.minjer.smarthome.R;
import com.minjer.smarthome.http.ActionClient;
import com.minjer.smarthome.pojo.Action;
import com.minjer.smarthome.pojo.Device;
import com.minjer.smarthome.utils.DialogUtil;
import com.minjer.smarthome.utils.TimeUtil;


public class LightFragment extends Fragment {

    private static final String TAG = "LightFragment";
    private Device device;

    private int r = 0;
    private int g = 0;
    private int b = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            device = getArguments().getParcelable("device");
        }
    }

    public static LightFragment newInstance(Device device) {
        LightFragment fragment = new LightFragment();
        Bundle args = new Bundle();
        args.putParcelable("device", device);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_light, container, false);


        initEditView(rootView);

        initButton(rootView);


        return rootView;
    }

    private void initButton(View rootView) {
        rootView.findViewById(R.id.btn_open_light).setOnClickListener(v -> {
            Action action = new Action(device.getID(), Action.ACTION_TYPE_OPEN, TimeUtil.getNowMillis(), Device.TYPE_LIGHT, Action.translateColor(r, g, b));
            ActionClient.sendAction(rootView.getContext(), action);
        });

        rootView.findViewById(R.id.btn_close_light).setOnClickListener(v -> {
            Action action = new Action(device.getID(), Action.ACTION_TYPE_CLOSE, TimeUtil.getNowMillis(), Device.TYPE_LIGHT);
            ActionClient.sendAction(rootView.getContext(), action);
        });

    }

    private void initEditView(View rootView) {

        EditText etRed = rootView.findViewById(R.id.et_red);
        EditText etGreen = rootView.findViewById(R.id.et_green);
        EditText etBlue = rootView.findViewById(R.id.et_blue);

        etRed.setText("0");
        etGreen.setText("0");
        etBlue.setText("0");

        TextView showView = rootView.findViewById(R.id.show_selected_color);
        int color = Color.rgb(0, 0, 0);
        showView.setBackgroundColor(color);

        etRed.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String red = ((android.widget.EditText) v).getText().toString();
                int redInt = Integer.parseInt(red);
                if (redInt < 0 || redInt > 255) {
                    ((android.widget.EditText) v).setText("0");
                    DialogUtil.showToastShort(rootView.getContext(), "红值超出范围，请输入0-255之间的数值");
                    return;
                }
                r = Integer.parseInt(etRed.getText().toString());
                showView.setBackgroundColor(Color.rgb(r, g, b));
            }

        });

        etGreen.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String green = ((android.widget.EditText) v).getText().toString();
                int greenInt = Integer.parseInt(green);
                if (greenInt < 0 || greenInt > 255) {
                    ((android.widget.EditText) v).setText("0");
                    DialogUtil.showToastShort(rootView.getContext(), "绿值超出范围，请输入0-255之间的数值");
                    return;
                }
                g = Integer.parseInt(etGreen.getText().toString());
                showView.setBackgroundColor(Color.rgb(r, g, b));
            }
        });

        etBlue.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String blue = ((android.widget.EditText) v).getText().toString();
                int blueInt = Integer.parseInt(blue);
                if (blueInt < 0 || blueInt > 255) {
                    ((android.widget.EditText) v).setText("0");
                    DialogUtil.showToastShort(rootView.getContext(), "蓝值超出范围，请输入0-255之间的数值");
                    return;
                }
                b = Integer.parseInt(etBlue.getText().toString());
                showView.setBackgroundColor(Color.rgb(r, g, b));
            }
        });

    }

}