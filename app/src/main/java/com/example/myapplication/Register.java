package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class Register extends AppCompatActivity {

    EditText email, password, fname, phone;
    Button login;
    TextView textView;
    boolean isEmailValid, isPasswordValid;
    TextInputLayout emailError, passError;
    String stremail, strpass, strphone, strname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.email);
        password = findViewById(R.id.pass);
        fname = findViewById(R.id.fname);
        phone = findViewById(R.id.phone);
        login = (Button) findViewById(R.id.button);
        textView = findViewById(R.id.i_already_h);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goRegister();
            }
        });
    }

    private void goRegister() {
        stremail = email.getText().toString();
        strpass = password.getText().toString();
        strphone = phone.getText().toString();
        strname = fname.getText().toString();
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
                params.add("phone", strphone);
                params.add("fullname", strname);
                params.add("tbname", "main_table");
                AsyncHttpClient client = new AsyncHttpClient();
                client.post(Routes.insert, params, new AsyncHttpResponseHandler() {
                    ProgressDialog dialog;

                    @Override
                    public void onStart() {
                        dialog = ProgressDialog.show(Register.this, "Please wait",
                                "Signing up...", true);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        dialog.dismiss();
                        String str = new String(responseBody);
                        if (statusCode == 200) {
                            HashMap hm = new HashMap();
                            hm.put("phone", strphone);
                            hm.put("name", strname);
                            hm.put("email", stremail);
                            hm.put("password", strpass);

                            new SharedPreferencesWork(Register.this).insertOrReplace(hm, Routes.sharedPrefForLogin);
                            Intent admin = new Intent(Register.this, MainActivity.class);
                            startActivity(admin);
                            finish();
                        } else {
                            Logger.Logger(statusCode + str);
                            Toast.makeText(Register.this, "Enter right credentials", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(Register.this, "Connection Timed out", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }


                    @Override
                    public void onRetry(int retryNo) {
                        dialog = ProgressDialog.show(Register.this, "Logging in...",
                                "Saving.Please wait...", true);
                    }
                });
            }
        } else {
            new DialogBox(this, "Check your network connection").asyncDialogBox();
        }
    }
}