package com.example.kinoteka.views;

import com.example.kinoteka.dao.entities.MoviesEntity;
import com.example.kinoteka.dao.entities.UserrolesEntity;
import com.example.kinoteka.dao.entities.UsersEntity;
import com.example.kinoteka.dao.repositories.RepositoryUserroles;
import com.example.kinoteka.dao.repositories.RepositoryUsers;
import com.example.kinoteka.security.SecurityService;
import com.example.kinoteka.ui.ClearableTextField;
import com.example.kinoteka.ui.MainLayout;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.grid.GridSingleSelectionModel;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;


@Route(value="Users", layout = MainLayout.class)
@PageTitle("Студия | КиноТека")
@PermitAll
public class UserView extends VerticalLayout {
    private final Grid<UsersEntity> grid = new Grid<>();
    private ListDataProvider<UsersEntity> dataProvider;
    private final FormLayout editLayout = new FormLayout();
    private final ClearableTextField search = new ClearableTextField();
    private Button saveButton, deleteButton;
    private final Binder<UsersEntity> binder = new Binder<>();

    private  UsersEntity usersEntity;

    private final RepositoryUsers users;

    private final RepositoryUserroles roles;

    private final SecurityService securityService;

    @Autowired
    public UserView(SecurityService securityService, RepositoryUsers users,  RepositoryUserroles roles) {
        this.users = users;
        this.securityService = securityService;
        this.roles = roles;
    }

    @PostConstruct
    public void init(){
        setSizeFull();

        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.addColumn(UsersEntity::getId).setHeader("Id").setSortable(true);
        grid.addColumn(UsersEntity::getUsername).setHeader("UserName").setSortable(true);

        update();


        TextField nameField = new TextField("Логин");
        PasswordField passwordField = new PasswordField("Пароль");
        CheckboxGroup<UserrolesEntity> checkboxGroup = new CheckboxGroup<>("Роли пользователя",
                roles.findAllByOrderByRolenameAsc());
        checkboxGroup.setItemLabelGenerator(UserrolesEntity::getRolename);

        binder.forField(nameField)
                .asRequired()
                .bind(UsersEntity::getUsername, UsersEntity::setUsername);

        binder.forField(passwordField)
                .asRequired()
                .bind(UsersEntity::getPassword, UsersEntity::setPassword);

        binder.forField(checkboxGroup)
                .asRequired()
                .bind(UsersEntity::getUserRoles, UsersEntity::setUserRoles);

        HorizontalLayout editBarLayout = new HorizontalLayout();
        saveButton = new Button("Сохранить", this::save);
        deleteButton = new Button("Удалить", this::delete);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        editBarLayout.add(saveButton, deleteButton);

        editLayout.setEnabled(false);
        editLayout.add(nameField, passwordField, checkboxGroup, editBarLayout);
        editLayout.setMaxWidth("15%");


        GridSingleSelectionModel<UsersEntity> singleSelectionModel = (GridSingleSelectionModel<UsersEntity>)  grid.getSelectionModel();
        singleSelectionModel.addSingleSelectionListener(event -> {
            usersEntity = event.getSelectedItem().orElse(null);
            binder.readBean(usersEntity);
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

        Button addNewButton = new Button("Новый Жанр", VaadinIcon.PLUS.create());
        addNewButton.addClickListener(this::newItem);

        tBarLayout.add(search);
        tBarLayout.setFlexGrow(1, search);
        tBarLayout.add(addNewButton);
        return tBarLayout;
    }

    private void newItem(ClickEvent<Button> buttonClickEvent) {
        usersEntity = new UsersEntity();
        binder.readBean(usersEntity);
        editLayout.setEnabled(true);
        saveButton.setEnabled(true);
        deleteButton.setEnabled(false);
    }

    private void searchFilter(HasValue.ValueChangeEvent<? extends String> valueChangeEvent) {
        dataProvider.setFilter(UsersEntity::getUsername, item -> item.toLowerCase().contains(valueChangeEvent.getValue().toLowerCase()));
    }

    private void delete(ClickEvent<Button> buttonClickEvent) {
        users.delete(usersEntity);
        update();
    }

    private void save(ClickEvent<Button> buttonClickEvent) {
        if(binder.isValid()){
            try{
                binder.writeBean(usersEntity);
                usersEntity = users.save(usersEntity);
                if(dataProvider.getItems().contains(usersEntity)){
                    dataProvider.refreshItem(usersEntity);
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
        dataProvider = new ListDataProvider<>(users.findAllByOrderByUsername());
        grid.setDataProvider(dataProvider);
    }
}
