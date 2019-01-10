package com.example.grupo6.appgrup6;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private FABToolbarLayout morph;

    private ViewPager mViewPager;
    private NotificationManager notificationManager;
    static final String CANAL_ID = "mi_canal";
    static final int NOTIFICACION_ID = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Sistema de notificaciones parcialmente implementado.
        int a = 20;

        if( a < 30 ){
            notificacion(null);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        morph = (FABToolbarLayout) findViewById(R.id.fabtoolbar);

        View uno, dos, tres, cuatro;

        uno = findViewById(R.id.uno);
        dos = findViewById(R.id.dos);
        cuatro = findViewById(R.id.cuatro);
        tres = findViewById(R.id.tres);

        fab.setOnClickListener(this);
        uno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lanzarSensores(view);
                morph.hide();
                }
                });
        dos.setOnClickListener(this);
        tres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lanzarMedicinas(view);
                morph.hide();
            }
        });
        cuatro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lanzarMapa(view);
                morph.hide();
            }
        });
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                lanzarSensores(view);
            }
        });*/

        lanzarServicioAcelerometro(null);
        lanzarServicioGas(null);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            morph.show();
        }

        morph.hide();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.menu_usuario) {
            Intent intent = new Intent(this, UsuarioActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    TabPeso tab1 = new TabPeso();
                    return tab1;
                case 1:
                    TabAltura tab2 = new TabAltura();
                    return tab2;
                case 2:
                    TabImc tab3 = new TabImc();
                    return tab3;
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Peso";
                case 1:
                    return "Altura";
                case 2:
                    return "Imc";
            }
            return null;
        }
    }

    public void notificacion(View view){
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    CANAL_ID, "Mis Notificaciones",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Descripcion del canal");
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificacion =
                new NotificationCompat.Builder(MainActivity.this, CANAL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("ERROR")
                        .setContentText("ERROR AL PESARSE");
        PendingIntent intencionPendiente = PendingIntent.getActivity(
                this, 0, new Intent(this, TabPeso.class), 0);
        notificacion.setContentIntent(intencionPendiente);
        notificationManager.notify(NOTIFICACION_ID, notificacion.build());

    }

    public void lanzarSensores(View view){
        Intent intent = new Intent(this, SensorsActivity.class);
        startActivity(intent);

    }

    public void lanzarMedicinas(View view){
        Intent intent = new Intent(this, MedicinasActivity.class);
        startActivity(intent);

    }

    public void lanzarMapa(View view){
        Intent intent = new Intent(this, Mapa.class);
        startActivity(intent);

    }

    void lanzarServicioGas(View view){
        Intent intent = new Intent(this, ServicioGas.class);
        startService(intent);
    }

    void lanzarServicioAcelerometro(View view){
        Intent intent = new Intent(this, ServicioAcelerometro.class);
        startService(intent);
    }

}
