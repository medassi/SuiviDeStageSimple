package com.example.suividestage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private ArrayAdapter<Etudiant> adapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Model.init(this); ;
        adapter = Model.getInstance().getArrayAdapterEtu();
        ActivityResultLauncher<Intent> launcherEtudiantActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                activityResult -> {
                    if (activityResult.getResultCode() == 1) {
                       Model.getInstance().refreshListEtudiants();
                    } else if (activityResult.getResultCode() == 0) {
                        Toast.makeText(this, "Les changements ont été annulés", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        ((ListView) findViewById(R.id.lv))
                .setOnItemClickListener((adapterView, view, i, l) -> {
                    Intent intent = new Intent(this,EtudiantActivity.class) ;
                    intent.putExtra("index",i) ;
                    launcherEtudiantActivity.launch(intent);
                });
        ((ListView) findViewById(R.id.lv))
                .setOnItemLongClickListener((adapterView, view, i, l) -> {
                    Etudiant e = (Etudiant) adapterView.getAdapter().getItem(i);
                    Model.getInstance().delEtudiant(e); ;
                    return true;
                });

        ((ListView) findViewById(R.id.lv)).setAdapter(adapter);
        findViewById(R.id.buttonAjouter).setOnClickListener(v -> {
            Intent intent = new Intent(this, EtudiantActivity.class);
            launcherEtudiantActivity.launch(intent);
        });
        findViewById(R.id.buttonCarte).setOnClickListener(v->{
            Intent intent = new Intent(this,MapActivity.class) ;
            startActivity(intent);
        });

    }


}