package controllers;

import models.Archivamiento;
import models.Conexion;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class ImplArchivamiento {
    private Conexion con;
    private ResultSet rs;
    private PreparedStatement ps;
    private final List<Archivamiento> datos = new ArrayList();
    private final List<Archivamiento> dato = new ArrayList();
    
    
    public ImplArchivamiento(){
        con = Conexion.getInstancia();
    }
    
    public boolean subir(Archivamiento data) {
        try {
            try {
                ps = con.conectar().prepareStatement("INSERT INTO Archivamiento(ubigeo,codigo_cpp,dni,nombres_informa,d100,fecha_d100,s100,fecha_s100,fsu,fecha_fsu) VALUES(?,?,?,?,?,?,?,?,?,?);");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ImplArchivamiento.class.getName()).log(Level.SEVERE, null, ex);
            }
            ps.setString(1, data.getUBIGEO());
            ps.setString(2, data.getCPP());
            ps.setString(3, data.getDNI());
            ps.setString(4, data.getNOMBRES());
            ps.setString(5, data.getND100());
            ps.setString(6, data.getFECHAD100());
            ps.setString(7, data.getS100());
            ps.setString(8, data.getFECHAS100());
            ps.setString(9, data.getFSU());
            ps.setString(10, data.getFECHAFSU());
            if (ps.executeUpdate() < 0) {
                JOptionPane.showMessageDialog(null, "No se logro subir a la base de datos");
                return false;
            }
            ps.close();
            return true;
        } catch (SQLException e) {
            errorHandler(e);
        }finally{
            ps = null;
            con.desconectar();
        }
        return false;
    }

    protected void errorHandler(SQLException errorCode){
        int code = errorCode.getErrorCode();
        switch(code){
            case 1049:
                JOptionPane.showMessageDialog(null,"Error: La base de datos no existe.");
                break;
            case 1146:
                JOptionPane.showMessageDialog(null,"Error de consulta: La tabla no existe.");
                break;
            case 1062:
                JOptionPane.showMessageDialog(null,"Error de integridad: La fila ya existe en la base de datos.");
                break;
            case 1451:
                JOptionPane.showMessageDialog(null,"Error de integridad: No se puede eliminar la fila debido a una restricción de clave foránea.");
                break;
            default:
                JOptionPane.showMessageDialog(null,"Error desconocido: " + errorCode.getMessage());
                break;
        }
    }

    public List<Archivamiento> getData() throws ClassNotFoundException{
        try {
            ps = con.conectar().prepareStatement("SELECT ubigeo,codigo_cpp,dni,nombres_informa,d100,fecha_d100,s100,fecha_s100,fsu,fecha_fsu FROM Archivamiento");
            rs = ps.executeQuery();
            while (rs.next()) {
                //System.out.println(rs.getString(1)+" -- "+rs.getString(2));
                datos.add(new Archivamiento(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10)));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }finally{
            ps = null;
            rs = null;
            con.desconectar();
        }
        return datos;
    }
    
    public List<Archivamiento> searchDNI(String DNI) throws ClassNotFoundException{
        dato.clear();
        try {
            ps = con.conectar().prepareStatement("SELECT ubigeo,codigo_cpp,dni,nombres_informa,d100,fecha_d100,s100,fecha_s100,fsu,fecha_fsu FROM Archivamiento WHERE dni = ?");
            ps.setString(1, DNI);
            rs = ps.executeQuery();
            while (rs.next()) {
                dato.add(new Archivamiento(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10)));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }finally{
            ps = null;
            rs = null;
            con.desconectar();
        }
        
        return dato;
    }
}
