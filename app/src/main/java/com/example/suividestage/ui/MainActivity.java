package com.example.suividestage.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.suividestage.R;
import com.example.suividestage.beans.Etudiant;
import com.example.suividestage.net.WSConnexionHTTPS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createAndLaunchASWSGetEtudiants();
        ((ListView) findViewById(R.id.lv)).setOnItemLongClickListener((adapterView, view, i, l) -> {
            Etudiant eSel = (Etudiant) adapterView.getItemAtPosition(i);
            onItemLongClickLV(eSel);
            return true;
        });
    }

    private void createAndLaunchASWSGetEtudiants() {
        WSConnexionHTTPS ws = new WSConnexionHTTPS() {
            @Override
            protected void onPostExecute(String s) {
                traiterRetourGetEtudiants(s);
            }
        };
        ws.execute("uc=getEtudiants");
    }

    private void traiterRetourGetEtudiants(String s) {
        Log.d("TRAITERRETETU", s);
        List<Etudiant> etudiantList = new ArrayList<>();
        try {
            JSONArray jsona = new JSONArray(s);
            for (int i = 0; i < jsona.length(); i++) {
                JSONObject jsono = jsona.getJSONObject(i);
                int id = jsono.getInt("idEtu");
                String nomEtu = jsono.getString("nomEtu");
                String prenomEtu = jsono.getString("prenomEtu");
                String nomEnt = jsono.getString("nomEnt");
                Double lat = jsono.getDouble("latEnt");
                Double lng = jsono.getDouble("lngEnt");
                Etudiant e = new Etudiant(id, nomEtu, prenomEtu, nomEnt, lat, lng);
                etudiantList.add(e);
            }
            ((ListView) findViewById(R.id.lv)).setAdapter(new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, etudiantList));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void onItemLongClickLV(Etudiant eSel) {
        createAndLaunchASWSDelEtudiant(eSel);
    }

    private void createAndLaunchASWSDelEtudiant(Etudiant eSel) {
        WSConnexionHTTPS ws = new WSConnexionHTTPS() {
            @Override
            protected void onPostExecute(String s) {
                traiterRetourDelEtudiant(s);
            }
        };
        ws.execute("uc=delEtudiant&idEtu=" + eSel.getIdEtu());
    }

    private void traiterRetourDelEtudiant(String s) {
        if (s.equals("1")) {
            Toast.makeText(this, "Suppression etudiant OK", Toast.LENGTH_SHORT).show();
            createAndLaunchASWSGetEtudiants();
        } else {
            Toast.makeText(this, "Problem Suppression etudiant ", Toast.LENGTH_SHORT).show();
        }
    }
}