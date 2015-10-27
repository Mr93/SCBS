package com.example.dendimon.scbs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;

/**
 * Created by Dendimon on 10/23/2015.
 */

//http://android-coding.blogspot.com/2012/07/dialogfragment-with-interface-to-pass.html

public class TestDialogFragment extends DialogFragment {
    TextView pathFolder;
    EditText pathItem;
    Button btnok, btncancel;
    int requestCode;
    String pathCode;
    String timeCode = ""+System.currentTimeMillis();

    static TestDialogFragment newInstance(int requestCode,String pathCode) {
        TestDialogFragment testDialogFragment = new TestDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("code", requestCode);
        bundle.putString("path", pathCode);
        testDialogFragment.setArguments(bundle);
        return testDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestCode = getArguments().getInt("code");
        pathCode = getArguments().getString("path");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.dialog_backup_contact, container, false);

        pathFolder = (TextView)dialogView.findViewById(R.id.pathFolderContact);
        btnok = (Button)dialogView.findViewById(R.id.ok);
        btncancel = (Button)dialogView.findViewById(R.id.cancel);
        btnok.setOnClickListener(btnok_updateOnClickListener);
        pathItem = (EditText)dialogView.findViewById(R.id.pathContact);

        pathFolder.setText(pathCode+ File.separator);
        if(requestCode == 2) {
            pathItem.setText("SMS_" + timeCode + ".xml");
        }else if(requestCode == 1){
            pathItem.setText("Contacts_"+timeCode+".vcf");
        }
        btncancel.setOnClickListener(btncancel_updateOnClickListener);



        return dialogView;
    }


    private Button.OnClickListener btnok_updateOnClickListener
            = new Button.OnClickListener(){

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            EditDialogListener activity = (EditDialogListener) getActivity();
            activity.updateResult(pathFolder.getText().toString()+ pathItem.getText().toString());
            dismiss();
        }
    };

    private Button.OnClickListener btncancel_updateOnClickListener
            = new Button.OnClickListener(){

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            dismiss();
        }
    };

    public interface EditDialogListener {
        void updateResult(String inputText);
    }
}
