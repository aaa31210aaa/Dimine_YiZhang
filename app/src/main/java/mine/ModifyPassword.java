package mine;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.administrator.dimine_yizhang.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import utils.BaseActivity;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class ModifyPassword extends BaseActivity {
    private ImageView modify_password_back;
    private EditText modify_password_old_etv;
    private EditText modify_password_new_etv;
    private EditText modify_password_again_etv;
    private Button modify_password_submit_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        modify_password_back = (ImageView) findViewById(R.id.modify_password_back);
        modify_password_old_etv = (EditText) findViewById(R.id.modify_password_old_etv);
        modify_password_new_etv = (EditText) findViewById(R.id.modify_password_new_etv);
        modify_password_again_etv = (EditText) findViewById(R.id.modify_password_again_etv);
        modify_password_submit_btn = (Button) findViewById(R.id.modify_password_submit_btn);
    }

    @Override
    protected void initData() {
        url = PortIpAddress.ModifyPwd();
        user_token = SharedPrefsUtil.getValue(this, "userInfo", "user_token", null);
        Monitor();
    }

    //监听Editext
    private void Monitor() {
        modify_password_old_etv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                NoSpace(modify_password_old_etv, s, start);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        modify_password_new_etv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                NoSpace(modify_password_new_etv, s, start);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        modify_password_again_etv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                NoSpace(modify_password_again_etv, s, start);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    @Override
    protected void setOnClick() {
        modify_password_back.setOnClickListener(this);
        modify_password_submit_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.modify_password_back:
                finish();
                break;
            case R.id.modify_password_submit_btn:
                if (modify_password_old_etv.getText().toString().trim().equals("")) {
                    ShowToast.showShort(this, "请填写当前密码");
                } else if (modify_password_new_etv.getText().toString().trim().equals("")) {
                    ShowToast.showShort(this, "请填写新密码");
                } else if (modify_password_again_etv.getText().toString().trim().equals("")) {
                    ShowToast.showShort(this, "请确认新密码");
                } else {
                    mOkhttp();
                }
                break;
        }
    }


    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("oldpasswd", modify_password_old_etv.getText().toString())
                .addParams("newpasswd", modify_password_new_etv.getText().toString())
                .addParams("confirmpasswd", modify_password_again_etv.getText().toString())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, response + "----" + modify_password_old_etv.getText().toString() + "---" + modify_password_new_etv.getText().toString() + "-----" + modify_password_again_etv.getText().toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String err = jsonObject.getString("errormessage");
                            if (success.equals("false")) {
                                ShowToast.showShort(ModifyPassword.this, err);
                            } else {
                                ShowToast.showShort(ModifyPassword.this, "修改成功");
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
