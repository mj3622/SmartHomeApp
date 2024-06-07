package com.minjer.smarthome;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.minjer.smarthome.utils.ParamUtil;

public class MessageBoxActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String messageListJson = ParamUtil.getString(this, ParamUtil.MESSAGE_LIST, null);
        if (messageListJson == null) {

            setContentView(R.layout.default_message_box);
            findViewById(R.id.message_go_back_button_default).setOnClickListener(v -> finish());
        } else {

            setContentView(R.layout.activity_message_box);
            findViewById(R.id.message_go_back_button).setOnClickListener(v -> finish());
        }
    }
}