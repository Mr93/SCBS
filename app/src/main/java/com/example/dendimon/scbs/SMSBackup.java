package com.example.dendimon.scbs;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Telephony;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.Xml;

import com.nononsenseapps.filepicker.FilePickerActivity;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

/**
 * Created by Dendimon on 10/19/2015.
 */
public class SMSBackup extends FragmentActivity implements TestDialogFragment.EditDialogListener {
    static Context mcontext;
    int FILE_CODE = 1111;
    int requestCodeSMS = 2;
    String sFile = null;
    String defaultPathFolder = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "SCBS_SMS";
    Uri pathUri = null;

    @Override
    public  void  onCreate (Bundle savedInstanceState){
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

        /*setContentView(R.layout.backupsms);
        mcontext = SMSBackup.this;
        createFolder();
        getXML();*/

    }

    @Override
    public void updateResult(String inputText){
        sFile = inputText;
        setContentView(R.layout.backupsms);
        mcontext = SMSBackup.this;

        getXML();
    }

    private void createFolder(){
        File SCBS_SMS = new File(defaultPathFolder);
        if(!SCBS_SMS.exists()){
            SCBS_SMS.mkdir();
        }
    }

    private void getXML (){

        /*final String sTime = ""+System.currentTimeMillis() ;
        final String sFile = "SMS"+"_"+sTime+".xml";
        final String sPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SCBS_SMS/"+sTime*/;
        //Log.d("PathXML", sPath);
         FileOutputStream fileos = null;
        XmlSerializer xmlSerializer;

        try{
             fileos = new FileOutputStream(new File(sFile),true);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //Telephony.Sms.Inbox.CONTENT_URI


        Cursor sCursor = mcontext.getContentResolver().query(Uri.parse("content://sms"),null,null,null,null);
        sCursor.moveToFirst();

            try{

                xmlSerializer = Xml.newSerializer();
                StringWriter writer = new StringWriter();
                xmlSerializer.setOutput(writer);
                xmlSerializer.startDocument("UTF-8", true);
                xmlSerializer.startTag(null, "smsData");
                for (int i = 0;i < sCursor.getCount();i++){
                    if(sCursor.getString(sCursor.getColumnIndex(Telephony.Sms.ADDRESS))!=null){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                            xmlSerializer.startTag(null, "item");
                            xmlSerializer.startTag(null, "_id");
                            xmlSerializer.text(sCursor.getString(sCursor.getColumnIndex(Telephony.Sms._ID)));
                            xmlSerializer.endTag(null, "_id");
                            xmlSerializer.startTag(null, "thread_id");
                            xmlSerializer.text(sCursor.getString(sCursor.getColumnIndex(Telephony.Sms.THREAD_ID)));
                            xmlSerializer.endTag(null, "thread_id");
                            xmlSerializer.startTag(null, "address");
                            xmlSerializer.text(sCursor.getString(sCursor.getColumnIndex(Telephony.Sms.ADDRESS)));
                            xmlSerializer.endTag(null, "address");
                            xmlSerializer.startTag(null, "date");
                            xmlSerializer.text(sCursor.getString(sCursor.getColumnIndex(Telephony.Sms.DATE)));
                            xmlSerializer.endTag(null, "date");
                            xmlSerializer.startTag(null, "date_sent");
                            xmlSerializer.text(sCursor.getString(sCursor.getColumnIndex(Telephony.Sms.DATE_SENT)));
                            xmlSerializer.endTag(null, "date_sent");
                            xmlSerializer.startTag(null, "read");
                            xmlSerializer.text(sCursor.getString(sCursor.getColumnIndex(Telephony.Sms.READ)));
                            xmlSerializer.endTag(null, "read");
                            xmlSerializer.startTag(null, "status");
                            xmlSerializer.text(sCursor.getString(sCursor.getColumnIndex(Telephony.Sms.STATUS)));
                            xmlSerializer.endTag(null, "status");
                            xmlSerializer.startTag(null, "type");
                            xmlSerializer.text(sCursor.getString(sCursor.getColumnIndex(Telephony.Sms.TYPE)));
                            xmlSerializer.endTag(null, "type");
                            xmlSerializer.startTag(null, "body");
                            xmlSerializer.text(sCursor.getString(sCursor.getColumnIndex(Telephony.Sms.BODY)));
                            xmlSerializer.endTag(null, "body");
                            xmlSerializer.startTag(null, "locked");
                            xmlSerializer.text(sCursor.getString(sCursor.getColumnIndex(Telephony.Sms.LOCKED)));
                            xmlSerializer.endTag(null, "locked");
                            xmlSerializer.endTag(null, "item");
                            Log.d("Check_Error", sCursor.getString(sCursor.getColumnIndex(Telephony.Sms.THREAD_ID)) + " ADDRESS: " + sCursor.getString(sCursor.getColumnIndex(Telephony.Sms.ADDRESS)) + " DATE: " + sCursor.getString(sCursor.getColumnIndex(Telephony.Sms.DATE))
                                    + " READ: " + sCursor.getString(sCursor.getColumnIndex(Telephony.Sms.READ)) + " STATUS: " + sCursor.getString(sCursor.getColumnIndex(Telephony.Sms.STATUS)) + " TYPE: " + sCursor.getString(sCursor.getColumnIndex(Telephony.Sms.TYPE)) +
                                    " BODY: " + sCursor.getString(sCursor.getColumnIndex(Telephony.Sms.BODY)) + " LOCKED: " + sCursor.getString(sCursor.getColumnIndex(Telephony.Sms.LOCKED)));
                        }else {
                            xmlSerializer.startTag(null, "item");
                            xmlSerializer.startTag(null, "thread_id");
                            xmlSerializer.text(sCursor.getString(sCursor.getColumnIndex("thread_id")));
                            xmlSerializer.endTag(null, "thread_id");
                            xmlSerializer.startTag(null, "address");
                            xmlSerializer.text("" + sCursor.getString(sCursor.getColumnIndex("address")));
                            xmlSerializer.endTag(null, "address");
                            xmlSerializer.startTag(null, "date");
                            xmlSerializer.text(sCursor.getString(sCursor.getColumnIndex("date")));
                            xmlSerializer.endTag(null, "date");
                            xmlSerializer.startTag(null, "read");
                            xmlSerializer.text(sCursor.getString(sCursor.getColumnIndex("read")));
                            xmlSerializer.endTag(null, "read");
                            xmlSerializer.startTag(null, "status");
                            xmlSerializer.text(sCursor.getString(sCursor.getColumnIndex("status")));
                            xmlSerializer.endTag(null, "status");
                            xmlSerializer.startTag(null, "type");
                            xmlSerializer.text(sCursor.getString(sCursor.getColumnIndex("type")));
                            xmlSerializer.endTag(null, "type");
                            xmlSerializer.startTag(null, "body");
                            xmlSerializer.text(sCursor.getString(sCursor.getColumnIndex("body")));
                            xmlSerializer.endTag(null, "body");
                            xmlSerializer.startTag(null, "locked");
                            xmlSerializer.text(sCursor.getString(sCursor.getColumnIndex("locked")));
                            xmlSerializer.endTag(null, "locked");
                            xmlSerializer.endTag(null, "item");
                        }
                    }
                    sCursor.moveToNext();
                }
                xmlSerializer.endTag(null, "smsData");
                xmlSerializer.endDocument();
                xmlSerializer.flush();
                String dataWrite = writer.toString();
                fileos.write(dataWrite.getBytes());
                fileos.close();
            }
             //

            catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

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
                // Do something with the URI
                pathUri = uri;
                Log.d("Test_path", uri.getPath().toString());



            }
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if(pathUri!=null) {
            DialogFragment newFragment = TestDialogFragment.newInstance(requestCodeSMS, pathUri.getPath().toString());
            newFragment.show(getSupportFragmentManager(), "dialog");
            pathUri = null;
        }
    }
}
