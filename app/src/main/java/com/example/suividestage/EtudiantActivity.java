package com.example.suividestage;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EtudiantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etudiant);
        int index = getIntent().getIntExtra("index", -1);
        if (index != -1) { //Alors on veut modifier un etudiant
            Etudiant e = Model.getInstance().getArrayAdapterEtu().getItem(index);
            ((EditText) (findViewById(R.id.etNom))).setText(e.getNomEtu());
            ((EditText) (findViewById(R.id.etPrenom))).setText(e.getPrenomEtu());
            ((EditText) (findViewById(R.id.etNomEntreprise))).setText(e.getNomEnt());
            ((EditText) (findViewById(R.id.etLat))).setText(e.getLatEnt() + "");
            ((EditText) (findViewById(R.id.etLng))).setText(e.getLngEnt() + "");
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
                Log.d("MODIFICATION",e.toString()) ;
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
}