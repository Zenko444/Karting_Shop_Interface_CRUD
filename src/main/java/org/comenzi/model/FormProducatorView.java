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
import model.Producator;

@Route(value = "producator-detalii", layout = MainLayout.class)
public class FormProducatorView extends VerticalLayout implements HasUrlParameter<Long> {

    private Long producatorId;
    private TextField numeField = new TextField("Nume Brand");
    private TextField taraField = new TextField("Țara de Origine");
    private Button btnSave = new Button("Salvează");
    private Button btnDelete = new Button("Șterge");

    public FormProducatorView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        getStyle().set("background", "transparent");

        // --- Container Chenar ---
        VerticalLayout container = new VerticalLayout();
        container.setMaxWidth("500px");
        container.getStyle().set("background-color", "rgba(0, 0, 0, 0.75)");
        container.getStyle().set("border-radius", "15px");
        container.getStyle().set("padding", "40px");
        container.setAlignItems(Alignment.CENTER);

        H2 titlu = new H2("Editare Producător");
        titlu.getStyle().set("color", "white");

        FormLayout form = new FormLayout();
        form.setWidthFull();
        form.add(numeField, taraField);

        // Stilizare Albă
        styleField(numeField);
        styleField(taraField);

        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSave.addClickListener(e -> salveaza());

        btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnDelete.addClickListener(e -> sterge());

        RouterLink backLink = new RouterLink("Înapoi", NavigableGridProducatorView.class);
        backLink.getStyle().set("color", "#cccccc");

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
        this.producatorId = id;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("KartingJPA");
        EntityManager em = emf.createEntityManager();
        try {
            Producator p = em.find(Producator.class, id.intValue()); // Conversie Long -> Integer
            if (p != null) {
                numeField.setValue(p.getNume());
                taraField.setValue(p.getTaraOrigine());
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
            Producator p = em.find(Producator.class, producatorId.intValue());
            if (p != null) {
                p.setNume(numeField.getValue());
                p.setTaraOrigine(taraField.getValue());
                em.merge(p);
                em.getTransaction().commit();
                Notification.show("Salvat!");
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
            Producator p = em.find(Producator.class, producatorId.intValue());
            if (p != null) {
                em.remove(p);
                em.getTransaction().commit();
                Notification.show("Șters!");
                getUI().ifPresent(ui -> ui.navigate(NavigableGridProducatorView.class));
            }
        } catch (Exception e) {
            if(em.getTransaction().isActive()) em.getTransaction().rollback();
            Notification.show("Nu se poate șterge (folosit în alte locuri).");
        } finally {
            em.close();
            emf.close();
        }
    }
}