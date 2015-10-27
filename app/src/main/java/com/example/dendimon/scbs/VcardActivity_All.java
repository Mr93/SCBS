package com.example.dendimon.scbs;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;

import com.nononsenseapps.filepicker.FilePickerActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Dendimon on 10/15/2015.
 */
public class VcardActivity_All extends FragmentActivity implements TestDialogFragment.EditDialogListener {
    String vfile = null;
    int FILE_CODE = 9999;
    static Context mContext;
    ArrayList<String> Lcheck;
    int requestCodeContact = 1;
    String defaultPathFolder = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "SCBS_Contact";
    Uri pathUri = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createFolder();

        // This always works
        Intent i = new Intent(this, FilePickerActivity.class);
        // This works if you defined the intent filter
        // Intent i = new Intent(Intent.ACTION_GET_CONTENT);

        // Set these depending on your use case. These are the defaults.
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
        i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_DIR);

        // Configure initial directory by specifying a String.
        // You could specify a String like "/storage/emulated/0/", but that can
        // dangerous. Always use Android's API calls to get paths to the SD-card or
        // internal memory.
        i.putExtra(FilePickerActivity.EXTRA_START_PATH,defaultPathFolder);

        startActivityForResult(i, FILE_CODE);

//        if (vfile!=null){
//            setContentView(R.layout.backup);
//            mContext = VcardActivity_All.this;
//            createFolder();
//            getVCF();
//        }


    }

    @Override
    public void updateResult(String inputText) {
        vfile = inputText;
        setContentView(R.layout.backup);
        mContext = VcardActivity_All.this;
        getVCF();
    }


    private  void getVCF() {
    //    final String vfile = "Contacts_"+vTime+".vcf";
//        File sSCBS = new File("/sdcard/SCBS/"+System.currentTimeMillis());
//        sSCBS.mkdir();

     //   File sSCBS = new File(Environment.getExternalStorageDirectory().toString()+ "/SCBS");
        Cursor phones = mContext.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        phones.moveToFirst();
        for (int i = 0; i < phones.getCount(); i++) {

            String lookupKey = phones.getString(phones.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                    lookupKey = lookupKey.trim();
            if(Lcheck == null){
                Lcheck = new ArrayList<String>();
                Lcheck.add(lookupKey);
            }else {
                if(isDuplicate(lookupKey)){
                    phones.moveToNext();
                    continue;
                }else {
                    Lcheck.add(lookupKey);
                }
            }
                Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey);
                AssetFileDescriptor fd;
                try {
                    fd = mContext.getContentResolver().openAssetFileDescriptor(uri, "r");
                    FileInputStream fis = fd.createInputStream();
                    byte[] buf = new byte[(int) fd.getDeclaredLength()];
                    Log.d("FD", " " + (int) fd.getDeclaredLength());
                    fis.read(buf); // truyen du lieu vao buf
                    String VCard = new String(buf);

                    String path = vfile;
                            //Environment.getExternalStorageDirectory().toString() + File.separator + vfile;
                    Log.d("Duong_dan",path.toString());


                    Log.d("Path", mContext.getFilesDir().toString());
                    FileOutputStream mFileOutputStream = new FileOutputStream(path, true);


                    mFileOutputStream.write(VCard.toString().getBytes());
                    phones.moveToNext();
                    Log.d("Vcard", VCard);
                    mFileOutputStream.close();
                    fis.close();

                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } }


        }

    public boolean isDuplicate (String lkKey){
        for (String s:Lcheck){
            if(s.equals(lkKey)){
                return true;
            }
        }
        return false;
    }


    public void createFolder(){

        File SCBS = new File(defaultPathFolder);
        if (!SCBS.exists()){
        SCBS.mkdir();}
    }

    /*public String getEnvironment(){
        return Environment.getExternalStorageDirectory().toString();
    }*/

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILE_CODE && resultCode == Activity.RESULT_OK) {
            if (data.getBooleanExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false)) {
                // For JellyBean and above
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ClipData clip = data.getClipData();

                    if (clip != null) {
                        for (int i = 0; i < clip.getItemCount(); i++) {
                            Uri uri = clip.getItemAt(i).getUri();
                            // Do something with the URI
                            Log.d("Test_path", uri.toString());
                        }


                    }
                    // For Ice Cream Sandwich
                } else {
                    ArrayList<String> paths = data.getStringArrayListExtra
                            (FilePickerActivity.EXTRA_PATHS);

                    if (paths != null) {
                        for (String path: paths) {
                            Uri uri = Uri.parse(path);
                            // Do something with the URI
                            Log.d("Test_path", uri.toString());
                        }
                    }
                }

            } else {
                Uri uri = data.getData();
                pathUri = uri;
                Log.d("Test_path", uri.getPath().toString());
            }
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if(pathUri!=null) {
            DialogFragment newFragment = TestDialogFragment.newInstance(requestCodeContact, pathUri.getPath().toString());
            newFragment.show(getSupportFragmentManager(), "dialog");
            pathUri = null;
        }
    }

}
