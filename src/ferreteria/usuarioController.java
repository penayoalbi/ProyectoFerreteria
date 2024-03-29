package ferreteria;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class usuarioController {
    @FXML private Hyperlink principal;
    @FXML private Button btnListar,btnNuevo,btnEditar, btnBorrar, btnGuardar, btnCancelar;
    @FXML private TableView <usuario> tablaUsuario;
    @FXML private TableColumn <usuario, Integer>colUsuarioID;
    @FXML private TableColumn <usuario,String>colTipoDocumento;
    @FXML private TableColumn <usuario,String>colDocumento;
    @FXML private TableColumn <usuario,String>colNombre;
    @FXML private TableColumn <usuario,String>colApellido;
    @FXML private TableColumn <usuario,String>colCorreo;
    @FXML private TableColumn <usuario,String>colUsuario;
    @FXML private TableColumn <usuario,String>colClave;
    @FXML private TableColumn <usuario,String>colCargo;
    @FXML private TableColumn <usuario,String>colTelefono;
    @FXML private TableColumn <usuario,String>colEstado;
    @FXML private TableColumn <usuario,String>colDomicilio;
    @FXML private TextField txtUsuarioID;
    @FXML private TextField txtDocumento;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtTelefono, txtUsuario;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtDomicilio;
    @FXML private TextField txtClave;
    @FXML private ComboBox cmbDocumento, cmbEstado, cmbCargo;
    @FXML private TextField txtPrueba;

    Seguridad seguridad = new Seguridad();//instanciar seguridad
    Controller alert = new Controller();
    clientesController event = new clientesController();
    bd newbd= new bd();//llamo a la clase de conexión a base de datos
    validacion validar = new validacion();
    Alerts alertas = new Alerts();

    ObservableList<usuario> oblist = FXCollections.observableArrayList();//lista observable

    @FXML
    public void controladorClose() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("principal.fxml"));
            Parent root = loader.load();
            PrincipalController vistaPrincipal = loader.getController();//instancia controller class
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            stage.setOnCloseRequest(e ->  vistaPrincipal.loginClose());// cuando se cierra debe ejecutar esto
            Stage myStage = (Stage) this.principal.getScene().getWindow();//para cerrar ventana, volviendo a pantalla principal
            myStage.close();
        } catch (IOException e) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @FXML
    public void initialize() {
        eventoClick();
        List<String> TipoDocumento = new ArrayList<>();
        TipoDocumento.add("Documento Nacional de Identidad");
        TipoDocumento.add("Cédula de Identidad");
        TipoDocumento.add("Pasaporte");
        ObservableList tipoD = FXCollections.observableArrayList(TipoDocumento);
        cmbDocumento.getItems().clear();
        cmbDocumento.setItems(tipoD);
        cmbDocumento.setValue("Seleccionar...");

        List<String> estado = new ArrayList<>();
        estado.add("Habilitado");
        estado.add("Deshabilitado");
        ObservableList boxEstado = FXCollections.observableArrayList(estado);
        cmbEstado.getItems().clear();
        cmbEstado.setItems(boxEstado);
        cmbEstado.setValue("Seleccionar");

        List<String> cargo = new ArrayList<>();
        cargo.add("Administrador");
        cargo.add("Vendedor");
        ObservableList boxCargo = FXCollections.observableArrayList(cargo);
        cmbCargo.getItems().clear();
        cmbCargo.setItems(boxCargo);
        cmbCargo.setValue("Seleccionar");
    }

    @FXML
    public void Listar() {
        System.out.println("Click en listar");
        try {
          //  tablaUsuario.getItems().clear();
            bd base = new bd();
            ResultSet rs;
            rs = base.Consultar("SELECT `usuarioID`, `tipoDocumento`, `documento`, `nombre`, `apellido`, `correo`," +
                    " `usuario`, `clave`, `cargo`, `telefono`, `estado`, `domicilio` FROM `usuarios`");
            while (rs.next()) {
                oblist.add(new usuario (rs.getInt("usuarioID"),
                        rs.getString("tipoDocumento"),
                        rs.getInt("documento"), rs.getString("nombre"),
                        rs.getString("apellido"), rs.getString("correo"),
                        rs.getString("usuario"),rs.getString("clave"),
                        rs.getString("cargo"), rs.getInt("telefono"),
                        rs.getString("estado"), rs.getString("domicilio")
                       // , rs.getInt("claveGeneradaPor")
                ));
            }
            colUsuarioID.setCellValueFactory(new PropertyValueFactory<>("usuarioID"));
            colTipoDocumento.setCellValueFactory(new PropertyValueFactory<>("tipoDocumento"));
            colDocumento.setCellValueFactory(new PropertyValueFactory<>("documento"));
            colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
            colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
            colClave.setCellValueFactory(new PropertyValueFactory<>("clave"));
            colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
            colCargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));
            colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
            colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
            colDomicilio.setCellValueFactory(new PropertyValueFactory<>("domicilio"));
            tablaUsuario.setItems(oblist);//agregar a grillla
            tablaUsuario.refresh();
            //btnListar.setDisable(true);
        } catch (Exception e) {
            alertas.mensajeError("error nulo");
            System.out.println("Error: de nulo " + e.getMessage());
        }
    }

    @FXML
    public void eventoClick(){
        //valorActual apunta a valor seleccionado en la grilla
        tablaUsuario.getSelectionModel().selectedItemProperty().addListener((observableValue, valorAnterior, valorActual) -> {
            txtUsuarioID.setText(String.valueOf(valorActual.getUsuarioID()));
            cmbDocumento.setValue(valorActual.getTipoDocumento());
            txtDocumento.setText(String.valueOf(valorActual.getDocumento()));
            txtNombre.setText(String.valueOf(valorActual.getNombre()));
            txtApellido.setText(String.valueOf(valorActual.getApellido()));
            txtTelefono.setText(String.valueOf(valorActual.getTelefono()));
            txtCorreo.setText(String.valueOf(valorActual.getCorreo()));
            txtDomicilio.setText(String.valueOf(valorActual.getDomicilio()));
           // txtClave.setText(String.valueOf(valorActual.getClave()));
            cmbEstado.setValue(valorActual.getEstado());
            cmbCargo.setValue(valorActual.getCargo());

            btnListar.setDisable(true);
            txtUsuarioID.setDisable(true);
            btnEditar.setDisable(false);
            tablaUsuario.refresh();
        });
    }

    @FXML
    public void nuevoUsuario(){
        limpiarSeldas();
    }

    @FXML
    public void guardar(){
        System.out.println(" click en guardar");
        //btnListar.setDisable(true);
        try{
            if (cmbDocumento.getItems().equals("") || txtDocumento.getText().equals("")
                    || txtNombre.getText().equals("") || txtApellido.getText().equals("")
                    || txtCorreo.getText().equals("") || txtClave.getText().equals("")
                    || cmbCargo.getItems().equals("") || txtTelefono.getText().equals("")
                    || cmbEstado.getItems().equals("")|| txtDomicilio.getText().equals("")) {
                    alert.alert("Error: Campo vacio");
            }else{
                if(validar.validarNumero(txtDocumento.getText()) && validar.validarNumero(txtTelefono.getText())
                        && validar.validarEmail(txtCorreo.getText())){
                    if (txtDocumento.getText().length()>=5){
                        if(buscarCoincidencia(txtDocumento.getText())==1){
                            alertas.mensajeInfo("El usuario ya esta registrado");
                        }else{
                            if(txtNombre.getText().length()>=3 && txtNombre.getText().length()<=30
                                    && txtApellido.getText().length()>=3
                                    && txtApellido.getText().length()<=30){
                                if(event.confirmar()){
                                    String claveEncriptada = Encriptar(txtClave.getText());
                                    int claveGeneraPor = 0;
                                    String insert = "INSERT INTO usuarios (tipoDocumento, documento, nombre, apellido," +
                                            " correo, usuario, clave, cargo, telefono, estado, domicilio, claveGeneradaPor) " +
                                            "VALUES ('"+cmbDocumento.getValue()+
                                            "','"+txtDocumento.getText()+
                                            "','"+txtNombre.getText()+
                                            "','"+txtApellido.getText() +
                                            "','" +txtCorreo.getText()+
                                            "','"+txtUsuario.getText()+
                                            "','" +claveEncriptada+
                                            "','" +cmbCargo.getValue()+
                                            "','" +txtTelefono.getText()+
                                            "','"+cmbEstado.getValue()+
                                            "','" +txtDomicilio.getText()+
                                            "','"+claveGeneraPor+"')";
                                    System.out.println("tipo docu: "+cmbDocumento.getValue());
                                    newbd.Guardar(insert);
                                    alert.alert("Se guardó con exito!");
                                    tablaUsuario.refresh();
                                }else{
                                    System.out.println("Operacion cancelada");
                                    limpiarSeldas();
                                }
                            }else{
                                System.out.println("ERROR: el campo de Nombre/Apellido no es valido");
                            }
                        }
                    }else{
                        System.out.println("ERROR: el campo documento no valido");
                    }
                }else{
                    System.out.println("CARACTER NO VALIDO");
                }
            }
        }catch (Exception e){
            System.out.println("Error al guardar: "+e.getMessage());
        }
   }
   @FXML
   public void Modificar(){
       System.out.println("click en editar");
       btnListar.setDisable(false);
       txtUsuarioID.setDisable(false);
       //campo vacio
       try {
           if (txtUsuarioID.getText().equals("") || cmbDocumento.getItems().equals("")
                   || txtDocumento.getText().equals("") || txtNombre.getText().equals("")
                   || txtApellido.getText().equals("") || txtCorreo.getText().equals("")
                   || txtClave.getText().equals("") || cmbCargo.getItems().equals("")
                   || txtTelefono.getText().equals("") || cmbEstado.getItems().equals("")
                   || txtDomicilio.getText().equals("")) {
               alert.alert("Error. Campo vacio");
           } else {
               String claveModificada= Encriptar(txtClave.getText());
               String modificar = "UPDATE usuarios SET usuarioID ='"
                       + txtUsuarioID.getText() + "',tipoDocumento='"
                       + cmbDocumento.getValue().toString() + "',documento='"
                       + txtDocumento.getText() + "',nombre='"
                       + txtNombre.getText() + "',apellido='"
                       + txtApellido.getText() + "',correo='"
                       + txtCorreo.getText() + "',clave='"
                       +claveModificada + "',cargo='"
                       + cmbCargo.getValue() + "',telefono='"
                       + txtTelefono.getText() + "',estado='"
                       + cmbEstado.getValue() + "',domicilio='"
                       + txtDomicilio.getText() + "'" +
                       "WHERE usuarioID= '" + txtUsuarioID.getText() + "'";
               newbd.modificardatos(modificar);
               alert.alert("Se modificó correctamente.");
               tablaUsuario.refresh();
           }
       }catch (Exception e){
           System.out.println("error en modificar"+e.getMessage());
       }
   }

    @FXML
    public void borrarUsuario(){
        System.out.println("click en borrar");
        try{
            int item= tablaUsuario.getSelectionModel().getSelectedItem().getUsuarioID();
            System.out.println("El item a eliminar es: "+item);
            if(bd.eliminar(item)==1){
                oblist.remove(tablaUsuario.getSelectionModel().getFocusedIndex());//eliminar de la grilla
                alert.alert("El indice eliminado es : " +item);
                tablaUsuario.refresh();
            }else{
                alert.alert("Error al eliminar");
            }
        }catch (Exception e){
            System.out.println("Error "+e.getMessage());
        }
    }

    @FXML
    public void cancelar (){
        limpiarSeldas();
    }

    @FXML
    public void limpiarSeldas(){
        txtUsuarioID.setText(null);
        txtNombre.setText(null);
        txtApellido.setText(null);
        txtDocumento.setText(null);
        cmbCargo.setValue(null);
        txtCorreo.setText(null);
        txtTelefono.setText(null);
        txtClave.setText(null);
        txtDomicilio.setText(null);
        cmbEstado.setValue(null);
        cmbDocumento.setValue(null);
    }

    @FXML
    public String Encriptar(String clave) throws NoSuchAlgorithmException {
        String secretKey = "prueba";
        String encriptar = "";
        encriptar = seguridad.Encriptar(secretKey,clave);
        System.out.println("esto es prueba: " + encriptar);
        return encriptar;
    }

    public String desencriptar(String clave) throws NoSuchAlgorithmException
    {
        String secrectKey = "prueba";
        String cadena_encriptada ="uNKA850kweI=";
        String Texto_desencriptado = Seguridad.Desencriptar(secrectKey,clave);
        //lblTextoDesencriptado.setText(Texto_desencriptado);
        //System.out.println("texto desencriptado: " + Texto_desencriptado);
        return Texto_desencriptado;
    }

    public int buscarCoincidencia(String docu){
        ResultSet rs;
        Integer documento = 0 ;
        int valor = 0;
        try{
        String buscar = "select documento from usuarios where documento = '"+docu+"'";
        rs=newbd.Consultar(buscar);
        if(rs.next()){
             documento = rs.getInt("documento");
        }
        if (documento>0){
            return valor =1;
        }else{
            return valor=0;
        }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return valor;
    }

}
