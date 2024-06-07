package com.minjer.smarthome;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AboutFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        rootView.findViewById(R.id.debug_button).setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), DebugActivity.class);
            startActivity(intent);
        });

        // Inflate the layout for this fragment
        return rootView;
    }
}