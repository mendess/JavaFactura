package javafactura.gui;

import javafactura.businessLogic.JavaFactura;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class AdminFX extends FX {

    private final AdminAddIndividualFX addIndividual;
    private final AdminAddEmpresaFX addEmpresa;
    private final AdminTop10ContribuintesFX top10Contribuintes;
    private final AdminTopXEmpresasFX topXEmpresas;

    public AdminFX(JavaFactura javaFactura, Stage primaryStage, Scene previousScene){
        super(javaFactura, primaryStage, previousScene);

        this.addIndividual = new AdminAddIndividualFX(this.javaFactura, this.primaryStage, this.scene);
        this.addEmpresa = new AdminAddEmpresaFX(this.javaFactura, this.primaryStage, this.scene);
        this.top10Contribuintes = new AdminTop10ContribuintesFX(this.javaFactura, this.primaryStage, this.scene);
        this.topXEmpresas = new AdminTopXEmpresasFX(this.javaFactura, this.primaryStage, this.scene);

        int row = 0;
        // [SCENE TITLE]
        Text sceneTitle = new Text("Bem vindo");
        this.gridPane.add(sceneTitle, 0, row++, 2, 1);

        // [BUTTON] Add Contribuinte Individual
        Button addIndividualButton = new Button("Add Contribuinte Individual");
        addIndividualButton.setOnAction(event -> this.addIndividual.show());
        this.gridPane.add(makeHBox(addIndividualButton, Pos.CENTER), 0, row++);

        // [BUTTON] Add Contribuinte Empresarial
        Button addEmpresaButton = new Button("Add Contribuinte Empresarial");
        addEmpresaButton.setOnAction(event -> this.addEmpresa.show());
        this.gridPane.add(makeHBox(addEmpresaButton, Pos.CENTER), 0, row++);

        // [BUTTON] Ver Top 10 Contribuintes
        Button top10ContribuintesButton = new Button("Top 10 Contribuintes");
        top10ContribuintesButton.setOnAction(event -> this.top10Contribuintes.show());
        this.gridPane.add(makeHBox(top10ContribuintesButton, Pos.CENTER), 0, row++);

        // [BUTTON] Ver as top X empresas
        Button topXEmpresasButton = new Button("Top X Empresas");
        topXEmpresasButton.setOnAction(event -> this.topXEmpresas.show());
        this.gridPane.add(makeHBox(topXEmpresasButton, Pos.CENTER), 0, row++);

        // [PASSWORD FIELD] The field to use to change the password
        PasswordField pwf = new PasswordField();
        this.gridPane.add(pwf,1,1);

        // [PASSWORD ERROR] The error text for the password field
        Text pwfErrorText = new Text();
        this.gridPane.add(pwfErrorText,1,3);

        // [PASSWORD BUTTON] The button to change to password
        Button pwfButton = new Button("Mudar password");
        pwfButton.setOnAction(e ->{
            if(pwf.getText().length() < 5){
                pwfErrorText.setFill(Color.RED);
                pwfErrorText.setText("Password tem de ter mais\ndo que 4 caracteres.");
            }else{
                this.javaFactura.setAdminPassword(pwf.getText());
                pwfErrorText.setFill(Color.GREEN);
                pwfErrorText.setText("Password alterada com sucesso.");
            }
        });
        this.gridPane.add(makeHBox(pwfButton, Pos.CENTER_LEFT),1,2);

        // [BUTTON] Log out
        Button logOutButton = new Button("Log out");
        logOutButton.setOnAction(event -> logOut());
        this.gridPane.add(makeHBox(logOutButton, Pos.CENTER), 0, row);
    }

    private void logOut(){
        this.javaFactura.logout();
        this.primaryStage.setScene(this.previousScene);
    }

    @Override
    protected void goBack(){
        throw new UnsupportedOperationException();
    }
}
