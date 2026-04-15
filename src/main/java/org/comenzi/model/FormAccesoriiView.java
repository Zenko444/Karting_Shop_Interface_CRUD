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
import model.AccesoriiKarting;

@Route(value = "accesoriu-detalii", layout = MainLayout.class)
public class FormAccesoriiView extends VerticalLayout implements HasUrlParameter<Long> {

    private Long accesoriuId;
    private TextField denumireField = new TextField("Denumire Produs");
    private NumberField pretField = new NumberField("Preț (RON)");
    private TextField tipField = new TextField("Tip");
    private TextField marimeField = new TextField("Mărime");
    private TextField materialField = new TextField("Material");
    private TextField stocField = new TextField("Cantitate (Stoc)");

    private Button btnSave = new Button("Salvează");
    private Button btnDelete = new Button("Șterge");

    public FormAccesoriiView() {
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

        H2 titlu = new H2("Editare Accesoriu");
        titlu.getStyle().set("color", "white");

        FormLayout form = new FormLayout();
        form.setWidthFull();
        form.add(denumireField, pretField, tipField, marimeField, materialField, stocField);

        // Stilizare Albă
        styleField(denumireField); styleField(pretField); styleField(tipField);
        styleField(marimeField); styleField(materialField); styleField(stocField);

        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSave.addClickListener(e -> salveaza());

        btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnDelete.addClickListener(e -> sterge());

        RouterLink backLink = new RouterLink("Înapoi", NavigableGridAccesoriiView.class);
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
        this.accesoriuId = id;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("KartingJPA");
        EntityManager em = emf.createEntityManager();
        try {
            AccesoriiKarting a = em.find(AccesoriiKarting.class, id);
            if (a != null) {
                denumireField.setValue(a.getDenumire());
                pretField.setValue(a.getPret());
                tipField.setValue(a.getTip() != null ? a.getTip() : "");
                marimeField.setValue(a.getMarime() != null ? a.getMarime() : "");
                materialField.setValue(a.getMaterial() != null ? a.getMaterial() : "");
                stocField.setValue(String.valueOf(a.getStoc()));
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
            AccesoriiKarting a = em.find(AccesoriiKarting.class, accesoriuId);
            if (a != null) {
                a.setDenumire(denumireField.getValue());
                a.setPret(pretField.getValue());
                a.setTip(tipField.getValue());
                a.setMarime(marimeField.getValue());
                a.setMaterial(materialField.getValue());

                try {
                    a.setStoc(Integer.parseInt(stocField.getValue()));
                } catch (NumberFormatException ex) {
                    Notification.show("Stocul trebuie să fie un număr întreg!");
                    return;
                }

                em.merge(a);
                em.getTransaction().commit();
                Notification.show("Produs actualizat!");
            }
        } catch (Exception e) {
            if(em.getTransaction().isActive()) em.getTransaction().rollback();
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
            AccesoriiKarting a = em.find(AccesoriiKarting.class, accesoriuId);
            if (a != null) {
                em.remove(a);
                em.getTransaction().commit();
                Notification.show("Produs șters!");
                getUI().ifPresent(ui -> ui.navigate(NavigableGridAccesoriiView.class));
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