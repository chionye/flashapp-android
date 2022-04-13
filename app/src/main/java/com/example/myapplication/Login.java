package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Helper.DialogBox;
import com.example.myapplication.Helper.Logger;
import com.example.myapplication.Helper.NetworkConnection;
import com.example.myapplication.Helper.Routes;
import com.example.myapplication.Helper.SharedPreferencesWork;
import com.google.android.material.textfield.TextInputLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class Login extends AppCompatActivity {

    EditText email, password;
    Button login;
    TextView textView, textView1;
    boolean isEmailValid, isPasswordValid;
    TextInputLayout emailError, passError;
    String stremail, strpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
        new SharedPreferencesWork(Login.this).checkForLogin();
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.button);
        textView = findViewById(R.id.don_t_have_);
        textView1 = findViewById(R.id.forgot_pass);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Forgot_Password.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gologin();
            }
        });
    }

    private void gologin() {
        stremail = email.getText().toString();
        strpass = password.getText().toString();
        if (NetworkConnection.checkNetworkConnection(this)) {
            ArrayList validationMessage = new ArrayList();
            if (stremail.length() > 0) {
            } else
                validationMessage.add("Email is not valid");

            if (password.getText().toString().length() > 0) {
            } else
                validationMessage.add("password field is empty");

            if (validationMessage.size() > 0) {
                String allmessage = "";
                for (int i = 0; i < validationMessage.size(); i++) {
                    allmessage += "\n" + validationMessage.get(i);
                    new DialogBox(this, allmessage).asyncDialogBox();
                    validationMessage.clear();
                }
            } else {
                RequestParams params = new RequestParams();
                params.add("email", stremail);
                params.add("password", strpass);
                params.add("tbname", "main_table");
                AsyncHttpClient client = new AsyncHttpClient();
                client.post(Routes.selectOneByColumn, params, new AsyncHttpResponseHandler() {
                    ProgressDialog dialog;

                    @Override
                    public void onStart() {
                        dialog = ProgressDialog.show(Login.this, "Working",
                                "Signing in...", true);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        dialog.dismiss();
                        String str = new String(responseBody);
//                        Logger.Logger(str);
                        JSONObject jo = null;
                        JSONArray jr = null;
                        if (statusCode == 200) {
                            try {
                                jr = new JSONArray(str);
                                jo = jr.getJSONObject(0);
                                String userid = jo.getString("main_id");
                                HashMap hm = new HashMap();
                                hm.put("email", stremail);
                                hm.put("password", strpass);
                                hm.put("id", userid);

                                new SharedPreferencesWork(Login.this).insertOrReplace(hm, Routes.sharedPrefForLogin);
                                Intent admin = new Intent(Login.this, MainActivity.class);
                                startActivity(admin);
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Logger.Logger(statusCode + str);
                            Toast.makeText(Login.this, "Enter right credentials", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        dialog.dismiss();
                        Toast.makeText(Login.this, "Connection Timed out", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onRetry(int retryNo) {
                        dialog = ProgressDialog.show(Login.this, "Logging in...",
                                "Saving.Please wait...", true);
                    }
                });
            }
        } else {
            new DialogBox(this, "Check your network connection").asyncDialogBox();
        }
    }
}