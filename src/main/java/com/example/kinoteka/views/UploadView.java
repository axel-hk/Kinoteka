package com.example.kinoteka.views;

import com.example.kinoteka.ui.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route(value="upload", layout = MainLayout.class)
@PageTitle("Upload | КиноТека")
@PermitAll
public class UploadView extends VerticalLayout {

}
