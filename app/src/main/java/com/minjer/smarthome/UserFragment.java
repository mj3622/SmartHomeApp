package com.minjer.smarthome;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minjer.smarthome.http.DataClient;
import com.minjer.smarthome.http.GaodeClient;
import com.minjer.smarthome.utils.DialogUtil;
import com.minjer.smarthome.utils.JsonUtil;
import com.minjer.smarthome.utils.ParamUtil;

import java.util.Map;


public class UserFragment extends Fragment {
    public UserFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);

        // 初始化用户信息
        TextView tv_name = rootView.findViewById(R.id.tv_user_name);
        TextView tv_desc = rootView.findViewById(R.id.tv_user_desc);

        String name = ParamUtil.getString(rootView.getContext(), "name", "未登录");
        String desc = ParamUtil.getString(rootView.getContext(), "desc", "请先登录，以便获取更多信息");

        tv_name.setText(name);
        tv_desc.setText(desc);

        //TODO 任务组点击事件
        rootView.findViewById(R.id.mission_card).setOnClickListener((v) -> {
            DialogUtil.showToastShort(getContext(), "任务组功能暂未开放");
        });

        //TODO 定时任务点击事件
        rootView.findViewById(R.id.timer_card).setOnClickListener((v) -> {
            DialogUtil.showToastShort(getContext(), "定时任务功能暂未开放");
        });

        // 同步点击事件
        rootView.findViewById(R.id.sync_card).setOnClickListener((v) -> {
            String userId = ParamUtil.getString(getContext(), ParamUtil.LOGIN_ID, null);
            if (userId == null) {
                DialogUtil.showToastShort(getContext(), "请先登录");
                return;
            }

            new AlertDialog.Builder(getContext())
                    .setTitle("同步数据")
                    .setMessage("是否要读取云端数据进行覆盖本地数据？")
                    .setPositiveButton("同步", (dialog, which) -> {
                        // 同步数据
                        DataClient.syncData(this.getContext());
                        DialogUtil.showToastShort(getContext(), "同步成功");
                    })
                    .setNegativeButton("取消", (dialog, which) -> {
                        return;
                    })
                    .show();
        });

        // 上传点击事件
        rootView.findViewById(R.id.upload_card).setOnClickListener((v) -> {
            String userId = ParamUtil.getString(getContext(), ParamUtil.LOGIN_ID, null);
            if (userId == null) {
                DialogUtil.showToastShort(getContext(), "请先登录");
                return;
            }

            new AlertDialog.Builder(getContext())
                    .setTitle("上传数据")
                    .setMessage("是否要上传本地数据至云端？")
                    .setPositiveButton("上传", (dialog, which) -> {
                        // 上传数据
                        DataClient.uploadData(this.getContext());
                        DialogUtil.showToastShort(getContext(), "上传成功");
                    })
                    .setNegativeButton("取消", (dialog, which) -> {
                        return;
                    })
                    .show();
        });

        //个人信息点击事件
        rootView.findViewById(R.id.personal_card).setOnClickListener((v) -> {
            String userName = ParamUtil.getString(getContext(), "name", null);
            String description = ParamUtil.getString(getContext(), "desc", null);


            EditText et_userName = new EditText(getContext());
            et_userName.setHint("请输入用户名，不超过8个字符");
            et_userName.setMaxEms(8);

            EditText et_desc = new EditText(getContext());
            et_desc.setHint("请输入个人简介，不超过20个字符");
            et_desc.setMaxEms(20);

            // 创建 LinearLayout 容器,添加两个输入框
            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.addView(et_userName);
            linearLayout.addView(et_desc);

            // 创建提示窗
            new AlertDialog.Builder(getContext())
                    .setTitle("个人信息")
                    .setMessage("请输入用户名和个人简介")
                    .setView(linearLayout) // 设置自定义视图,包含两个 EditText
                    .setPositiveButton("保存", (dialog, which) -> {
                        // 获取用户输入的用户名和个人简介
                        String inputUserName = et_userName.getText().toString();
                        String inputDesc = et_desc.getText().toString();

                        if (inputUserName.isEmpty() || inputDesc.isEmpty()) {
                            DialogUtil.showToastShort(getContext(), "用户名和个人简介不能为空");
                            return;
                        }

                        // 保存用户名和个人简介
                        ParamUtil.saveString(getContext(), ParamUtil.USER_NAME, inputUserName);
                        ParamUtil.saveString(getContext(), ParamUtil.USER_DESC, inputDesc);

                        // 更新界面
                        tv_name.setText(inputUserName);
                        tv_desc.setText(inputDesc);

                        DialogUtil.showToastShort(getContext(), "更新成功");
                    })
                    .setNegativeButton("取消", (dialog, which) -> {
                        DialogUtil.showToastShort(getContext(), "取消保存");
                    })
                    .show();
        });

        //城市设置点击事件
        rootView.findViewById(R.id.city_card).setOnClickListener((v) -> {
            // 获取城市信息
            String response = GaodeClient.getCity();

            if (response == null) {
                DialogUtil.showToastShort(getContext(), "获取失败，请检查网络连接");
                return;
            }

            Map<String, Object> result = JsonUtil.parseToMap(response);
            String province = (String) result.get("province");
            String city = (String) result.get("city");
            String cityCode = (String) result.get("adcode");

            // 设置城市信息
            ParamUtil.saveString(getContext(), ParamUtil.PROVINCE, province);
            ParamUtil.saveString(getContext(), ParamUtil.CITY, city);
            ParamUtil.saveString(getContext(), ParamUtil.CITY_CODE, cityCode);

            // 显示城市信息
            DialogUtil.showToastShort(getContext(), "当前城市：" + city);

        });

        //绑定中枢点击事件
        rootView.findViewById(R.id.bind_card).setOnClickListener((v) -> {
            // 获取绑定情况
            String bindCode = ParamUtil.getString(getContext(), ParamUtil.GATEWAT_CODE, null);
            if (bindCode != null) {
                // 创建提示窗
                new AlertDialog.Builder(getContext())
                        .setTitle("绑定中枢")
                        .setMessage("当前已绑定中枢" + bindCode + "，是否解绑？")
                        .setPositiveButton("解绑", (dialog, which) -> {
                            // 解绑中枢
                            ParamUtil.remove(getContext(), "gatewayCode");
                            DialogUtil.showToastShort(getContext(), "解绑成功");
                        })
                        .setNegativeButton("取消", (dialog, which) -> {
                            return;
                        })
                        .show();
            } else {
                // 创建输入框
                EditText editText = new EditText(getContext());
                // 创建提示窗
                new AlertDialog.Builder(getContext())
                        .setTitle("绑定中枢")
                        .setMessage("请输入智能中枢识别码")
                        .setView(editText) // 设置自定义视图,包含 EditText
                        .setPositiveButton("绑定", (dialog, which) -> {
                            // 获取用户输入的中枢识别码
                            String inputText = editText.getText().toString();

                            if (inputText.isEmpty()) {
                                DialogUtil.showToastShort(getContext(), "中枢识别码不能为空");
                                return;
                            }

                            // 保存中枢识别码
                            ParamUtil.saveString(getContext(), ParamUtil.GATEWAT_CODE, inputText);

                            DialogUtil.showToastShort(getContext(), "绑定成功:");
                        })
                        .setNegativeButton("取消", (dialog, which) -> {
                            DialogUtil.showToastShort(getContext(), "取消绑定");
                        })
                        .show();
            }
        });

        //邮件点击事件
        rootView.findViewById(R.id.bind_email_card).setOnClickListener((v) -> {
            // 获取绑定情况
            String email = ParamUtil.getString(getContext(), ParamUtil.E_MAIL, null);
            if (email != null) {
                // 创建提示窗
                new AlertDialog.Builder(getContext())
                        .setTitle("绑定邮箱")
                        .setMessage("当前已绑定邮箱" + email + "，是否解绑？")
                        .setPositiveButton("解绑", (dialog, which) -> {
                            // 解绑邮箱
                            ParamUtil.remove(getContext(), ParamUtil.E_MAIL);
                            DialogUtil.showToastShort(getContext(), "解绑成功");
                        })
                        .setNegativeButton("取消", (dialog, which) -> {
                            return;
                        })
                        .show();
            } else {
                // 创建输入框
                EditText editText = new EditText(getContext());
                // 创建提示窗
                new AlertDialog.Builder(getContext())
                        .setTitle("绑定邮箱")
                        .setMessage("请输入邮箱地址")
                        .setView(editText) // 设置自定义视图,包含 EditText
                        .setPositiveButton("绑定", (dialog, which) -> {
                            // 获取用户输入的邮箱地址
                            String inputText = editText.getText().toString();

                            if (inputText.isEmpty()) {
                                DialogUtil.showToastShort(getContext(), "邮箱地址不能为空");
                                return;
                            }

                            // 检测邮箱地址格式
                            if (!inputText.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")) {
                                DialogUtil.showToastShort(getContext(), "邮箱地址格式不正确");
                                return;
                            }

                            // 保存邮箱地址
                            ParamUtil.saveString(getContext(), ParamUtil.E_MAIL, inputText);

                            DialogUtil.showToastShort(getContext(), "绑定成功:");
                        })
                        .setNegativeButton("取消", (dialog, which) -> {
                            DialogUtil.showToastShort(getContext(), "取消绑定");
                        })
                        .show();
            }
        });

        //切换账号点击事件
        rootView.findViewById(R.id.switch_account_card).setOnClickListener((v) -> {
            String userId = ParamUtil.getString(getContext(), ParamUtil.LOGIN_ID, null);
            if (userId == null){
                // 1. 用户登录，远程验证账号的合法性
                EditText et_userId = new EditText(getContext());
                et_userId.setHint("请输入账号");
                EditText et_password = new EditText(getContext());
                et_password.setHint("请输入密码");
                et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                // 创建 LinearLayout 容器,添加两个输入框
                LinearLayout linearLayout = new LinearLayout(getContext());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.addView(et_userId);
                linearLayout.addView(et_password);

                // 创建提示窗
                new AlertDialog.Builder(getContext())
                        .setTitle("登录")
                        .setMessage("请输入账号和密码")
                        .setView(linearLayout) // 设置自定义视图,包含两个 EditText
                        .setPositiveButton("登录", (dialog, which) -> {
                            // 获取用户输入的账号和密码
                            String inputUserId = et_userId.getText().toString();
                            String inputPassword = et_password.getText().toString();

                            if (inputUserId.isEmpty() || inputPassword.isEmpty()) {
                                DialogUtil.showToastShort(getContext(), "账号和密码不能为空");
                                return;
                            }

                            Boolean isValid = DataClient.loginCheck(inputUserId, inputPassword);

                            if (!isValid) {
                                DialogUtil.showToastShort(getContext(), "账号或密码错误");
                                return;
                            }

                            // 保存账号信息
                            ParamUtil.saveString(getContext(), ParamUtil.LOGIN_ID, inputUserId);
                            ParamUtil.saveString(getContext(), ParamUtil.LOGIN_PWD, inputPassword);

                            DialogUtil.showToastShort(getContext(), "登录成功");
                        })
                        .setNegativeButton("取消", (dialog, which) -> {
                            DialogUtil.showToastShort(getContext(), "取消登录");
                        })
                        .show();
            }else {
                // 切换账户
                new AlertDialog.Builder(getContext())
                        .setTitle("切换账户")
                        .setMessage("用户："+ userId +"，请问您是否要退出当前账户？")
                        .setPositiveButton("确定", (dialog, which) -> {
                            // 将信息保存至远程
                            DataClient.syncData(this.getContext());

                            // 清除所有用户信息
                           ParamUtil.clear(getContext());

                            // 更新界面
                            tv_name.setText("未登录");
                            tv_desc.setText("请先登录，以便获取更多信息");

                            DialogUtil.showToastShort(getContext(), "已退出登录");
                        })
                        .setNegativeButton("取消", (dialog, which) -> {
                            return;
                        })
                        .show();
            }
        });

        return rootView;
    }
}