package com.minjer.smarthome.deviceFragent;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.minjer.smarthome.R;
import com.minjer.smarthome.http.ActionClient;
import com.minjer.smarthome.pojo.Device;
public class SensorLightFragment extends Fragment {

    private static final String TAG = "LightFragment";
    private Device device;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            device = getArguments().getParcelable("device");
        }
    }

    public static SensorLightFragment newInstance(Device device) {
        SensorLightFragment fragment = new SensorLightFragment();
        Bundle args = new Bundle();
        args.putParcelable("device", device);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_senser_light, container, false);

        String brightness = ActionClient.getLightIntensity(getContext(), device.getID());

        TextView tv_light = rootView.findViewById(R.id.light_value);

        tv_light.setText(brightness + " lux");

        return rootView;
    }

}