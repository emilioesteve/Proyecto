package com.example.grupo6.appgrup6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class SendMessage extends AppCompatActivity {

    TextView resp;
    String respu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        resp = findViewById(R.id.asdf);
        Bundle extras = getIntent().getExtras();
        respu = extras.getString(EXTRA_MESSAGE);
        resp.setText(respu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compartir, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.compartir) {
            setShareIntent(null);
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void setShareIntent(Intent shareIntent) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, respu);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
}
