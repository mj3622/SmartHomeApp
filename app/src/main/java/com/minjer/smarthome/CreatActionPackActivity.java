package com.minjer.smarthome;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.minjer.smarthome.adapter.ActionAdapter;
import com.minjer.smarthome.pojo.Action;
import com.minjer.smarthome.pojo.ActionPack;
import com.minjer.smarthome.pojo.Device;
import com.minjer.smarthome.utils.ActionUtil;
import com.minjer.smarthome.utils.DeviceUtil;
import com.minjer.smarthome.utils.DialogUtil;
import com.minjer.smarthome.utils.TaskPackUtil;
import com.minjer.smarthome.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

public class CreatActionPackActivity extends AppCompatActivity {

    private static final String TAG = "CreatActionPackActivity";

    ArrayList<Action> actions;
    ActionAdapter actionAdapter;
    LinearLayout emptyView;
    ListView actionListView;

    ArrayList<Device> devices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_action_pack);

        actionListView = findViewById(R.id.addtaskpack_item_list);
        emptyView = findViewById(R.id.addtaskpack_empty);

        devices = DeviceUtil.getDeviceList(this);
        actions = ActionUtil.getPackCreatTempList(this);
        actionAdapter = new ActionAdapter(this, R.layout.item_action, actions);
        actionListView.setAdapter(actionAdapter);

        checkEmptyView();

        findViewById(R.id.addtaskpack_go_back_button).setOnClickListener(v -> {
            finish();
        });

        findViewById(R.id.addtaskpack_add_button).setOnClickListener(v -> {
            LinearLayout createActionView = new  LinearLayout(this);
            createActionView.setPadding(50, 50, 50, 50);
            createActionView.setOrientation(LinearLayout.VERTICAL);
            // 创建名字选择栏
            Spinner nameSpinner = new Spinner(this);
            nameSpinner.setPadding(0, 0, 0, 50);
            ArrayList<String> deviceName = new ArrayList<>();
            for (Device device : devices) {
                if (DeviceUtil.isControlDevice(device.getType())) {
                    deviceName.add(device.getName());
                }
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, deviceName.toArray(new String[0]));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            nameSpinner.setAdapter(adapter);
            createActionView.addView(nameSpinner);

            // 创建行为选择栏
            Spinner actionSpinner = new Spinner(this);
            ArrayAdapter<String> actionTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"打开", "关闭"});
            actionSpinner.setAdapter(actionTypeAdapter);
            actionSpinner.setPadding(0, 0, 0, 50);
            createActionView.addView(actionSpinner);

            // 创建添加行为
            new AlertDialog.Builder(this)
                    .setTitle("添加行为")
                    .setView(createActionView)
                    .setPositiveButton("确定", (dialog, which) -> {
                        String name = (String) nameSpinner.getSelectedItem();
                        String action = (String) actionSpinner.getSelectedItem();
                        Device device = null;
                        for (Device d : devices) {
                            if (d.getName().equals(name)) {
                                device = d;
                                break;
                            }
                        }
                        if (device == null) {
                            DialogUtil.showToastShort(this, "设备不存在");
                            return;
                        }
                        Action newAction = new Action();
                        newAction.setDeviceId(device.getID());
                        newAction.setDeviceType(device.getType());
                        newAction.setTime(TimeUtil.getNowMillis());
                        newAction.setActionType(action.equals("打开") ? Action.ACTION_TYPE_OPEN : Action.ACTION_TYPE_CLOSE);
                        actions.add(newAction);
                        actionAdapter.notifyDataSetChanged();
                        checkEmptyView();
                    })
                    .setNegativeButton("取消", null)
                    .show();
        });

        initTopBar();

    }

    private void initTopBar() {
        findViewById(R.id.addtaskpack_go_back_button).setOnClickListener(v -> {
            finish();
        });

        findViewById(R.id.addtaskpack_save_button).setOnClickListener(v -> {

            EditText et_title = findViewById(R.id.addtaskpack_title);
            EditText et_desc = findViewById(R.id.addtaskpack_desc);
            ActionPack actionPack = new ActionPack();
            actionPack.setPackName(et_title.getText().toString());
            actionPack.setPackDescription(et_desc.getText().toString());
            actionPack.setActions(actions);
            Log.d(TAG, "Create ActionPack: " + actionPack.getPackName() + " " + actionPack.getPackDescription() + " " + actionPack.getActions().size());
            DialogUtil.showToastShort(this, "创建调度成功");

            TaskPackUtil.addActionPack(this, actionPack);

            // 更新上级页面的信息
            Intent resultIntent = new Intent();
            setResult(Activity.RESULT_OK, resultIntent);


            finish();
        });
    }

    private void checkEmptyView() {
        if (actions.isEmpty()) {
            emptyView.setVisibility(LinearLayout.VISIBLE);
            actionListView.setVisibility(ListView.GONE);
        } else {
            emptyView.setVisibility(LinearLayout.GONE);
            actionListView.setVisibility(ListView.VISIBLE);
        }
    }
}