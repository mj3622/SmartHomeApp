package com.minjer.smarthome;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.minjer.smarthome.adapter.ActionPackAdapter;
import com.minjer.smarthome.adapter.MessageAdapter;
import com.minjer.smarthome.pojo.ActionPack;
import com.minjer.smarthome.pojo.Message;
import com.minjer.smarthome.utils.PageUtil;
import com.minjer.smarthome.utils.ParamUtil;
import com.minjer.smarthome.utils.TaskPackUtil;

import java.util.ArrayList;
import java.util.List;

public class TaskListActivity extends AppCompatActivity {
    private List<ActionPack> actionPackList = new ArrayList<>();
    private ActionPackAdapter actionPackAdapter;
    private ListView actionPackListView;
    private LinearLayout emptyView;

    private final static int REQUEST_CODE_MODIFY_DEVICE = 1;

    private static final String TAG = "TaskListActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        initList();

        // 设置增加任务按钮
        findViewById(R.id.tasklist_add_button).setOnClickListener(v -> {
            Intent intent = new Intent(this, CreatActionPackActivity.class);
            startActivityForResult(intent, REQUEST_CODE_MODIFY_DEVICE);
        });

        // 设置返回按钮
        findViewById(R.id.tasklist_go_back_button).setOnClickListener(v -> {
            finish();
        });

    }

    private void initList() {
        emptyView = findViewById(R.id.tasklist_empty);
        actionPackListView = findViewById(R.id.taskpack_item_list);

        actionPackList = TaskPackUtil.getActionPackPackList(this);
        actionPackAdapter = new ActionPackAdapter(this, R.layout.item_taskpack, actionPackList);
        actionPackListView.setAdapter(actionPackAdapter);

        checkActionPackList();
    }

    private void checkActionPackList() {
        if (actionPackList.isEmpty()) {
            actionPackListView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            actionPackListView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, requestCode + " " + resultCode);
        if (requestCode == REQUEST_CODE_MODIFY_DEVICE) {
            Log.d(TAG, "Update device list");
            PageUtil.refreshPage(this);
        }
    }
}