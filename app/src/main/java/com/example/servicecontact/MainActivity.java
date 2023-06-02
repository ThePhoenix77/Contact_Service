package com.example.servicecontact;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.servicecontact.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText firstName, lastName, phoneMobile, email, adresse ;
    FloatingActionButton fabSave, viewContactList ;
    DatabaseHelper db ;


    //viewBinding :
    private ActivityMainBinding binding ;

    //tag :
    private static final String TAG="CONTACT_TAG" ;

    //requete permission de contact :
    private static final int WRITE_CONTACT_PERMISSION_CODE=100 ;

    //image pick gallery intent :
    private static final int IMAGE_PICK_GALLERY_CODE=200 ;

    //list des permissions pour l'ecriture des contacts :
    private String[] contactPermissions ;

    //image URI :
    private Uri image_uri ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater()) ;
        setContentView(binding.getRoot());

        //id linking :
        firstName=findViewById(R.id.firstNameEt) ;
        lastName=findViewById(R.id.lastNameEt) ;
        phoneMobile=findViewById(R.id.phoneMobileEt) ;
        email=findViewById(R.id.emailEt) ;
        adresse=findViewById(R.id.adressEt) ;
        fabSave=findViewById(R.id.fabSave) ;
        viewContactList=findViewById(R.id.viewlist) ;

        db=new DatabaseHelper(this) ;


        viewContactList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ViewContact.class));
            }
        });

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstNameTXT=firstName.getText().toString() ;
                String lastNameTXT=lastName.getText().toString() ;
                String phoneMobileTXT=phoneMobile.getText().toString() ;
                String emailTXT=email.getText().toString() ;
                String adresseTXT=adresse.getText().toString() ;

                Boolean checkinsertdata=db.insertuserdata(firstNameTXT, lastNameTXT, phoneMobileTXT, emailTXT, adresseTXT) ;
                if(checkinsertdata==true){
                    Toast.makeText(MainActivity.this,"Contact ajoute",Toast.LENGTH_SHORT).show();
                }else Toast.makeText(MainActivity.this,"Veuillez repeter",Toast.LENGTH_SHORT).show();
            }
        });

        //init permission :
        contactPermissions=new String[]{Manifest.permission.WRITE_CONTACTS} ;

        //thumbnail click(choix image):
        binding.thumbnailIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalleryIntent() ;
            }
        });
        //sauvegarde de contact action :
        binding.fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isWriteContactPermissionEnabled()){
                    saveContact();
                }else {
                    requestWriteContactsPermission();
                }
            }
        });
    }

    private void saveContact() {
        //data input :
        String firstName=binding.firstNameEt.getText().toString().trim() ;
        String lastName=binding.lastNameEt.getText().toString().trim() ;
        String phoneMobile=binding.phoneMobileEt.getText().toString().trim() ;
        String email=binding.emailEt.getText().toString().trim() ;
        String adresse=binding.adressEt.getText().toString().trim() ;

        ArrayList<ContentProviderOperation> cpo=new ArrayList<>() ;

        //id contact :
        int rawConatctId=cpo.size() ;
        cpo.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE,null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME,null)
        .build()) ;

        //ajout nom et prenom :
        cpo.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID,rawConatctId)
                .withValue(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,firstName)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME,lastName)
                .build()) ;

        //ajout numero mobile :
        cpo.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID,rawConatctId)
                .withValue(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,phoneMobile)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build()) ;

        //ajout email :
        cpo.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID,rawConatctId)
                .withValue(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.DATA,email)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                .build()) ;

        //ajout adresse :
        cpo.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID,rawConatctId)
                .withValue(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.SipAddress.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.SipAddress.DATA,adresse)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,ContactsContract.CommonDataKinds.SipAddress.TYPE_CUSTOM)
                .build()) ;

        //ajout image(conversion en bytes pour stockage) :
        byte[] imageBytes=imageUriToBytes() ;
        if (imageBytes!=null){
                //contact avec image :
            cpo.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID,rawConatctId)
                    .withValue(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO,imageBytes)
                    .build()) ;
        }else {
                //contact sans image :
        }


        //sauvegarde contact :
        try {
            ContentProviderResult[] results=getContentResolver().applyBatch(ContactsContract.AUTHORITY,cpo) ;
            long newContactId = ContentUris.parseId(results[0].uri);
            Log.d(TAG, "saveContact: Succes");
            Toast.makeText(this, "Sauvegarde...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "saveContact: "+e.getMessage());
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private byte[] imageUriToBytes() {
        Bitmap bitmap ;
        ByteArrayOutputStream baos=null ;

        try{
            bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),image_uri) ;
            baos=new ByteArrayOutputStream() ;
            bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos) ;
            return baos.toByteArray() ;

        }catch(Exception e){
            Log.d(TAG, "imageUriToBytes: "+e.getMessage());
            return null ;
        }
    }

    private void openGalleryIntent() {
        //intent pour le choic image de la gallerie :
        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI) ;
        startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);
    }

    private boolean isWriteContactPermissionEnabled(){
        boolean result=ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_CONTACTS)==(PackageManager.PERMISSION_GRANTED) ;
        return result;
    }

    private void requestWriteContactsPermission(){
        ActivityCompat.requestPermissions(this,contactPermissions,WRITE_CONTACT_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //permission result :
        if (grantResults.length>0){
            if (requestCode==WRITE_CONTACT_PERMISSION_CODE){
                boolean haveWriteContactPermission=grantResults[0]==PackageManager.PERMISSION_GRANTED ;
                if (haveWriteContactPermission){
                    //permission granted :
                    saveContact() ;
                }else {
                    //permission denied :
                    Toast.makeText(this, "Vous n'avez pas de permission.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //image choisie ou non :
            //si :
        if (resultCode == RESULT_OK) {
            if(resultCode==IMAGE_PICK_GALLERY_CODE){
                image_uri=data.getData() ;

                //imageView set :
                binding.thumbnailIv.setImageURI(image_uri);
            }
        }else {
            //cancel :
            Toast.makeText(this, "Annuler", Toast.LENGTH_SHORT).show();
        }
    }
}