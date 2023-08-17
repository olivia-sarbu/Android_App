package com.example.myapplication;

public class ExpenseModel {

    private String categorie, id, suma, tip, data, nota;

    public ExpenseModel(String categorie, String id, String suma, String tip, String data, String nota) {
        this.categorie = categorie;
        this.id = id;
        this.suma = suma;
        this.tip = tip;
        this.data = data;
        this.nota = nota;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSuma() {
        return suma;
    }

    public void setSuma(String suma) {
        this.suma = suma;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }
}
