package org.comenzi.model;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Competitie;

import java.time.LocalDate;

@Route(value = "competitie-detalii", layout = MainLayout.class)
public class FormCompetitiiView extends VerticalLayout implements HasUrlParameter<Long> {

    private Long competitieId;
    private TextField numeField = new TextField("Nume Eveniment");
    private TextField circuitField = new TextField("Circuit");
    private TextField dataField = new TextField("Data (YYYY-MM-DD)");
    private TextField tipKartField = new TextField("Tip Kart / Clasă");
    private TextField participantiField = new TextField("Nr. Participanți");

    private Button btnSave = new Button("Salvează");
    private Button btnDelete = new Button("Șterge");

    public FormCompetitiiView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER); // Centrăm orizontal
        setJustifyContentMode(JustifyContentMode.CENTER); // Centrăm vertical
        getStyle().set("background", "transparent"); // Lăsăm să se vadă imaginea de fundal

        // --- 1. CONFIGURARE CONTAINER (CHENARUL) ---
        VerticalLayout containerFormular = new VerticalLayout();
        containerFormular.setMaxWidth("600px"); // Lățime maximă pentru aspect plăcut
        containerFormular.setWidthFull();

        // Stilizare Chenar (Negru Transparent)
        containerFormular.getStyle().set("background-color", "rgba(0, 0, 0, 0.75)");
        containerFormular.getStyle().set("border-radius", "15px"); // Colțuri rotunjite
        containerFormular.getStyle().set("padding", "40px");       // Spațiu interior
        containerFormular.getStyle().set("box-shadow", "0 0 20px rgba(0,0,0,0.5)"); // Umbră
        containerFormular.setAlignItems(Alignment.CENTER);

        // --- 2. TITLU ---
        H2 titlu = new H2("Editare Competiție");
        titlu.getStyle().set("color", "white"); // Titlu Alb
        titlu.getStyle().set("margin-top", "0");

        // --- 3. FORMULAR ---
        FormLayout form = new FormLayout();
        form.setWidthFull();
        form.add(numeField, circuitField, dataField, tipKartField, participantiField);

        // APLICĂM STILUL DE TEXT ALB PE CÂMPURI
        styleField(numeField);
        styleField(circuitField);
        styleField(dataField);
        styleField(tipKartField);
        styleField(participantiField);

        // --- 4. BUTOANE ---
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSave.addClickListener(event -> salveazaModificarile());

        btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnDelete.addClickListener(event -> sterge());

        RouterLink backLink = new RouterLink("Înapoi", NavigableGridCompetitiiView.class);
        backLink.getStyle().set("color", "#cccccc"); // Link gri deschis
        backLink.getStyle().set("font-weight", "bold");

        HorizontalLayout actions = new HorizontalLayout(btnSave, btnDelete, backLink);
        actions.setAlignItems(Alignment.CENTER);
        actions.getStyle().set("margin-top", "20px");

        // Adăugăm totul în container
        containerFormular.add(titlu, form, actions);

        // Adăugăm containerul în pagină
        add(containerFormular);
    }

    // --- METODĂ AJUTĂTOARE PENTRU CULOARE ALBĂ ---
    private void styleField(Component field) {
        // Culoarea etichetei (Label)
        field.getElement().getStyle().set("--vaadin-input-field-label-color", "#ffffff");
        // Culoarea textului introdus
        field.getElement().getStyle().set("--vaadin-input-field-value-color", "#ffffff");
        // Culoarea fundalului câmpului (opțional, puțin mai închis)
        field.getElement().getStyle().set("--vaadin-text-field-background", "rgba(255, 255, 255, 0.1)");
    }

    @Override
    public void setParameter(BeforeEvent event, Long id) {
        this.competitieId = id;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("KartingJPA");
        EntityManager em = emf.createEntityManager();
        try {
            Competitie c = em.find(Competitie.class, id);
            if (c != null) {
                numeField.setValue(c.getNume());
                circuitField.setValue(c.getCircuit());
                dataField.setValue(c.getDataStart() != null ? c.getDataStart().toString() : "");
                tipKartField.setValue(c.getTipKart());
                participantiField.setValue(String.valueOf(c.getNumarParticipanti()));
            }
        } finally {
            em.close();
            emf.close();
        }
    }

    private void salveazaModificarile() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("KartingJPA");
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Competitie c = em.find(Competitie.class, this.competitieId);
            if (c != null) {
                c.setNume(numeField.getValue());
                c.setCircuit(circuitField.getValue());
                c.setTipKart(tipKartField.getValue());
                try {
                    c.setNumarParticipanti(Integer.parseInt(participantiField.getValue()));
                    if (!dataField.getValue().isEmpty()) {
                        c.setDataStart(LocalDate.parse(dataField.getValue()));
                    }
                    em.merge(c);
                    em.getTransaction().commit();
                    Notification.show("Salvat cu succes!");
                } catch (Exception e) {
                    Notification.show("Date invalide (Verifică numărul sau formatul datei YYYY-MM-DD)");
                }
            }
        } finally {
            em.close();
            emf.close();
        }
    }

    private void sterge() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("KartingJPA");
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Competitie c = em.find(Competitie.class, competitieId);
            if (c != null) {
                em.remove(c);
                em.getTransaction().commit();
                Notification.show("Competiție ștearsă!");
                getUI().ifPresent(ui -> ui.navigate(NavigableGridCompetitiiView.class));
            }
        } catch (Exception e) {
            if(em.getTransaction().isActive()) em.getTransaction().rollback();
            Notification.show("Eroare la ștergere.");
        } finally {
            em.close();
            emf.close();
        }
    }
}