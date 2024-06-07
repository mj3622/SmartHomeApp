package com.minjer.smarthome;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.minjer.smarthome.adapter.MessageAdapter;
import com.minjer.smarthome.pojo.Message;
import com.minjer.smarthome.utils.MessageUtil;
import com.minjer.smarthome.utils.PageUtil;
import com.minjer.smarthome.utils.ParamUtil;

import java.util.ArrayList;
import java.util.List;

public class MessageBoxActivity extends AppCompatActivity {
    private List<Message> messageList = new ArrayList<>();
    private MessageAdapter messageAdapter;
    private ListView messageListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String messageListJson = ParamUtil.getString(this, ParamUtil.MESSAGE_LIST, null);
        if (messageListJson == null) {
            setContentView(R.layout.default_message_box);
            findViewById(R.id.message_go_back_button_default).setOnClickListener(v -> finish());
        } else {
            // 完成有消息时的页面设计
            setContentView(R.layout.activity_message_box);
            findViewById(R.id.message_go_back_button).setOnClickListener(v -> finish());
            messageListView = findViewById(R.id.message_list);
            messageList = MessageUtil.getMessageList(this);
            messageAdapter = new MessageAdapter(this, R.layout.item_message, messageList);
            messageListView.setAdapter(messageAdapter);
            // 清理消息
            findViewById(R.id.message_clear_button).setOnClickListener(v -> {
                new AlertDialog.Builder(this)
                        .setTitle("清空消息")
                        .setMessage("确定要清空所有消息吗？")
                        .setPositiveButton("确定", (dialog, which) -> {
                            messageList.clear();
                            messageAdapter.notifyDataSetChanged();
                            MessageUtil.clearMessageList(this);
                            // 启动当前活动的新实例
                            PageUtil.refreshPage(this);

                        })
                        .setNegativeButton("取消", null)
                        .show();
            });
        }
    }
}