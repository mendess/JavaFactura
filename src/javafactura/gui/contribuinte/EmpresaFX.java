package javafactura.gui.contribuinte;

import javafactura.businessLogic.ContribuinteEmpresarial;
import javafactura.businessLogic.ContribuinteIndividual;
import javafactura.businessLogic.Factura;
import javafactura.businessLogic.JavaFactura;
import javafactura.businessLogic.exceptions.NotContribuinteException;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Comparator;

/**
 * Class that represents the {@link ContribuinteEmpresarial} screen
 * {@inheritDoc}
 */
public class EmpresaFX extends ShowReceiptsFx {

    /**
     * The total money made by the company
     */
    private final TextFlow totalFacturado;
    /**
     * The company's clients
     */
    private final TableView<ContribuinteIndividual> clients;
    /**
     * The view client sub screen
     */
    private final EmpresaViewClientFX viewClientFX;

    /**
     * Constructor for a application window
     * @param javaFactura   The business logic instance
     * @param primaryStage  The stage where the window exists
     * @param previousScene The previous scene (null if this is the root window)
     */
    public EmpresaFX(JavaFactura javaFactura, Stage primaryStage, Scene previousScene){
        super(javaFactura, primaryStage, previousScene, false);

        EmpresaIssueReceiptFX empresaIssueReceiptFX
                = new EmpresaIssueReceiptFX(this.javaFactura, this.primaryStage,
                                            this.scene, new ShowReceiptsFx.TableRefresher());

        EmpresaProfileFX empresaProfileFX = new EmpresaProfileFX(this.javaFactura, this.primaryStage, this.scene);

        this.viewClientFX = new EmpresaViewClientFX(this.javaFactura, this.primaryStage, this.scene);

        int row = 0;
        Button profileButton = new Button("Perfil");
        profileButton.setOnAction(e -> empresaProfileFX.show());
        this.gridPane.add(makeHBox(profileButton, Pos.TOP_RIGHT), 1, row);

        Button issueReceipt = new Button("Emitir Factura");
        issueReceipt.setOnAction(e -> empresaIssueReceiptFX.show());
        this.gridPane.add(makeHBox(issueReceipt, Pos.TOP_LEFT), 0, row++);

        // Receipts Table
        this.gridPane.add(this.sortBox, 0, row++);
        this.gridPane.add(this.receiptsTable, 0, row);

        // Clients Table
        this.clients = new TableView<>();
        makeClientsTable();
        this.gridPane.add(this.clients, 1, row++);

        this.totalFacturado = new TextFlow();

        this.from = null;
        this.to = null;
        HBox toFactBox = new HBox(this.totalFacturado, datePickerFrom, datePickerTo);
        toFactBox.setSpacing(10);
        this.gridPane.add(toFactBox, 0, row++);

        Button goBack = new Button("Logout");
        goBack.setOnAction(event -> goBack());
        this.gridPane.add(makeHBox(goBack, Pos.BOTTOM_RIGHT), 1, row);
    }

    /**
     * Handles the creation of the clients table
     *
     * Creates the columns and adds listeners to open the {@link EmpresaViewClientFX} sub screen
     */
    private void makeClientsTable(){
        this.clients.setMinWidth(this.gridPane.getMinWidth());
        this.clients.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<ContribuinteIndividual,String> nif = new TableColumn<>("NIF");
        nif.setMinWidth(Factura.dateFormat.toString().length());
        nif.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue().getNif()));

        TableColumn<ContribuinteIndividual,String> name = new TableColumn<>("Nome");
        name.setMinWidth(100);
        name.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getName()));

        TableColumn<ContribuinteIndividual,Long> numCompras = new TableColumn<>("#Compras");
        numCompras.setMinWidth(100);
        numCompras.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(
                param.getValue().countFacturas(this.javaFactura.getLoggedUserNif())));
        this.clients.setRowFactory(tv -> {
            TableRow<ContribuinteIndividual> r = new TableRow<>();
            r.setOnMouseClicked(event -> {
                if(event.getButton().equals(MouseButton.PRIMARY)
                   && event.getClickCount() == 2
                   && !r.isEmpty()){
                    this.viewClientFX.setClient(r.getItem());
                    this.viewClientFX.show();
                }
            });
            return r;
        });
        this.clients.getColumns().add(nif);
        this.clients.getColumns().add(name);
        this.clients.getColumns().add(numCompras);
    }

    /**
     * {@inheritDoc}
     *
     * Also updates receipts
     * @return {@inheritDoc}
     */
    @Override
    public boolean show(){
        return super.show() && updateReceipts();
    }

    /**
     * {@inheritDoc} and the total
     */
    @Override
    protected boolean updateReceipts(){
        boolean noDates = this.from == null && this.to == null;
        LocalDate from = null;
        LocalDate to = null;
        if(!noDates){
            from = this.from != null ? this.from : LocalDate.MIN;
            to = this.to != null ? this.to : LocalDate.MAX;
        }
        try{
            Text a = new Text("Total faturado: ");
            a.setStyle("-fx-font-weight: bold");
            Text b = new Text(
                    String.format("%.2f€", noDates ? this.javaFactura.totalFaturado()
                                                   : this.javaFactura.totalFaturado(from, to)));
            this.totalFacturado.getChildren().setAll(a, b);
            Comparator<Factura> c = getFacturaComparator();
            if(c == null){
                this.facturas.setAll(noDates ? this.javaFactura.getLoggedUserFacturas()
                                             : this.javaFactura.getLoggedUserFacturas(from, to));
            }else{
                this.facturas.setAll(noDates ? this.javaFactura.getLoggedUserFacturas(c)
                                             : this.javaFactura.getLoggedUserFacturas(c, from, to));
            }
            this.clients.getItems().setAll(this.javaFactura.getClients());
        }catch(NotContribuinteException e){
            goBack();
            return false;
        }
        return true;
    }
}
