package com.example.kinoteka.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("login")
@PageTitle("Login | Vaadin CRM")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private LoginForm loginForm;

    public LoginView(){
        LoginI18n i18n = LoginI18n.createDefault();
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);


        LoginI18n.Form i18nForm = i18n.getForm();
        i18nForm.setTitle("КиноТека");
        i18nForm.setUsername("Логин");
        i18nForm.setPassword("Пароль");
        i18nForm.setSubmit("Войти");
        i18nForm.setForgotPassword("Забыли пароль?");
        i18n.setForm(i18nForm);

        LoginI18n.ErrorMessage i18nErrorMessage = i18n.getErrorMessage();
        i18nErrorMessage.setTitle("Ошибка Входа");
        i18nErrorMessage.setMessage(
                "Не удалось войти. Возможно вы использовали неверный логин или пароль");
        i18n.setErrorMessage(i18nErrorMessage);

        i18n.setAdditionalInformation("Приложение Анненкова А.В.");

        LoginOverlay loginOverlay = new LoginOverlay();
        loginOverlay.setI18n(i18n);

        loginForm = new LoginForm(i18n);
        loginForm.setAction("login");
        add(loginForm);

    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        // inform the user about an authentication error
        if(beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            loginForm.setError(true);
        }
    }
}
