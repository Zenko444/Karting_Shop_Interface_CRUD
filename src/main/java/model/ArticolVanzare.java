package model;

import jakarta.persistence.*;

@Entity
public class ArticolVanzare {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    protected int cantitate;
    protected Double valoare; // Prețul total pentru acest rând (cantitate * preț bucată)

    @ManyToOne
    protected Produs produs;

    @ManyToOne
    protected Vanzare vanzare;

    public ArticolVanzare() {
    }

    public ArticolVanzare(Produs produs, int cantitate) {
        this.produs = produs;
        this.cantitate = cantitate;
        // Calculăm valoarea automat: prețul produsului * câte bucăți cumpără
        if (produs != null) {
            this.valoare = produs.getPret() * cantitate;
        } else {
            this.valoare = 0.0;
        }
    }

    // Getters & Setters
    public Long getId() { return id; }

    public int getCantitate() { return cantitate; }
    public void setCantitate(int cantitate) { this.cantitate = cantitate; }

    public Double getValoare() { return valoare; }
    public void setValoare(Double valoare) { this.valoare = valoare; }

    public Produs getProdus() { return produs; }
    public void setProdus(Produs produs) { this.produs = produs; }

    public Vanzare getVanzare() { return vanzare; }
    public void setVanzare(Vanzare vanzare) { this.vanzare = vanzare; }
}