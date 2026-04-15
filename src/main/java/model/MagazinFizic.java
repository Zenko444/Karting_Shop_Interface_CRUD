package model;

import jakarta.persistence.*;

@Entity
public class MagazinFizic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String denumire; // NOU
    private String tara;     // NOU
    private String oras;
    private String adresa;
    private String telefon;
    private String program;

    // Legătura cu un produs (ex: Produsul Vedetă al magazinului)
    @ManyToOne
    private Produs produsVedeta; // NOU

    public MagazinFizic() {}

    public MagazinFizic(String denumire, String tara, String oras, String adresa, String telefon, String program, Produs produsVedeta) {
        this.denumire = denumire;
        this.tara = tara;
        this.oras = oras;
        this.adresa = adresa;
        this.telefon = telefon;
        this.program = program;
        this.produsVedeta = produsVedeta;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public String getDenumire() { return denumire; }
    public void setDenumire(String denumire) { this.denumire = denumire; }
    public String getTara() { return tara; }
    public void setTara(String tara) { this.tara = tara; }
    public String getOras() { return oras; }
    public void setOras(String oras) { this.oras = oras; }
    public String getAdresa() { return adresa; }
    public void setAdresa(String adresa) { this.adresa = adresa; }
    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }
    public String getProgram() { return program; }
    public void setProgram(String program) { this.program = program; }
    public Produs getProdusVedeta() { return produsVedeta; }
    public void setProdusVedeta(Produs produsVedeta) { this.produsVedeta = produsVedeta; }
}