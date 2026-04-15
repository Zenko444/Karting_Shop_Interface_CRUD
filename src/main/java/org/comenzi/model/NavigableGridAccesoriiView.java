package org.comenzi.model;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.AccesoriiKarting;
import model.MagazinFizic;

import java.util.List;
import java.util.stream.Collectors;

@Route(value = "produse-accesorii", layout = MainLayout.class)
public class NavigableGridAccesoriiView extends VerticalLayout {

    private Grid<AccesoriiKarting> grid = new Grid<>(AccesoriiKarting.class);
    private TextField searchField = new TextField();
    private List<AccesoriiKarting> allItems;

    public NavigableGridAccesoriiView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        getStyle().set("background", "transparent");

        Button btnAdd = new Button("Adaugă Produs");
        btnAdd.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnAdd.addClickListener(e -> creeazaSiNavigheaza());

        searchField.setPlaceholder("Caută ID sau Nume...");
        searchField.setClearButtonVisible(true);
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.addValueChangeListener(e -> filtreazaGrid());

        searchField.getStyle().set("--vaadin-input-field-value-color", "#ffffff");
        searchField.getStyle().set("--vaadin-input-field-placeholder-color", "#cccccc");
        searchField.getStyle().set("--vaadin-text-field-background", "rgba(255, 255, 255, 0.1)");
        searchField.getStyle().set("color", "white");

        HorizontalLayout toolbar = new HorizontalLayout(searchField, btnAdd);
        toolbar.setWidthFull();

        configureGrid();
        fetchData();
        grid.getStyle().set("border-radius", "10px");

        VerticalLayout contentBox = new VerticalLayout(toolbar, grid);
        contentBox.setMaxWidth("1200px");
        contentBox.setHeight("90%");
        contentBox.getStyle().set("background-color", "rgba(0, 0, 0, 0.6)");
        contentBox.getStyle().set("border-radius", "15px");
        contentBox.getStyle().set("padding", "20px");
        contentBox.getStyle().set("box-shadow", "0 4px 15px rgba(0,0,0,0.5)");

        add(contentBox);
    }

    private void configureGrid() {
        grid.removeAllColumns();
        grid.addColumn(AccesoriiKarting::getId).setHeader("ID").setWidth("80px").setFlexGrow(0);
        grid.addColumn(AccesoriiKarting::getDenumire).setHeader("Produs");
        grid.addColumn(acc -> acc.getPret() + " RON").setHeader("Preț");
        grid.addColumn(AccesoriiKarting::getStoc).setHeader("Stoc");
        grid.addColumn(this::gasesteMagazin).setHeader("Magazin");

        grid.addComponentColumn(item -> {
            Button btnEdit = new Button("Edit");
            btnEdit.addThemeVariants(ButtonVariant.LUMO_SMALL);
            btnEdit.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(FormAccesoriiView.class, Long.valueOf(item.getId()))));
            Button btnDelete = new Button("Șterge");
            btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_SMALL);
            btnDelete.addClickListener(e -> stergeItem(item));
            return new HorizontalLayout(btnEdit, btnDelete);
        }).setHeader("Acțiuni");
    }

    private String gasesteMagazin(AccesoriiKarting produs) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("KartingJPA");
        EntityManager em = emf.createEntityManager();
        try {
            List<MagazinFizic> magazine = em.createQuery(
                            "SELECT m FROM MagazinFizic m WHERE m.produsVedeta.id = :pid", MagazinFizic.class)
                    .setParameter("pid", produs.getId())
                    .getResultList();
            if (!magazine.isEmpty()) return magazine.get(0).getDenumire();
            return "-";
        } catch (Exception e) { return "Err"; } finally { em.close(); emf.close(); }
    }

    private void stergeItem(AccesoriiKarting item) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("KartingJPA");
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            AccesoriiKarting a = em.find(AccesoriiKarting.class, item.getId());
            if (a != null) {
                em.remove(a);
                em.getTransaction().commit();
                Notification.show("Șters!");
                fetchData();
            }
        } catch (Exception e) {
            if(em.getTransaction().isActive()) em.getTransaction().rollback();
            Notification.show("Eroare la ștergere.");
        } finally {
            em.close();
            emf.close();
        }
    }

    private void fetchData() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("KartingJPA");
        EntityManager em = emf.createEntityManager();
        try {
            allItems = em.createQuery("SELECT a FROM AccesoriiKarting a", AccesoriiKarting.class).getResultList();
            filtreazaGrid();
        } finally {
            em.close();
            emf.close();
        }
    }

    private void filtreazaGrid() {
        String val = searchField.getValue();
        if (allItems != null) {
            if (val == null || val.isEmpty()) {
                grid.setItems(allItems);
            } else {
                List<AccesoriiKarting> filtered = allItems.stream()
                        .filter(a -> String.valueOf(a.getId()).contains(val) || a.getDenumire().toLowerCase().contains(val.toLowerCase()))
                        .collect(Collectors.toList());
                grid.setItems(filtered);
            }
        }
    }

    private void creeazaSiNavigheaza() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("KartingJPA");
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            AccesoriiKarting a = new AccesoriiKarting("Produs Nou", 0.0, null, "-", "-", "-");
            em.persist(a);
            em.getTransaction().commit();
            getUI().ifPresent(ui -> ui.navigate(FormAccesoriiView.class, Long.valueOf(a.getId())));
        } finally {
            em.close();
            emf.close();
        }
    }
}