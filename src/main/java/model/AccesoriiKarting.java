package model;

import jakarta.persistence.Entity;

@Entity
public class AccesoriiKarting extends Produs {

    private String tip;      // "Manusi" sau "Casca"
    private String marime;   // S, M, L, XL
    private String material; // "Carbon", "Textil", "Piele"
    private int stoc;        // Cantitate disponibilă

    public AccesoriiKarting() {
        // Constructor gol obligatoriu pentru JPA
    }

    // Constructorul adaptat la App.java (6 parametri)
    public AccesoriiKarting(String denumire, Double pret, Producator producator, String tip, String marime, String material) {
        super(denumire, pret, producator);
        this.tip = tip;
        this.marime = marime;
        this.material = material;
        this.stoc = 0; // Setăm stocul default la 0 pentru că App.java nu îl furnizează
    }

    // Constructor complet (opțional, dacă vrei să setezi și stocul manual în viitor)
    public AccesoriiKarting(String denumire, Double pret, Producator producator, String tip, String marime, String material, int stoc) {
        super(denumire, pret, producator);
        this.tip = tip;
        this.marime = marime;
        this.material = material;
        this.stoc = stoc;
    }

    // Getters & Setters
    public String getTip() { return tip; }
    public void setTip(String tip) { this.tip = tip; }

    public String getMarime() { return marime; }
    public void setMarime(String marime) { this.marime = marime; }

    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }

    public int getStoc() { return stoc; }
    public void setStoc(int stoc) { this.stoc = stoc; }
}