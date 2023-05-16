package com.example.kinoteka.views;

import com.example.kinoteka.dao.entities.SessionsEntity;
import com.example.kinoteka.dao.entities.TicketSalesEntity;
import com.example.kinoteka.dao.repositories.RepositorySessions;
import com.example.kinoteka.dao.repositories.RepositoryTicketSales;
import com.example.kinoteka.security.SecurityService;
import com.example.kinoteka.ui.ClearableTextField;
import com.example.kinoteka.ui.MainLayout;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

@Route(value="Tickets", layout = MainLayout.class)
@PageTitle("Билеты | КиноТека")
@PermitAll
public class TiketsView extends VerticalLayout {
    private final Grid<TicketSalesEntity> grid = new Grid<>();
    private ListDataProvider<TicketSalesEntity> dataProvider;
    private final FormLayout editLayout = new FormLayout();
    private final ClearableTextField search = new ClearableTextField();
    private Button saveButton, deleteButton;
    private final Binder<TicketSalesEntity> binder = new Binder<>();

    private final RepositoryTicketSales ticketSales;

    private final RepositorySessions sessions;


    private  TicketSalesEntity ticketSalesEntity;

    private final SecurityService securityService;

    @Autowired
    public TiketsView(RepositoryTicketSales ticketSales, RepositorySessions sessions, SecurityService securityService) {
        this.ticketSales = ticketSales;
        this.sessions = sessions;
        this.securityService = securityService;
    }

    @PostConstruct
    public void init(){
        setSizeFull();

        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addColumn(TicketSalesEntity::getSaleId).setHeader("SaleId").setSortable(true);
        grid.addColumn(TicketSalesEntity::getSessionId).setHeader("SessionId").setSortable(true);
        grid.addColumn(TicketSalesEntity::getSaleTime).setHeader("SaleTime").setSortable(true);
        grid.addColumn(TicketSalesEntity::getNumTickets).setHeader("NumTickets").setSortable(true);
        grid.addColumn(TicketSalesEntity::getPrice).setHeader("Price").setSortable(true);

        update();



        ComboBox<Integer> sessionsBox = new ComboBox<>("Сессия");
        sessionsBox.setItems(sessions.findAll().stream().map(SessionsEntity::getSessionId).collect(Collectors.toSet()));

        DateTimePicker saleTime = new DateTimePicker("Время продажи");

        IntegerField num = new IntegerField("Количество Билетов");

        IntegerField price = new IntegerField("Цена");
        price.setMin(0);


        binder.forField(sessionsBox)
                .asRequired()
                .bind(TicketSalesEntity::getSessionId, TicketSalesEntity::setSessionId);

        binder.forField(saleTime)
                .asRequired()
                .bind(TicketSalesEntity::getSaleTime, TicketSalesEntity::setSaleTime);

        binder.forField(num)
                .asRequired()
                .bind(TicketSalesEntity::getNumTickets, TicketSalesEntity::setNumTickets);

        binder.forField(price)
                .asRequired()
                .bind(TicketSalesEntity::getPrice, TicketSalesEntity::setPrice);

        HorizontalLayout editBarLayout = new HorizontalLayout();
        saveButton = new Button("Сохранить", this::save);
        deleteButton = new Button("Удалить", this::delete);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        editBarLayout.add(saveButton, deleteButton);

        editLayout.setEnabled(false);
        editLayout.setMaxWidth("15%");
        editLayout.add(
                sessionsBox,
                saleTime,
                num,
                price,
                editBarLayout);


        GridMultiSelectionModel<TicketSalesEntity> multiSelectionModel = (GridMultiSelectionModel<TicketSalesEntity>) grid.getSelectionModel();
        multiSelectionModel.addMultiSelectionListener(multiSelectionEvent -> {
            for (TicketSalesEntity TicketSalesEntity: multiSelectionEvent.getAllSelectedItems()){
                binder.readBean(TicketSalesEntity);
            }
            editLayout.setEnabled(true);
            saveButton.setEnabled(false);
            deleteButton.setEnabled(true);
        });

        binder.addValueChangeListener(event -> saveButton.setEnabled(binder.isValid()));

        Div gridLayout = new Div();
        gridLayout.setSizeFull();
        gridLayout.add(grid);

        HorizontalLayout contentLayout = new HorizontalLayout();
        contentLayout.setSizeFull();
        contentLayout.add(gridLayout);
        if(securityService.getAllUserRoles().contains("admin"))  contentLayout.add(editLayout);

        add(getTBarLayout(), contentLayout);
        setFlexGrow(1, contentLayout);
    }

    private HorizontalLayout getTBarLayout() {
        HorizontalLayout tBarLayout = new HorizontalLayout();
        tBarLayout.setWidth("100%");
        search.getTextField().setId("rcr-user-search");
        search.setPlaceholder("Поиск");
        search.addValueChangeListener(this::searchFilter);

        Button addNewButton = new Button("Новый Фильм", VaadinIcon.PLUS.create());
        addNewButton.addClickListener(this::newItem);

        tBarLayout.add(search);
        tBarLayout.setFlexGrow(1, search);
        tBarLayout.add(addNewButton);
        return tBarLayout;
    }

    private void newItem(ClickEvent<Button> buttonClickEvent) {
        ticketSalesEntity = new TicketSalesEntity();
        binder.readBean(ticketSalesEntity);
        editLayout.setEnabled(true);
        saveButton.setEnabled(true);
        deleteButton.setEnabled(false);
    }

    private void searchFilter(HasValue.ValueChangeEvent<? extends String> valueChangeEvent) {
        if(valueChangeEvent.getValue().equals("")) update();
        else dataProvider.setFilter(TicketSalesEntity::getSessionId,
                item -> item.equals(Integer.parseInt(valueChangeEvent.getValue())));
    }

    private void delete(ClickEvent<Button> buttonClickEvent) {
        ticketSales.delete(ticketSalesEntity);
        update();
    }

    private void save(ClickEvent<Button> buttonClickEvent) {
        if(binder.isValid()){
            try{
                binder.writeBean(ticketSalesEntity);
                ticketSalesEntity = ticketSales.save(ticketSalesEntity);
                if(dataProvider.getItems().contains(ticketSalesEntity)){
                    dataProvider.refreshItem(ticketSalesEntity);
                } else {
                    update();
                    search.clear();
                }
            }
            catch (ValidationException e){
                e.printStackTrace();
            }
        }

    }

    private void update(){
        dataProvider = new ListDataProvider<>(ticketSales.findAllByOrderBySaleId());
        grid.setDataProvider(dataProvider);
    }

}