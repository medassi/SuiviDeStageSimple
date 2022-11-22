package com.example.suividestage.daos;

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
    private final ObjectMapper om = new ObjectMapper();

    private DaoEtudiant() {
        etudiants = new ArrayList<>();
    }

    public List<Etudiant> getLocalEtudiants() {
        return etudiants;
    }

    public static DaoEtudiant getInstance() {
        if (instance == null) {
            instance = new DaoEtudiant();
        }
        return instance;
    }

    public void getEtudiants(DelegateAsyncTask delegate) {
        String url = "uc=getEtudiants";
        WSConnexionHTTPS wsConnexionHTTPS = new WSConnexionHTTPS() {
            @Override
            protected void onPostExecute(String s) {
                traiterRetourGetEtudiants(s, delegate);
            }
        };
        wsConnexionHTTPS.execute(url);
    }

    private void traiterRetourGetEtudiants(String s, DelegateAsyncTask delegate) {
        try {
            etudiants.clear();
            Arrays.asList(om.readValue(s, Etudiant[].class)).forEach(etudiant -> etudiants.add(etudiant));
            delegate.whenWSConnexionIsTerminated(s);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    public void delEtudiant(Etudiant e, DelegateAsyncTask delegate) {
        String url = "uc=delEtudiant&idEtu=" + e.getIdEtu();
        WSConnexionHTTPS wsConnexionHTTPS = new WSConnexionHTTPS() {
            @Override
            protected void onPostExecute(String s) {
                traiterRetourDelEtudiant(s, delegate);
            }
        };
        wsConnexionHTTPS.execute(url);
    }

    private void traiterRetourDelEtudiant(String s, DelegateAsyncTask delegate) {
        getEtudiants(new DelegateAsyncTask() {
            @Override
            public void whenWSConnexionIsTerminated(String result) {
                delegate.whenWSConnexionIsTerminated(s);
            }
        });
        delegate.whenWSConnexionIsTerminated(s);
    }

    public void updateEtudiant(Etudiant e, DelegateAsyncTask delegate) {
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
                traiterRetourUpdateEtudiant(s, delegate);
            }
        }.execute(url);
    }

    private void traiterRetourUpdateEtudiant(String s, DelegateAsyncTask delegate) {
        getEtudiants(new DelegateAsyncTask() {
            @Override
            public void whenWSConnexionIsTerminated(String result) {
                delegate.whenWSConnexionIsTerminated(s);
            }
        });
        delegate.whenWSConnexionIsTerminated(s);
    }

    public void addEtudiant(Etudiant newEtudiant, DelegateAsyncTask delegate) {
        String url = "uc=addEtudiant" +
                "&nomEtu=" + newEtudiant.getNomEtu() +
                "&prenomEtu=" + newEtudiant.getPrenomEtu() +
                "&nomEnt=" + newEtudiant.getNomEnt() +
                "&latEnt=" + newEtudiant.getLatEnt() +
                "&lngEnt=" + newEtudiant.getLngEnt();
        WSConnexionHTTPS wsConnexionHTTPS = new WSConnexionHTTPS() {
            @Override
            protected void onPostExecute(String s) {
                traiterRetourAddEtudiant(s, delegate);
            }
        };
        wsConnexionHTTPS.execute(url);
    }

    private void traiterRetourAddEtudiant(String s, DelegateAsyncTask delegate) {
        getEtudiants(new DelegateAsyncTask() {
            @Override
            public void whenWSConnexionIsTerminated(String result) {
                delegate.whenWSConnexionIsTerminated(s);
            }
        });
        delegate.whenWSConnexionIsTerminated(s);
    }
}
