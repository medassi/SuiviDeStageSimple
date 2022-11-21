package com.example.suividestage;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;

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

public class MapActivity extends AppCompatActivity {
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        String[] tabPerm = new String[1];
        tabPerm[0] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        requestPermissionsIfNecessary(tabPerm);
        GeoPoint p = positionnerEtudiants();
        positionnerSurCentre(p) ;
        ( (Button) findViewById(R.id.buttonRetourMap) ).setOnClickListener(view -> {
            finish();
        });
    }

    private void positionnerSurCentre(GeoPoint p) {
        IMapController mapController = map.getController();
        mapController.setZoom(9.5);
        mapController.setCenter(p);
    }
    private GeoPoint positionnerEtudiants() {
        map.getOverlays().clear();
        double moyLat=0 ;
        double moyLng=0 ;
        for(Etudiant e : Model.getInstance().getEtudiants()){
            Marker m = new Marker(map) ;
            moyLat+=e.getLatEnt() ;
            moyLng+=e.getLngEnt() ;
            m.setPosition(new GeoPoint(e.getLatEnt(), e.getLngEnt()));
            m.setTitle(e.toString());
            map.getOverlays().add(m) ;
        }
        return new GeoPoint(moyLat/Model.getInstance().getEtudiants().size(),moyLng/Model.getInstance().getEtudiants().size()) ;
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
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