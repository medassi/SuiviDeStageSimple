package com.example.suividestage;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;

public class EtudiantActivity extends AppCompatActivity {
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView mapEtu ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etudiant);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        mapEtu = (MapView) findViewById(R.id.mapEtu);
        mapEtu.setTileSource(TileSourceFactory.MAPNIK);
        String[] tabPerm = new String[1];
        tabPerm[0] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        requestPermissionsIfNecessary(tabPerm);
        int index = getIntent().getIntExtra("index", -1);
        if (index != -1) { //Alors on veut modifier un etudiant
            Etudiant e = Model.getInstance().getArrayAdapterEtu().getItem(index);
            ((EditText) (findViewById(R.id.etNom))).setText(e.getNomEtu());
            ((EditText) (findViewById(R.id.etPrenom))).setText(e.getPrenomEtu());
            ((EditText) (findViewById(R.id.etNomEntreprise))).setText(e.getNomEnt());
            ((EditText) (findViewById(R.id.etLat))).setText(e.getLatEnt() + "");
            ((EditText) (findViewById(R.id.etLng))).setText(e.getLngEnt() + "");
            mapEtu.getOverlays().clear();
            Marker m = new Marker(mapEtu) ;
            m.setTitle(e.toString());
            m.setPosition(new GeoPoint(e.getLatEnt(),e.getLngEnt()));
            mapEtu.getOverlays().add(m) ;
            IMapController mapController = mapEtu.getController();
            mapController.setZoom(9.5);
            mapController.setCenter(m.getPosition());
        }else{
            mapEtu.setVisibility(View.INVISIBLE);
        }
        findViewById(R.id.buttonValider).setOnClickListener(v -> {
            if (index != -1) {
                Etudiant e = Model.getInstance().getArrayAdapterEtu().getItem(index);
                e.setNomEtu(((EditText) (findViewById(R.id.etNom))).getText().toString());
                e.setPrenomEtu(((EditText) (findViewById(R.id.etPrenom))).getText().toString());
                e.setNomEnt(((EditText) (findViewById(R.id.etNomEntreprise))).getText().toString());
                e.setLatEnt(Double.parseDouble(((EditText) (findViewById(R.id.etLat))).getText().toString()));
                e.setLngEnt(Double.parseDouble(((EditText) (findViewById(R.id.etLng))).getText().toString()));
                setResult(1);
                Model.getInstance().updateEtudiant(e);
            } else {
                Etudiant newEtudiant
                        = new Etudiant(0, ((EditText) (findViewById(R.id.etNom))).getText().toString(),
                        ((EditText) (findViewById(R.id.etPrenom))).getText().toString(),
                        ((EditText) (findViewById(R.id.etNomEntreprise))).getText().toString(),
                        Double.parseDouble(((EditText) (findViewById(R.id.etLat))).getText().toString()),
                        Double.parseDouble(((EditText) (findViewById(R.id.etLng))).getText().toString())
                );
                Model.getInstance().addEtudiant(newEtudiant);
                setResult(1);
            }
            finish();
        });
        findViewById(R.id.buttonAnnuler).setOnClickListener(v -> {
            setResult(0);
            finish();
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
}