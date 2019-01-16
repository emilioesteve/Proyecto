package com.example.grupo6.appgrup6;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Mapa extends FragmentActivity
        implements OnMapReadyCallback {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        // Obtenemos el mapa de forma asíncrona (notificará cuando esté listo)
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        GoogleMap mapa = googleMap;
        //LatLng UPV = new LatLng(39.481106, -0.340987); //Nos ubicamos en la UPV
        //mapa.addMarker(new MarkerOptions().position(UPV).title("Marker UPV"));
        LatLng farm3 = new LatLng(38.99352202641748,-0.1637810468673706);
        LatLng farm4 = new LatLng(38.9921711546645, -0.16087889671325684);
        mapa.addMarker(new MarkerOptions().position(farm3).title("Farmacia Universitat"));
        mapa.addMarker(new MarkerOptions().position(farm4).title("Universitat Castelló"));
        LatLng farm1 = new LatLng(38.99531481022166,-0.16271889209747314);
        LatLng farm2 = new LatLng(39.00580793836007, -0.16408681869506836);
        mapa.addMarker(new MarkerOptions().position(farm1).title("Farmacia Rosa dels Vents"));
        mapa.addMarker(new MarkerOptions().position(farm2).title("Farmacia Playa"));
        float zoomLevel = 16.0f; //This goes up to 21
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(farm1, zoomLevel));
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
}
