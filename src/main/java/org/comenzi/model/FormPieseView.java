package org.comenzi.model;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Piesa;

@Route(value = "piesa", layout = MainLayout.class)
public class FormPieseView extends VerticalLayout implements HasUrlParameter<Integer> {

    private Integer piesaId;
    private TextField producatorField = new TextField("Brand (Info)");
    private TextField denumireField = new TextField("Denumire Piesă");
    private NumberField pretField = new NumberField("Preț (RON)");
    private TextField compatibilitateField = new TextField("Compatibilitate");

    private Button btnSave = new Button("Salvează");
    private Button btnDelete = new Button("Șterge");

    public FormPieseView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        getStyle().set("background", "transparent");

        // --- 1. CONFIGURARE CONTAINER (CHENARUL) ---
        VerticalLayout container = new VerticalLayout();
        container.setMaxWidth("600px");
        container.getStyle().set("background-color", "rgba(0, 0, 0, 0.75)"); // Negru transparent
        container.getStyle().set("border-radius", "15px");
        container.getStyle().set("padding", "40px");
        container.setAlignItems(Alignment.CENTER);

        H2 titlu = new H2("Editare Piesă");
        titlu.getStyle().set("color", "white");

        FormLayout form = new FormLayout();
        form.setWidthFull();

        producatorField.setReadOnly(true);
        form.add(producatorField, denumireField, pretField, compatibilitateField);

        // Stil text alb
        styleField(producatorField); styleField(denumireField);
        styleField(pretField); styleField(compatibilitateField);

        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSave.addClickListener(e -> salveaza());

        btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnDelete.addClickListener(e -> sterge());

        RouterLink backLink = new RouterLink("Înapoi", NavigableGridPieseView.class);
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
    public void setParameter(BeforeEvent event, Integer id) {
        this.piesaId = id;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("KartingJPA");
        EntityManager em = emf.createEntityManager();
        try {
            Piesa p = em.find(Piesa.class, id);
            if (p != null) {
                producatorField.setValue(p.getProducator() != null ? p.getProducator().getNume() : "-");
                denumireField.setValue(p.getDenumire());
                pretField.setValue(p.getPret());
                compatibilitateField.setValue(p.getCompatibilitate());
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
            Piesa p = em.find(Piesa.class, piesaId);
            if (p != null) {
                p.setDenumire(denumireField.getValue());
                p.setPret(pretField.getValue());
                p.setCompatibilitate(compatibilitateField.getValue());
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
            Piesa p = em.find(Piesa.class, piesaId);
            if (p != null) {
                em.remove(p);
                em.getTransaction().commit();
                Notification.show("Șters!");
                getUI().ifPresent(ui -> ui.navigate(NavigableGridPieseView.class));
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