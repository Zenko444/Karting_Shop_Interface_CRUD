package model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;

@Entity
public class Competitie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nume;
    private String circuit;
    private LocalDate dataStart;
    private int numarParticipanti;
    private String tipKart; // NOU: ex "Rotax Junior", "KZ2"

    public Competitie() {}

    public Competitie(String nume, String circuit, LocalDate dataStart, int numarParticipanti, String tipKart) {
        this.nume = nume;
        this.circuit = circuit;
        this.dataStart = dataStart;
        this.numarParticipanti = numarParticipanti;
        this.tipKart = tipKart;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }
    public String getCircuit() { return circuit; }
    public void setCircuit(String circuit) { this.circuit = circuit; }
    public LocalDate getDataStart() { return dataStart; }
    public void setDataStart(LocalDate dataStart) { this.dataStart = dataStart; }
    public int getNumarParticipanti() { return numarParticipanti; }
    public void setNumarParticipanti(int numarParticipanti) { this.numarParticipanti = numarParticipanti; }
    public String getTipKart() { return tipKart; }
    public void setTipKart(String tipKart) { this.tipKart = tipKart; }
}