package com.example.kinoteka.views;

import com.example.kinoteka.dao.entities.MoviesEntity;
import com.example.kinoteka.dao.entities.SessionsEntity;
import com.example.kinoteka.dao.entities.TicketSalesEntity;
import com.example.kinoteka.dao.repositories.RepositorySessions;
import com.example.kinoteka.dao.repositories.RepositoryTicketSales;
import com.example.kinoteka.dto.CurrentSession;
import com.example.kinoteka.security.SecurityService;
import com.example.kinoteka.services.DtoSerivce;
import com.example.kinoteka.ui.ClearableTextField;
import com.example.kinoteka.ui.MainLayout;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.grid.GridSingleSelectionModel;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

@Route(value="TimeTable", layout = MainLayout.class)
@PageTitle("Распиание | КиноТека")
@PermitAll
public class TimeTableView extends VerticalLayout {
    private final Grid<CurrentSession> grid = new Grid<>();
    private ListDataProvider<CurrentSession> dataProvider;
    private final FormLayout editLayout = new FormLayout();
    private final ClearableTextField search = new ClearableTextField();
    private Button saveButton, deleteButton;
    private final Binder<CurrentSession> binder = new Binder<>();

    private final DtoSerivce dtoSerivce;

    private final SecurityService securityService;

    private CurrentSession currentSession;


    private final RepositorySessions sessions;


    private final RepositoryTicketSales ticketSales;

    @Autowired
    public TimeTableView(DtoSerivce dtoSerivce, SecurityService securityService, RepositorySessions sessions,  RepositoryTicketSales ticketSales) {
        this.dtoSerivce = dtoSerivce;
        this.securityService = securityService;
        this.sessions = sessions;
        this.ticketSales = ticketSales;
    }

    @PostConstruct
    public void init(){
        dtoSerivce.createEntityManager();
        setSizeFull();

        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.addColumn(CurrentSession::getName).setHeader("Название").setSortable(true);
        grid.addColumn(CurrentSession::getTicketCost).setHeader("Стоимсоть Билета").setSortable(true);
        grid.addColumn(CurrentSession::getStartTime).setHeader("Время начала сеанса").setSortable(true);
        grid.addColumn(CurrentSession::getAvailableSeats).setHeader("Количество свободных мест").setSortable(true);
        grid.addColumn(CurrentSession::getSessionNumber).setHeader("Номер Сессии").setSortable(true);


        update();


        TextField titleField = new TextField("Название");

        IntegerField sessionId = new IntegerField("Сессия");

        IntegerField ticketCost = new IntegerField("Стоимсоть Билета");

        DateTimePicker dateTimePicker = new DateTimePicker("Время начала сеанса");

        IntegerField availableSeats = new IntegerField("Количество свободных мест");

        binder.forField(titleField)
                .asRequired()
                .bind(CurrentSession::getName, CurrentSession::setName);

        binder.forField(sessionId)
                .asRequired()
                .bind(CurrentSession::getSessionNumber, CurrentSession::setSessionNumber);

        binder.forField(ticketCost)
                .asRequired()
                .bind(CurrentSession::getTicketCost, CurrentSession::setTicketCost);

        binder.forField(dateTimePicker)
                .asRequired()
                .bind(CurrentSession::getStartTime, CurrentSession::setStartTime);
        binder.forField(availableSeats)
                .asRequired()
                .bind(CurrentSession::getAvailableSeats, CurrentSession::setAvailableSeats);


        HorizontalLayout editBarLayout = new HorizontalLayout();
        saveButton = new Button("Забронировать", this::save);
        deleteButton = new Button("Отменить бронь", this::delete);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        editBarLayout.add(saveButton, deleteButton);

        editLayout.setEnabled(false);
        editLayout.setMaxWidth("20%");
        editLayout.add(titleField,
                sessionId,
                ticketCost,
                dateTimePicker,
                availableSeats,
                editBarLayout);


        GridSingleSelectionModel<CurrentSession> singleSelectionModel = (GridSingleSelectionModel<CurrentSession>)  grid.getSelectionModel();
        singleSelectionModel.addSingleSelectionListener(event -> {
            currentSession = event.getSelectedItem().orElse(null);
            binder.readBean(currentSession);
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
        currentSession = new CurrentSession();
        binder.readBean(currentSession);
        editLayout.setEnabled(true);
        saveButton.setEnabled(true);
        deleteButton.setEnabled(false);
    }

    private void searchFilter(HasValue.ValueChangeEvent<? extends String> valueChangeEvent) {
        dataProvider.setFilter(CurrentSession::getName, item -> item.toLowerCase().contains(valueChangeEvent.getValue().toLowerCase()));
    }

    private void delete(ClickEvent<Button> buttonClickEvent) {
        Optional<SessionsEntity> optionalSessions = sessions.findBySessionId(currentSession.getSessionNumber());
        if(optionalSessions.isPresent()) {
            SessionsEntity sessions1 = optionalSessions.get();
            sessions1.setSeatsAvailable(currentSession.getAvailableSeats());
            sessions.save(sessions1);
        }
        if(binder.isValid()) {
            try {
                binder.writeBean(currentSession);
                if (dataProvider.getItems().contains(currentSession)) {
                    dataProvider.refreshItem(currentSession);
                } else {
                    update();
                    search.clear();
                }
            } catch (ValidationException e) {
                e.printStackTrace();
            }
        }
    }

    private void save(ClickEvent<Button> buttonClickEvent) {
        if(binder.isValid()){
            try{
                binder.writeBean(currentSession);
                TicketSalesEntity ticketSalesEntity = new TicketSalesEntity();
                ticketSalesEntity.setNumTickets(currentSession.getAvailableSeats());
                ticketSalesEntity.setPrice(currentSession.getTicketCost());
                ticketSalesEntity.setSessionId(currentSession.getSessionNumber());
                ticketSalesEntity.setSaleTime(LocalDateTime.now());
                ticketSales.save(ticketSalesEntity);
                if(dataProvider.getItems().contains(currentSession)){
                    dataProvider.refreshItem(currentSession);
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
        dataProvider = new ListDataProvider<>(dtoSerivce.getCurrentSessions());
        grid.setDataProvider(dataProvider);
    }

}