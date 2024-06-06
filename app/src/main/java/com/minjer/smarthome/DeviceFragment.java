package com.minjer.smarthome;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.minjer.smarthome.http.DataClient;

public class DeviceFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_device, container, false);

//        // 测试用
//         rootView.findViewById(R.id.button_test).setOnClickListener(v -> {
//             DataClient.loginCheck("100000", "123456");
//         });

        // Inflate the layout for this fragment
        return rootView;
    }
}