package com.kraususa;


import com.vaadin.ui.*;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User:
 * Date: 8/14/13
 * Time: 5:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoginWindow extends Window {

    private static Logger logger = Logger.getLogger(LoginWindow.class);

    private Button btnLogin = new Button("Login");
    private TextField login = new TextField("Username");
    private PasswordField password = new PasswordField("Password");


    public LoginWindow() {
        super("Authentication Required !");
        setName("login");
        initUI();
    }

    private void initUI() {
        addComponent(new Label("Please login in order to use the application"));
        addComponent(new Label());
        addComponent(login);
        addComponent(password);
        addComponent(btnLogin);
        btnLogin.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                setContent(new MainWindow());
            }
        });
    }


}
