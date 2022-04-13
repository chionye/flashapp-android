package com.example.myapplication.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import java.net.URISyntaxException;

public class EnterAddressFragment extends Fragment {
    EditText editText[];
    Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_enter_address, container, false);
        editText= new EditText[]{root.findViewById(R.id.frame_15), root.findViewById(R.id.frame_14)};
        Button cxz = (Button)root.findViewById(R.id.button);
        for (int i = 0;i < editText.length; i++){
            editText[i].setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasfocus) {
                    if (!hasfocus) {
                        if(editText[0].getText().length() != 0 && editText[1].getText().length() != 0){
                            try {
                                new MainActivity().GetLocationData(editText[0].getText().toString(), editText[1].getText().toString());
                            } catch (URISyntaxException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        }

        return root;
    }


}