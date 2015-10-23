package com.example.dendimon.scbs;

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

/**
 * Created by Dendimon on 10/23/2015.
 */

//http://android-coding.blogspot.com/2012/07/dialogfragment-with-interface-to-pass.html

public class TestDialogFragment extends DialogFragment  {
    TextView pathFolderContact;
    EditText pathContact;
    Button btnok, btncancel;

    static TestDialogFragment newInstance() {
        return new TestDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.dialog_backup_contact, container, false);

        pathFolderContact = (TextView)dialogView.findViewById(R.id.pathFolderContact);
        btnok = (Button)dialogView.findViewById(R.id.ok);
        btncancel = (Button)dialogView.findViewById(R.id.cancel);
        btnok.setOnClickListener(btnok_updateOnClickListener);
        btncancel.setOnClickListener(customDialog_DismissOnClickListener);

        pathContact = (EditText)dialogView.findViewById(R.id.pathContact);

        return dialogView;
    }


    private Button.OnClickListener btnok_updateOnClickListener
            = new Button.OnClickListener(){

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            pathFolderContact.setText(customDialog_EditText.getText().toString());
        }
    };

    private Button.OnClickListener customDialog_DismissOnClickListener
            = new Button.OnClickListener(){

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            EditDialogListener activity = (EditDialogListener) getActivity();
            activity.updateResult(customDialog_EditText.getText().toString());

            dismiss();
        }
    };

    public interface EditDialogListener {
        void updateResult(String inputText);
    }
}
