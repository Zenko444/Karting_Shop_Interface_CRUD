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
import model.MagazinFizic;

@Route(value = "magazin-detalii", layout = MainLayout.class)
public class FormMagazinFizicView extends VerticalLayout implements HasUrlParameter<Long> {

    private Long magazinId;
    private TextField denumireField = new TextField("Denumire Magazin");
    private TextField taraField = new TextField("Țara");
    private TextField orasField = new TextField("Oraș");
    private TextField adresaField = new TextField("Adresă");
    private TextField telefonField = new TextField("Telefon");
    private TextField programField = new TextField("Program");

    private Button btnSave = new Button("Salvează");
    private Button btnDelete = new Button("Șterge");

    public FormMagazinFizicView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        getStyle().set("background", "transparent");

        // --- Container Chenar ---
        VerticalLayout container = new VerticalLayout();
        container.setMaxWidth("600px");
        container.getStyle().set("background-color", "rgba(0, 0, 0, 0.75)");
        container.getStyle().set("border-radius", "15px");
        container.getStyle().set("padding", "40px");
        container.setAlignItems(Alignment.CENTER);

        H2 titlu = new H2("Editare Magazin");
        titlu.getStyle().set("color", "white");

        FormLayout form = new FormLayout();
        form.setWidthFull();
        form.add(denumireField, taraField, orasField, adresaField, telefonField, programField);

        // Stilizare Albă
        styleField(denumireField); styleField(taraField); styleField(orasField);
        styleField(adresaField); styleField(telefonField); styleField(programField);

        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSave.addClickListener(e -> salveaza());

        btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnDelete.addClickListener(e -> sterge());

        RouterLink backLink = new RouterLink("Înapoi", NavigableGridMagazineView.class);
        backLink.getStyle().set("color", "#cccccc");
        backLink.getStyle().set("font-weight", "bold");

        HorizontalLayout actions = new HorizontalLayout(btnSave, btnDelete, backLink);
        actions.setAlignItems(Alignment.CENTER);
        actions.getStyle().set("margin-top", "20px");

        container.add(titlu, form, actions);
        add(container);
    }

    private void styleField(Component field) {
        field.getElement().getStyle().set("--vaadin-input-field-label-color", "#ffffff");
        field.getElement().getStyle().set("--vaadin-input-field-value-color", "#ffffff");
        field.getElement().getStyle().set("--vaadin-text-field-background", "rgba(255, 255, 255, 0.1)");
    }

    @Override
    public void setParameter(BeforeEvent event, Long id) {
        this.magazinId = id;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("KartingJPA");
        EntityManager em = emf.createEntityManager();
        try {
            MagazinFizic m = em.find(MagazinFizic.class, id);
            if (m != null) {
                denumireField.setValue(m.getDenumire() != null ? m.getDenumire() : "");
                taraField.setValue(m.getTara() != null ? m.getTara() : "");
                orasField.setValue(m.getOras());
                adresaField.setValue(m.getAdresa());
                telefonField.setValue(m.getTelefon());
                programField.setValue(m.getProgram());
            }
        } finally {
            em.close();
            emf.close();
        }
    }

    private void salveaza() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("KartingJPA");
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            MagazinFizic m = em.find(MagazinFizic.class, magazinId);
            if (m != null) {
                m.setDenumire(denumireField.getValue());
                m.setTara(taraField.getValue());
                m.setOras(orasField.getValue());
                m.setAdresa(adresaField.getValue());
                m.setTelefon(telefonField.getValue());
                m.setProgram(programField.getValue());

                em.merge(m);
                em.getTransaction().commit();
                Notification.show("Magazin actualizat!");
            }
        } catch (Exception e) {
            em.getTransaction().rollback();
            Notification.show("Eroare: " + e.getMessage());
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
            MagazinFizic m = em.find(MagazinFizic.class, magazinId);
            if (m != null) {
                em.remove(m);
                em.getTransaction().commit();
                Notification.show("Magazin șters!");
                getUI().ifPresent(ui -> ui.navigate(NavigableGridMagazineView.class));
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