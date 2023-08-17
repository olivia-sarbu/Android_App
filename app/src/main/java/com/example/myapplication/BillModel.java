package com.example.myapplication;

public class BillModel {
    private String id, categorie_factura, suma_plata, termen_limita, data_notificare;

    public BillModel(String id, String categorie_factura, String suma_plata, String termen_limita, String data_notificare) {
        this.id = id;
        this.categorie_factura = categorie_factura;
        this.suma_plata = suma_plata;
        this.termen_limita = termen_limita;
        this.data_notificare = data_notificare;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategorie_factura() {
        return categorie_factura;
    }

    public void setCategorie_factura(String categorie_factura) {
        this.categorie_factura = categorie_factura;
    }

    public String getSuma_plata() {
        return suma_plata;
    }

    public void setSuma_plata(String suma_plata) {
        this.suma_plata = suma_plata;
    }

    public String getTermen_limita() {
        return termen_limita;
    }

    public void setTermen_limita(String termen_limita) {
        this.termen_limita = termen_limita;
    }

    public String getData_notificare() {
        return data_notificare;
    }

    public void setData_notificare(String data_notificare) {
        this.data_notificare = data_notificare;
    }
}
