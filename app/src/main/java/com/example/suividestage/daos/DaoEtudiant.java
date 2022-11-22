package com.example.suividestage.daos;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.suividestage.beans.Etudiant;
import com.example.suividestage.net.WSConnexionHTTPS;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DaoEtudiant {
    private static DaoEtudiant instance = null;
    private final List<Etudiant> etudiants;
    private final ArrayAdapter<Etudiant> arrayAdapterEtu;
    private final Context context;
    private final ObjectMapper om = new ObjectMapper();

    private DaoEtudiant(Context context) {
        this.context = context;
        etudiants = new ArrayList<>();
        arrayAdapterEtu = new ArrayAdapter(context, android.R.layout.simple_list_item_1, etudiants);
        refreshListEtudiants();
    }

    public List<Etudiant> getEtudiants() {
        return etudiants;
    }

    public ArrayAdapter<Etudiant> getArrayAdapterEtu() {
        return arrayAdapterEtu;
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new DaoEtudiant(context);
        }
    }

    public static DaoEtudiant getInstance() {
        return instance;
    }

    public void refreshListEtudiants() {
        String url = "uc=getEtudiants";
        WSConnexionHTTPS wsConnexionHTTPS = new WSConnexionHTTPS() {
            @Override
            protected void onPostExecute(String s) {
                traiterRetourGetEtudiants(s);

            }
        };

        wsConnexionHTTPS.execute(url);
    }

    private void traiterRetourGetEtudiants(String s) {
        try {
            etudiants.clear();
            Arrays.asList(om.readValue(s, Etudiant[].class)).forEach(etudiant -> etudiants.add(etudiant));
            arrayAdapterEtu.notifyDataSetChanged();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    public void delEtudiant(Etudiant e) {
        String url = "uc=delEtudiant&idEtu=" + e.getIdEtu();
        WSConnexionHTTPS wsConnexionHTTPS = new WSConnexionHTTPS() {
            @Override
            protected void onPostExecute(String s) {
                traiterRetourDelEtudiant(s);
            }
        };
        wsConnexionHTTPS.execute(url);
    }

    private void traiterRetourDelEtudiant(String s) {
        if (s.equals("1")) {
            refreshListEtudiants();
            Toast.makeText(context, "L'étudiant a été supprimé avec succès", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "L'étudiant n'a pas été supprimé.", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateEtudiant(Etudiant e) {
        String url = "uc=updateEtudiant" +
                "&nomEtu=" + e.getNomEtu() +
                "&prenomEtu=" + e.getPrenomEtu() +
                "&nomEnt=" + e.getNomEnt() +
                "&latEnt=" + e.getLatEnt() +
                "&lngEnt=" + e.getLngEnt() +
                "&idEtu=" + e.getIdEtu();
        WSConnexionHTTPS wsConnexionHTTPS = (WSConnexionHTTPS) new WSConnexionHTTPS() {
            @Override
            protected void onPostExecute(String s) {
                traiterRetourUpdateEtudiant(s);
            }
        }.execute(url);
    }

    private void traiterRetourUpdateEtudiant(String s) {
        if (s.equals("1")) {
            refreshListEtudiants();
            Toast.makeText(context, "L'étudiant a été modifié avec succès", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "L'étudiant n'a pas été modifié.", Toast.LENGTH_SHORT).show();
        }
    }

    public void addEtudiant(Etudiant newEtudiant) {
        String url = "uc=addEtudiant" +
                "&nomEtu=" + newEtudiant.getNomEtu() +
                "&prenomEtu=" + newEtudiant.getPrenomEtu() +
                "&nomEnt=" + newEtudiant.getNomEnt() +
                "&latEnt=" + newEtudiant.getLatEnt() +
                "&lngEnt=" + newEtudiant.getLngEnt();
        WSConnexionHTTPS wsConnexionHTTPS = new WSConnexionHTTPS() {
            @Override
            protected void onPostExecute(String s) {
                traiterRetourAddEtudiant(s);
            }
        };
        wsConnexionHTTPS.execute(url);
    }

    private void traiterRetourAddEtudiant(String s) {
        if (s.equals("1")) {
            refreshListEtudiants();
            Toast.makeText(context, "L'étudiant a été ajouté avec succès", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "L'étudiant n'a pas été ajouté.", Toast.LENGTH_SHORT).show();
        }
    }
}
