package com.example.suividestage;

public class Etudiant {
    private int idEtu;
    private String nomEtu;
    private String prenomEtu;
    private String nomEnt;
    private double latEnt;
    private double lngEnt;

    public Etudiant(){}

    public Etudiant(int id ,String nom, String prenom, String nomEntreprise, double lat, double lng) {
        this.idEtu = id ;
        this.nomEtu = nom;
        this.prenomEtu = prenom;
        this.nomEnt = nomEntreprise;
        this.latEnt = lat;
        this.lngEnt = lng;
    }

    public String getNomEtu() {
        return nomEtu;
    }

    public String getPrenomEtu() {
        return prenomEtu;
    }

    public String getNomEnt() {
        return nomEnt;
    }

    public double getLatEnt() {
        return latEnt;
    }

    public double getLngEnt() {
        return lngEnt;
    }

    public void setNomEtu(String nomEtu) {
        this.nomEtu = nomEtu;
    }

    public void setPrenomEtu(String prenomEtu) {
        this.prenomEtu = prenomEtu;
    }

    public void setNomEnt(String nomEnt) {
        this.nomEnt = nomEnt;
    }

    public void setLatEnt(double latEnt) {
        this.latEnt = latEnt;
    }

    public void setLngEnt(double lngEnt) {
        this.lngEnt = lngEnt;
    }

    public int getIdEtu() {
        return idEtu;
    }


    @Override
    public String toString() {
        return nomEtu + " " + prenomEtu + " ("+ nomEnt +")";
    }
}
