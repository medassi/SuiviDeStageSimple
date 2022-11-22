package com.example.suividestage.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.suividestage.daos.DelegateAsyncTask;
import com.example.suividestage.R;
import com.example.suividestage.beans.Etudiant;
import com.example.suividestage.daos.DaoEtudiant;

public class MainActivity extends AppCompatActivity {
    private ArrayAdapter<Etudiant> etudiantArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etudiantArrayAdapter = new ArrayAdapter<Etudiant>(this, android.R.layout.simple_list_item_1,
                DaoEtudiant.getInstance().getLocalEtudiants());
        ActivityResultLauncher<Intent> launcherEtudiantActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                activityResult -> {
                    if (activityResult.getResultCode() == 1) {
                        DaoEtudiant.getInstance().getEtudiants(new DelegateAsyncTask() {
                            @Override
                            public void whenWSConnexionIsTerminated(String result) {
                                etudiantArrayAdapter.notifyDataSetChanged();
                            }
                        });
                    } else if (activityResult.getResultCode() == 0) {
                        Toast.makeText(this, "Les changements ont été annulés", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        ((ListView) findViewById(R.id.lv))
                .setOnItemClickListener((adapterView, view, i, l) -> {
                    Intent intent = new Intent(this, EtudiantActivity.class);
                    intent.putExtra("index", i);
                    launcherEtudiantActivity.launch(intent);
                });
        ((ListView) findViewById(R.id.lv))
                .setOnItemLongClickListener((adapterView, view, i, l) -> {
                    Etudiant e = (Etudiant) adapterView.getAdapter().getItem(i);
                    DaoEtudiant.getInstance().delEtudiant(e, new DelegateAsyncTask() {
                        @Override
                        public void whenWSConnexionIsTerminated(String result) {
                            if (result.equals("1")) {
                                Toast.makeText(MainActivity.this, "Etudiant supprimé", Toast.LENGTH_SHORT).show();
                                etudiantArrayAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                    return true;
                });
        ((ListView) findViewById(R.id.lv)).setAdapter(etudiantArrayAdapter);
        findViewById(R.id.buttonAjouter).setOnClickListener(v -> {
            Intent intent = new Intent(this, EtudiantActivity.class);
            launcherEtudiantActivity.launch(intent);
        });
        findViewById(R.id.buttonCarte).setOnClickListener(v -> {
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
        });
        DaoEtudiant.getInstance().getEtudiants(new DelegateAsyncTask() {
            @Override
            public void whenWSConnexionIsTerminated(String result) {
                etudiantArrayAdapter.notifyDataSetChanged();
            }
        });
    }
}