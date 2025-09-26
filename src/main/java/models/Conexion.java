package models;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Conexion {
    private final String URL = "jdbc:sqlite:data/dbMuni.db";
    public static Conexion instancia;
    public Connection conexion; 
    //constructor de la clase
    private Conexion(){}
    
    //metodo para confirma si se conecto de forma correcta
    public Connection conectar() throws ClassNotFoundException{
        try{
            this.conexion = DriverManager.getConnection(URL);//obtener la ruta para conectar
            return conexion;//devolver la conexion
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error en la conexion");
            return null;  
        }
    }
    //metodo para desconectar de forma correcta
    public void desconectar(){
        try{
            this.conexion.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    //permite acceder una sola vez a la conexion
    public synchronized static Conexion getInstancia(){
       if (instancia==null){//va revisar si existe una conexion o no y si no existe creara una sola
            instancia=new Conexion();
        }
        return instancia;
    }
}
