package org.comenzi.model;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
        String urlImagine = "https://i.imgur.com/8a8FR6y.jpeg";
        getElement().getStyle()
                .set("background-image", "url('" + urlImagine + "')")
                .set("background-size", "cover")        // Acoperă tot ecranul
                .set("background-position", "center")   // Centrează imaginea
                .set("background-attachment", "fixed")  // Imaginea stă fixă când dai scroll
                .set("min-height", "100vh");

    }

    private void createHeader() {
        H1 logo = new H1("Karting Hub");
        logo.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0")
                .set("margin-right", "20px");

        MenuBar menuBar = new MenuBar();
        menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY);

        // 1. Gestiune
        MenuItem gestiune = menuBar.addItem("Gestiune");
        SubMenu gestiuneSub = gestiune.getSubMenu();
        gestiuneSub.addItem("Vânzări", e -> getUI().ifPresent(ui -> ui.navigate(NavigableGridVanzariView.class)));
        gestiuneSub.addItem("Producători", e -> getUI().ifPresent(ui -> ui.navigate(NavigableGridProducatorView.class)));

        // 2. Produse (Accesorii)
        MenuItem produseItem = menuBar.addItem("Accesorii");
        SubMenu prodSub = produseItem.getSubMenu();
        prodSub.addItem("Accesorii Karting", e -> getUI().ifPresent(ui -> ui.navigate(NavigableGridAccesoriiView.class)));

        // 3. KARTURI & PIESE (NOU)
        MenuItem karturiItem = menuBar.addItem("Karturi & Piese");
        SubMenu kartSub = karturiItem.getSubMenu();
        kartSub.addItem("Karturi Complete", e -> getUI().ifPresent(ui -> ui.navigate(NavigableGridKartView.class)));
        kartSub.addItem("Piese de Schimb", e -> getUI().ifPresent(ui -> ui.navigate(NavigableGridPieseView.class)));

        // 4. Organizare
        MenuItem organizare = menuBar.addItem("Organizare");
        SubMenu orgSub = organizare.getSubMenu();
        orgSub.addItem("Calendar Competiții", e -> getUI().ifPresent(ui -> ui.navigate(NavigableGridCompetitiiView.class)));
        orgSub.addItem("Rețea Magazine", e -> getUI().ifPresent(ui -> ui.navigate(NavigableGridMagazineView.class)));

        HorizontalLayout header = new HorizontalLayout(logo, menuBar);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.addClassName("header");
        header.getStyle().set("padding", "0 20px");
        header.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-10pct)");

        addToNavbar(header);
    }
}