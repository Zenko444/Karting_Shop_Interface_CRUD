package model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Vanzare {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDate data;
    private Double total = 0.0;

    @ManyToOne
    private Client client;

    @OneToMany(mappedBy = "vanzare", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ArticolVanzare> articole = new ArrayList<>();

    public Vanzare() {
        this.data = LocalDate.now();
    }

    public Vanzare(Client client) {
        this.client = client;
        this.data = LocalDate.now();
    }

    // --- METODA CARE LIPSEA ---
    public void calculeazaTotal() {
        this.total = 0.0;
        if (articole != null) {
            for (ArticolVanzare articol : articole) {
                // Ne asigurăm că valoarea articolului este corectă
                if (articol.getValoare() != null) {
                    this.total += articol.getValoare();
                }
            }
        }
    }

    public void adaugaProdus(Produs produs, int cantitate) {
        ArticolVanzare articol = new ArticolVanzare(produs, cantitate);
        articol.setVanzare(this);
        this.articole.add(articol);

        // Recalculăm totalul automat
        calculeazaTotal();
    }

    // Getters & Setters
    public Long getId() { return id; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public List<ArticolVanzare> getArticole() { return articole; }
    public void setArticole(List<ArticolVanzare> articole) { this.articole = articole; }
}