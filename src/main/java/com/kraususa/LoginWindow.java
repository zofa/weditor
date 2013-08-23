package com.kraususa;


import com.vaadin.ui.Button;
import com.vaadin.ui.*;

/**
 * Created with IntelliJ IDEA.
 * User:
 * Date: 8/14/13
 * Time: 5:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoginWindow extends Window {

    private Button btnLogin = new Button("Login");
    private TextField login = new TextField ( "Username");
    private PasswordField password = new PasswordField ( "Password");


    public LoginWindow ()
    {
        super("Authentication Required !");
        setName ( "login" );
        initUI();
    }

    private void initUI ()
    {
        addComponent ( new Label ("Please login in order to use the application") );
        addComponent ( new Label () );
        addComponent ( login );
        addComponent ( password );
        addComponent ( btnLogin );
    }


}
