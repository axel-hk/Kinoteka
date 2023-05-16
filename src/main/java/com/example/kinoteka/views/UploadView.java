package com.example.kinoteka.views;

import com.example.kinoteka.services.DtoSerivce;
import com.example.kinoteka.ui.MainLayout;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import jakarta.persistence.Access;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;

@Route(value="upload", layout = MainLayout.class)
@PageTitle("Upload | КиноТека")
@PermitAll
public class UploadView extends VerticalLayout {
    private Button downloadButton;

    @Autowired
    private DtoSerivce dtoSerivce;

    public UploadView(){}

    @PostConstruct
    public void init(){
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        downloadButton = new Button("Download", this::upload);
        downloadButton.setIcon(VaadinIcon.DOWNLOAD.create());
        add(downloadButton);

    }

    private void upload(ClickEvent<Button> buttonClickEvent) {
        Document document = new Document();
        dtoSerivce.createEntityManager();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);

            document.open();
            Font font = FontFactory.getFont(FontFactory.COURIER, 18, Font.BOLD, BaseColor.BLACK);
            Chunk chunk = new Chunk("List of Movies", font);
            Paragraph paragraph = new Paragraph();
            paragraph.setAlignment(Element.ALIGN_CENTER);
            paragraph.add(chunk);

            document.add(paragraph);

            int index = 1;
            Font style =  FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
            for(Object[] kino: dtoSerivce.getMoviesGroupedByProducer()){
                String text = String.format("%d. Title: %s, Producer: %s, Country: %s, Number of sold tickets: %d",
                        index, kino[1], kino[3], kino[2], (Integer) kino[0]);
                index++;
                Paragraph list = new Paragraph();
                Chunk string = new Chunk(text, style);
                list.setAlignment(Element.ALIGN_LEFT);
                list.add(string);

                document.add(list);
            }
            document.close();

            StreamResource resource = new StreamResource("Kinoteka.pdf", () -> new ByteArrayInputStream(baos.toByteArray()));
            com.vaadin.flow.component.html.Anchor downloadLink = new  com.vaadin.flow.component.html.Anchor(resource, "Download");
            downloadLink.getElement().setAttribute("download", true);
            downloadLink.add(downloadButton);

            add(downloadLink);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}

