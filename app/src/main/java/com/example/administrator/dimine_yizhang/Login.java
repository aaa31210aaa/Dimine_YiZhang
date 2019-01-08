package com.example.administrator.dimine_yizhang;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class Login extends BaseActivity {
    private EditText login_account_etv;
    private EditText login_account_password;
    private Button login_btn;
    private CheckBox login_save_account;
    private String myurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
        setOnClick();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 延迟发送退出
     */
    private void exit() {
        if (!isExit) {
            isExit = true;
            ShowToast.showShort(this, R.string.click_agin);
            // 利用handler延迟发送更改状态信息
            handler.sendEmptyMessageDelayed(0, send_time);
        } else {
            finish();
            System.exit(0);
        }
    }


    @Override
    protected void initView() {
        login_account_etv = (EditText) findViewById(R.id.login_account_etv);
        login_account_password = (EditText) findViewById(R.id.login_account_password);
        login_save_account = (CheckBox) findViewById(R.id.login_save_account);
        login_btn = (Button) findViewById(R.id.login_btn);
        login_btn.setBackgroundColor(Color.rgb(255, 236, 139));
        login_btn.setEnabled(false);
    }

    @Override
    protected void initData() {
        AccountPassEtv();
        SaveAccount();
    }

    private void mOkhttp() {
        url = PortIpAddress.LoginAddress();
        OkHttpUtils
                .get()
                .url(url)
                .addParams("loginname", login_account_etv.getText().toString().trim())
                .addParams("loginpwd", login_account_password.getText().toString().trim())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showToastNoWait(Login.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.e(TAG, response + "--");
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String error = jsonObject.getString("errormessage");
                            if (success.equals("true")) {
                                String token = jsonObject.getString("access_token");
                                String userid = jsonObject.getString("userid");
                                String username = jsonObject.getString("username");
                                String headurl = jsonObject.getString("headurl");
                                String sf = jsonObject.getString("sf");
                                SharedPrefsUtil.putValue(Login.this, "userInfo", "user_token", token);
                                SharedPrefsUtil.putValue(Login.this, "userInfo", "username", username);
                                SharedPrefsUtil.putValue(Login.this, "userInfo", "userid", userid);
                                SharedPrefsUtil.putValue(Login.this, "userInfo", "headurl", headurl);
                                SharedPrefsUtil.putValue(Login.this, "userInfo", "sf", sf);

                                if (login_save_account.isChecked()) {
                                    SharedPrefsUtil.putValue(Login.this, "userInfo", "USER_NAME", login_account_etv.getText().toString().trim());
                                }

                                Message message = handler.obtainMessage();
                                message.what = 1;
                                handler.sendMessage(message);
                            } else {
                                dialog.dismiss();
                                ShowToast.showShort(Login.this, error);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    public Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 0:
                    isExit = false;
                    break;
                case 1:
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    dialog.dismiss();
                    finish();
                    ShowToast.showShort(Login.this, R.string.login_success);
                    break;
                default:
                    break;
            }
        }
    };


    /**
     * 监听账号密码输入框
     */
    private void AccountPassEtv() {
        login_account_etv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                NoSpace(login_account_etv, s, start);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (login_account_etv.getText().toString().length() != 0 && login_account_password.getText().toString().length() != 0) {
                    login_btn.setEnabled(true);
                    login_btn.setBackgroundResource(R.drawable.login_btn_style);
                } else {
                    login_btn.setEnabled(false);
                }
            }
        });

        login_account_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                NoSpace(login_account_password, s, start);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (login_account_etv.getText().toString().length() != 0 && login_account_password.getText().toString().length() != 0) {
                    login_btn.setEnabled(true);
                    login_btn.setBackgroundResource(R.drawable.login_btn_style);
                } else {
                    login_btn.setEnabled(false);
                }
            }
        });
    }

    /**
     * 设置记住账号
     */
    private void SaveAccount() {
        //判断记住密码多选框的状态
        if (SharedPrefsUtil.getValue(this, "userInfo", "ISCHECK", false)) {
            //设置默认是记住账号状态
            login_save_account.setChecked(true);
            login_account_etv.setText(SharedPrefsUtil.getValue(this, "userInfo", "USER_NAME", ""));

            if (login_account_etv.getText().toString().trim().equals("")) {
                login_account_etv.requestFocus();
            } else {
                login_account_password.requestFocus();
            }
        }

        //监听保存账号的选择框
        login_save_account.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (login_save_account.isChecked()) {
                    SharedPrefsUtil.putValue(Login.this, "userInfo", "ISCHECK", true);
                } else {
                    SharedPrefsUtil.putValue(Login.this, "userInfo", "ISCHECK", false);
                }
            }
        });
    }

    @Override
    protected void setOnClick() {
        login_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                if (login_account_etv.getText().toString().trim().equals("") && !login_account_password.getText().toString().trim().equals("")) {
                    ShowToast.showShort(Login.this, "请填写账号");
                } else if (!login_account_etv.getText().toString().trim().equals("") && login_account_password.getText().toString().trim().equals("")) {
                    ShowToast.showShort(Login.this, "请填写密码");
                } else if (login_account_etv.getText().toString().trim().equals("") && login_account_password.getText().toString().trim().equals("")) {
                    ShowToast.showShort(Login.this, "请填写登陆信息");
                } else {
                    dialog = DialogUtil.createLoadingDialog(Login.this, R.string.loading_write);
                    mOkhttp();
                }
                break;
        }
    }

}
