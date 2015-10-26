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
    TextView pathFolderContact;
    EditText pathContact;
    Button btnok, btncancel;
    int requestCode;
    String vTime = ""+System.currentTimeMillis();

    static TestDialogFragment newInstance(int requestCode) {
        TestDialogFragment testDialogFragment = new TestDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("code", requestCode);
        testDialogFragment.setArguments(bundle);
        return testDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestCode = getArguments().getInt("code");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.dialog_backup_contact, container, false);

        pathFolderContact = (TextView)dialogView.findViewById(R.id.pathFolderContact);
        btnok = (Button)dialogView.findViewById(R.id.ok);
        btncancel = (Button)dialogView.findViewById(R.id.cancel);
        btnok.setOnClickListener(btnok_updateOnClickListener);
        pathContact = (EditText)dialogView.findViewById(R.id.pathContact);

        if(requestCode == 1) {
            pathFolderContact.setText(((VcardActivity_All) getActivity()).getEnvironment() + File.separator + "SCBS" + File.separator +"Contacts_" + vTime + ".vcf");
            pathContact.setText("Contacts_" + vTime + ".vcf");
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
            activity.updateResult(pathContact.getText().toString());
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
