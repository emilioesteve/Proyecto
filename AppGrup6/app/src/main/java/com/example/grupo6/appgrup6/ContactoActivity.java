package com.example.grupo6.appgrup6;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ContactoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacto);
    }


    public void llamarServTecnico(View view) {
        startActivity(new Intent(Intent.ACTION_DIAL,
                Uri.parse("tel:" + "646601542")));
    }

    public void pgWeb(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://scaleapp.org/"));
        startActivity(intent);
    }

    public void mandarCorreo(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "asunto");
        intent.putExtra(Intent.EXTRA_TEXT, "texto del correo");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"scaleapp.contacto@contacto.com"});
        startActivity(intent);
    }

    public void abrirFacebook(View view){
        Intent intentFace = openFacebook(ContactoActivity.this);
        startActivity(intentFace);
    }

    public static Intent openFacebook(Context context) {

        try {
            context.getPackageManager()
                    .getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("facebook://gugus.com/inbox"));
        } catch (Exception e){

            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/"));
        }


    }

    public void abrirTwitter(View view){
        Intent intentFace = openTwitter(ContactoActivity.this);
        startActivity(intentFace);
    }

    public static Intent openTwitter(Context context) {

        try {
            context.getPackageManager()
                    .getPackageInfo("com.twitter.android", 0);
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("twitter://user?screen_name=sanpeterparker"));
        } catch (Exception e){

            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://twitter.com/sanpeterparker"));
        }


    }

    public void abririnsta(View view){
        Intent intentFace = openInstagram(ContactoActivity.this);
        startActivity(intentFace);
    }

    public static Intent openInstagram(Context context) {

        try {
            context.getPackageManager()
                    .getPackageInfo("com.instagram.android", 0);
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("instagram://user?screen_name=sptsptsptsptspt"));
        } catch (Exception e){

            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://instagram.com/sptsptsptsptspt"));
        }


    }
}
