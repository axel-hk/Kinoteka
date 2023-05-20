package com.example.kinoteka.ui;

import com.example.kinoteka.security.SecurityService;
import com.example.kinoteka.views.*;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;


@Route("")
@PermitAll
public class MainLayout extends AppLayout {
    private final SecurityService securityService;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("КиноТека");
        logo.addClassNames(
                LumoUtility.FontSize.LARGE,
                LumoUtility.Margin.MEDIUM);

        String u = securityService.getAuthenticatedUser().getUsername();
        Button logout = new Button("Выйти из " + u, e -> securityService.logout());

        var header = new HorizontalLayout(new DrawerToggle(), logo, logout);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames(
                LumoUtility.Padding.Vertical.NONE,
                LumoUtility.Padding.Horizontal.MEDIUM);

        addToNavbar(header);

    }

    private void createDrawer() {
        if (securityService.getAllUserRoles().contains("admin") || securityService.getAllUserRoles().contains("user"))
            addToDrawer(new VerticalLayout(
                    new RouterLink("Жанры", GenresView.class),
                    new RouterLink("Фильмы", MoviesView.class),
                    new RouterLink("Участники", ParticipantsView.class),
                    new RouterLink("Профессии и Роли", RoleView.class),
                    new RouterLink("Сеансы", SessionView.class),
                    new RouterLink("Студии", StudioView.class),
                    new RouterLink("Билеты", TiketsView.class),
                    new RouterLink("Расписание", TimeTableView.class)
            ));
        if(securityService.getAllUserRoles().contains("admin")) addToDrawer(new VerticalLayout(
                new RouterLink("Пользователи", UserView.class)));
        addToDrawer(new VerticalLayout(
                new RouterLink("Выгрузить документ", UploadView.class)
        ));
    }
}