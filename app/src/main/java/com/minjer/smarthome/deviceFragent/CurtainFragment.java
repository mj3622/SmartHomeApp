package com.minjer.smarthome.deviceFragent;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.minjer.smarthome.R;
import com.minjer.smarthome.http.ActionClient;
import com.minjer.smarthome.pojo.Action;
import com.minjer.smarthome.pojo.Device;
import com.minjer.smarthome.utils.TimeUtil;

public class CurtainFragment extends Fragment {

    private static final String TAG = "CurtainFragment";
    private Device device;

    ImageView curtainClose;
    ImageView curtainOpen;

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

        rootView.findViewById(R.id.closeCurtain).setOnClickListener(v -> {
            Action action = new Action(device.getID(), Action.ACTION_TYPE_CLOSE, TimeUtil.getNowMillis(), device.getType());
            ActionClient.sendAction(rootView.getContext(), action);
            curtainClose.setVisibility(View.VISIBLE);
            curtainOpen.setVisibility(View.GONE);
        });

        rootView.findViewById(R.id.openCurtain).setOnClickListener(v -> {
            Action action = new Action(device.getID(), Action.ACTION_TYPE_OPEN, TimeUtil.getNowMillis(), device.getType());
            ActionClient.sendAction(rootView.getContext(), action);
            curtainClose.setVisibility(View.GONE);
            curtainOpen.setVisibility(View.VISIBLE);
        });

        return rootView;
    }

}