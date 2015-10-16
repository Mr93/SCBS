package com.example.dendimon.scbs;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by Dendimon on 10/15/2015.
 */
public class VcardActivity_All extends Activity {
    Cursor cursor;
    ArrayList<String> vCard;
    String vfile;
    static Context mContext;
    ArrayList<String> Lcheck;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.backup);
        mContext = VcardActivity_All.this;
        getVCF();
        createFolder();
    }

    private  void getVCF() {
        final String vfile = "Contacts"+"_"+System.currentTimeMillis()+".vcf";
        File sSCBS = new File("/sdcard/SCBS/"+System.currentTimeMillis());
        sSCBS.mkdir();
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

                    String path = sSCBS.getPath()+ File.separator + vfile;
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

        File SCBS = new File("/sdcard/SCBS");
        if (!SCBS.exists()){
        SCBS.mkdir();}
    }

}
