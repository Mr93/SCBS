package com.example.dendimon.scbs;


        import java.io.File;
        import java.io.FileInputStream;
        import java.io.FileOutputStream;
        import java.util.ArrayList;

        import android.app.Activity;
        import android.content.res.AssetFileDescriptor;
        import android.database.Cursor;
        import android.net.Uri;
        import android.os.Bundle;
        import android.os.Environment;
        import android.provider.ContactsContract;
        import android.util.Log;
        import android.view.View;

public class VcardActivity extends Activity
{
    Cursor cursor;
    ArrayList<String> vCard ;
    String vfile;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.backup);

        /**This Function For Vcard And here i take one Array List in Which i store every Vcard String of Every Conatact
         * Here i take one Cursor and this cursor is not null and its count>0 than i repeat one loop up to cursor.getcount() means Up to number of phone contacts.
         * And in Every Loop i can make vcard string and store in Array list which i declared as a Global.
         * And in Every Loop i move cursor next and print log in logcat.
         * */
        getVcardString();

    }
    private void getVcardString() {
        // TODO Auto-generated method stub

        vCard = new ArrayList<String>();
        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if(cursor!=null&&cursor.getCount()>0)
        {

            Log.d("COUNT ", " " +cursor.getCount());
            cursor.moveToFirst();
            for(int i =0;i<cursor.getCount();i++)
            {

                vfile = "Contacts" + "_" + System.currentTimeMillis()+".vcf";
                get(cursor);
                Log.d("Test", "Contact "+(i+1)+"VcF String is"+vCard.get(i));
                cursor.moveToNext();
            }

//            int i = 0;
//            while (cursor.moveToNext()){
//                get(cursor);
//                Log.d("TAG", "Contact " + (i + 1) + "VcF String is"+vCard.get(i));
//                i++;
//            }



        }
        else
        {
            Log.d("TAG", "No Contacts in Your Phone");
        }

    }

    public void get(Cursor cursor)
    {


        //cursor.moveToFirst();
        String lookupKey = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey);
        AssetFileDescriptor fd;
        try {
            fd = this.getContentResolver().openAssetFileDescriptor(uri, "r");

            // Your Complex Code and you used function without loop so how can you get all Contacts Vcard.??


//           FileInputStream fis = fd.createInputStream();
//            byte[] buf = new byte[(int) fd.getDeclaredLength()];
//            fis.read(buf);
//            String VCard = new String(buf);
//            String path = Environment.getExternalStorageDirectory().toString() + File.separator + vfile;
//            FileOutputStream out = new FileOutputStream(path);
//            out.write(VCard.toString().getBytes());
//            Log.d("Vcard",  VCard);

            FileInputStream fis = fd.createInputStream();
            byte[] buf = new byte[(int) fd.getDeclaredLength()];
            fis.read(buf);
            String vcardstring= new String(buf);
            vCard.add(vcardstring);

            String storage_path =  Environment.getExternalStorageDirectory().toString() + File.separator + vfile;
            FileOutputStream mFileOutputStream = new FileOutputStream(storage_path, false);
            mFileOutputStream.write(vcardstring.toString().getBytes());

        } catch (Exception e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }}