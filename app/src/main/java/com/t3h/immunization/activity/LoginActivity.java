package com.t3h.immunization.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.muddzdev.styleabletoast.StyleableToast;
import com.t3h.immunization.R;
import com.t3h.immunization.api.ApiBuilder;
import com.t3h.immunization.model.User;
import com.t3h.immunization.respone.ResponeLogin;
import com.t3h.immunization.utils.AppPreferences;
import com.t3h.immunization.utils.SaveData;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.t3h.immunization.utils.Constant.KEY_NEXT;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.edt_name_login)
    EditText edtName;
    @BindView(R.id.edt_password)
    EditText edtPass;
    @BindView(R.id.checkbox)
    CheckBox checkBox;
    private SharedPreferences sharedPreferences;
    @BindView(R.id.tv_forget)
    TextView tvForget;
    private Handler handler =new Handler();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SaveData.updateLangua(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        intView();
    }

    private void intView() {
        AppPreferences.getInstance(getApplicationContext()).putBoolean(KEY_NEXT, true);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        tvForget.setOnClickListener(this);
       edtName.setText( AppPreferences.getInstance(getApplicationContext()).getString(("taikhoan")));
      edtPass.setText( AppPreferences.getInstance(getApplicationContext()).getString("matkhau"));
        Log.e("LOGIN", "key Check box  "+AppPreferences.getInstance(getApplicationContext()).getBoolean("checked") );
        checkBox.setChecked(AppPreferences.getInstance(getApplicationContext()).getBoolean("checked"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                Intent register = new Intent(this, RegisterActivity.class);
                startActivity(register);
                break;
            case R.id.tv_forget:
                Intent intent = new Intent(this, ForgotActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_login:
                callApiLogin();
                break;
        }
    }
    public  void callApiLogin() {
        String user_name = edtName.getText().toString();
        String password = edtPass.getText().toString();
        if (user_name.isEmpty() && password.isEmpty()) {
            StyleableToast.makeText(this, getResources().getString(R.string.toast),R.style.ColoredText).show();
        }
        progressDialog = new ProgressDialog(this,R.style.CustomDialog);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getResources().getString(R.string.message));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        ApiBuilder.getInstance().login(user_name, password).enqueue(new Callback<ResponeLogin>() {
            @Override
            public void onResponse(Call<ResponeLogin> call, Response<ResponeLogin> response) {
                if (response.body().getStatus() == true) {
                    Log.e("TAG", "CHECK LOGIN SUCCESS!");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();

                        }
                    },500);
                    Intent intent = new Intent(LoginActivity.this, CategoriActivity.class);
                    startActivity(intent);
                    StyleableToast.makeText(LoginActivity.this, getResources().getString(R.string.success),R.style.ColoredText).show();
                    User.getInstans().setId(response.body().getData().getId());
                    if (checkBox.isChecked()) {
                        AppPreferences.getInstance(getApplicationContext()).putString("taikhoan", user_name);
                        AppPreferences.getInstance(getApplicationContext()).putString("matkhau", password);
                        AppPreferences.getInstance(getApplicationContext()).putBoolean("checked", true);
                        Log.e("LOGIN", "onResponse: check  "+AppPreferences.getInstance(getApplicationContext()).getBoolean("checked") );

                    } else {
                        AppPreferences.getInstance(getApplicationContext()).putString("taikhoan", "");
                        AppPreferences.getInstance(getApplicationContext()).putString("matkhau", "");
                        AppPreferences.getInstance(getApplicationContext()).putBoolean("checked", false);
                        Log.e("LOGIN", "onResponse: not check "+  AppPreferences.getInstance(getApplicationContext()).getBoolean("checked"));

                    }
                    finish();
                } else {
                    progressDialog.dismiss();
                    StyleableToast.makeText(LoginActivity.this, getResources().getString(R.string.error),R.style.ColoredText).show();
                }

            }

            @Override
            public void onFailure(Call<ResponeLogin> call, Throwable t) {
                Log.e("TAG", "CHECK LOGIN failed!");
                t.printStackTrace();
            }
        });
    }
}



