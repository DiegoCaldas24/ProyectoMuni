package views;

import java.io.File;
import javax.swing.JFileChooser;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import models.Archivamiento;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.ptg.TblPtg;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import models.Conexion;
import controllers.ImplArchivamiento;
import java.awt.HeadlessException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.toedter.calendar.JDateChooser;
import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Frm_Principal extends javax.swing.JFrame {

    JDateChooser d = new JDateChooser();
    ImplArchivamiento archi = new ImplArchivamiento();
    static List<Archivamiento> dataFSU = new ArrayList<>();
    static List<Archivamiento> dataS100 = new ArrayList<>();
    static List<Archivamiento> prueba = new ArrayList<>();
    Object[] table = new Object[10];
    DefaultTableModel tabla;
    public int rowTable;
    public int columTable;
    Object depaCod = "";

    public Frm_Principal() {
        initComponents();
        this.setExtendedState(MAXIMIZED_BOTH);
        txtDNI.setEnabled(false);
    }

//------------------------------------------------------------------------------
    public List<String> extractD100Date(String text) throws IOException {
        List<String> results = new ArrayList<>();
        // Expresión regular para obtener solo letras
        String regex = "(?<=declaración jurada el )\\d+\\s\\w+\\s\\w+\\s\\w+\\s\\d+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            results.add(matcher.group());
        }
        return results;
    }

    public List<String> extractD100(String text) throws IOException {
        List<String> results = new ArrayList<>();
        // Expresión regular para obtener solo letras
        String regex = "(?<=:)\\s*(\\d{7})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            results.add(matcher.group());
        }
        return results;
    }

    public Integer extractCant(String text) throws IOException {
        int results = 0;
        // Expresión regular para obtener solo letras
        String regex = "(?<=FSU\\s\\s\\s\\s\\s\\s\\s\\s\\s\\s:\\s)\\d{1}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            String a = matcher.group();
            results = Integer.parseInt(a);
        }
        return results;
    }

    public List<String> extractDateFSU(String text) throws IOException {
        List<String> results = new ArrayList<>();
        // Expresión regular para obtener solo letras
        String regex = "(\\d+-\\d+-\\d+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            results.add(matcher.group().trim());
        }
        return results;
    }

    public List<String> extractFSUCode(String text) throws IOException {
        List<String> results = new ArrayList<>();
        // Expresión regular para obtener solo letras
        String regex = "(\\s\\d{8}\\s)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            results.add(matcher.group());
        }
        return results;
    }

    public List<String> extractDNI(String text) throws IOException {
        List<String> results = new ArrayList<>();
        // Expresión regular para obtener solo letras
        String regex = "(\\d{8}-)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {

            results.add(matcher.group().trim());
        }
        return results;
    }

    public List<String> extractS100D100Date(String text) throws IOException {
        List<String> results = new ArrayList<>();
        // Expresión regular para obtener solo letras
        String regex = "(?<=D100   : )\\d+/\\d+/\\d+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            results.add(matcher.group());
        }
        return results;
    }

    public List<String> extractS100Code(String text) throws IOException {
        List<String> results = new ArrayList<>();
        // Expresión regular para obtener solo letras
        String regex = "(\\s\\d{8}\\s)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            results.add(matcher.group().trim());
        }
        return results;
    }

    public List<String> extractNames(String text) throws IOException {
        List<String> results = new ArrayList<>();
        // Expresión regular para obtener solo letras
        String regex = "(?<=\\d{8}-).*([A-Z],*\\n.*|\\n\\s)";//(?<=\\d{8}-).*?(?:\\n|$)
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            results.add(matcher.group().replaceAll(",", "").trim());
        }
        return results;
    }

    public List<String> fechaS100D(String text) throws IOException {
        List<String> results = new ArrayList<>();
        // Expresión regular para obtener solo letras
        String regex = "(\\d+-\\d+-\\d+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            results.add(matcher.group());
        }
        return results;
    }

    public String getContentTwoPage(String path) throws IOException {
        String palabra = "";
        String content = "";
        try {
            PdfReader read = new PdfReader(path);
            int pages = read.getNumberOfPages();
            for (int i = 2; i <= pages; i++) {
                content += PdfTextExtractor.getTextFromPage(read, i);
            }
            palabra = content;

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return palabra;
    }

    public String getContentOnePage(String path) throws IOException {
        String palabra = "";
        String content = null;
        try {
            PdfReader read = new PdfReader(path);
            int pages = read.getNumberOfPages();
            for (int i = 1; i <= pages; i++) {
                content = PdfTextExtractor.getTextFromPage(read, 1);
            }
            palabra = content;

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return palabra;
    }

    public String type(String content) {
        String type = "";
        // Expresión regular para obtener solo letras
        String regex = "(FSU\\s*:\\s*\\d+|S100\\s*:\\s*\\d+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            type = matcher.group().trim();
        }
        return type;
    }

    public String getUbigeo(String content) {
        String ubigeo = "";
        // Expresión regular para obtener solo letras
        String regex = "200605-M";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            ubigeo = matcher.group().trim();
        }
        return ubigeo.replace("-M", "");
    }

    public Integer extractCantS100(String text) throws IOException {
        int results = 0;
        // Expresión regular para obtener solo letras
        String regex2 = "(?<=S100).*: 1";
        Pattern pattern = Pattern.compile(regex2);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            String a = matcher.group().trim().replace(": ", "");
            results = Integer.parseInt(a);
        }
        return results;
    }

    String changeDateType(String date) {
        String formatoOriginal = "dd MMMM yyyy"; // Formato de la fecha original
        String formatoNuevo = "dd-MM-yyyy"; // Formato deseado
        String fechaFormateada = "";
        try {
            // Parseamos la fecha original al formato especificado
            DateTimeFormatter formatterOriginal = DateTimeFormatter.ofPattern(formatoOriginal);
            LocalDate fecha = LocalDate.parse(date, formatterOriginal);

            // Formateamos la fecha al formato deseado
            DateTimeFormatter formatterNuevo = DateTimeFormatter.ofPattern(formatoNuevo);
            fechaFormateada = fecha.format(formatterNuevo);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return fechaFormateada;
    }

    void save(List<String> directories) throws IOException {

        //===============================================
        /*
        Obtiene el path de los archivos posteriormente mediante un ciclo va 
        recorriendo la cantidad de path que hay y realizando cada operacion necesaria
         */
        List<String> a = directories;
        for (String path : a) {
            String content = getContentOnePage(path);//los metodos reciben el path para saber que achivo leer
            String contentTwo = getContentTwoPage(path);//en este caso leen el primer path que se obtuvo
            //===================================================
            List<String> dni = extractDNI(contentTwo);
            List<String> names = extractNames(contentTwo);
            List<String> D100 = extractD100(content);
            List<String> fechaD100 = extractD100Date(content);
            List<String> FSU = extractFSUCode(contentTwo);
            List<String> fechaFSU = extractDateFSU(contentTwo);
            //
            List<String> fechaD100D = extractS100D100Date(content);
            List<String> S100 = extractS100Code(contentTwo);
            List<String> fechaS100 = fechaS100D(contentTwo);
            String ubigeo = getUbigeo(content);
            int cant = extractCant(content);

            for (int i = 0; i < cant; i++) {
                String dniC = dni.get(i).replace("-", "");
                String namesC = names.get(i).replaceAll("\n", " ").replaceAll("-", "");
                String d100 = D100.get(0);
                String fechad100 = changeDateType(fechaD100.get(0).replaceAll("de ", ""));
                String FSUc = FSU.get(i);
                String fechafsu = fechaFSU.get(i);
                dataFSU.add(new Archivamiento(ubigeo, dniC, namesC, d100, fechad100, FSUc, fechafsu));
            }

            if (type(content).contains("S100")) {
                int cantS100 = extractCantS100(content);
                for (int j = 0; j < cantS100; j++) {
                    String dniC = dni.get(j).replace("-", "");
                    String namesC = names.get(j).replaceAll("\n", " ").replaceAll("-", "");
                    String d100 = D100.get(0);
                    String fechad100 = fechaD100D.get(0).replaceAll("/", "-");
                    String s100 = S100.get(0);
                    String fechas100 = fechaS100.get(0);
                    dataS100.add(new Archivamiento(ubigeo, dniC, namesC, d100, fechad100, s100, fechas100, null));
                }
            }
        }
    }

    public List<String> openFiles() throws IOException {
        List<String> files = new ArrayList<>();
        JFileChooser f = new JFileChooser();
        int i = f.showOpenDialog(null);
        if (i == 0) {
            String DIRECTORY = f.getCurrentDirectory().toString().replace("\\", "/") + "/";
            File SELECTDIRECTORY = new File(DIRECTORY);
            String[] fs = SELECTDIRECTORY.list();
            for (String f1 : fs) {
                files.add(DIRECTORY + f1);
            }
            JOptionPane.showMessageDialog(null, "Guardado correctamente");
        } else {
            JOptionPane.showMessageDialog(null, "Se cancelo la carga");
        }
        return files;
    }

//------------------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        logo = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        btnDatr = new javax.swing.JButton();
        btnCreateExcel = new javax.swing.JButton();
        btnClean = new javax.swing.JButton();
        btnUpdateDB = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblConsulta = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        txtDNI = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        cbxSelected = new javax.swing.JComboBox<>();
        jPanel9 = new javax.swing.JPanel();
        btnConfig = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ARCHIVAMIENTO");

        logo.setIcon(new javax.swing.ImageIcon("C:\\Users\\Diego\\Documents\\NetBeansProjects\\Porjecto_muni\\src\\main\\java\\assets\\logo2.png")); // NOI18N
        logo.setPreferredSize(new java.awt.Dimension(286, 210));

        jLabel2.setFont(new java.awt.Font("Tekton Pro", 1, 52)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("SISTEMA DE ARCHIVAMIENTO");

        jLabel5.setFont(new java.awt.Font("Segoe UI Black", 0, 36)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("MUNICIPALIDAD DISTRITAL DE MARCAVELICA");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 854, Short.MAX_VALUE)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jLabel5)
                        .addGap(20, 20, 20)))
                .addContainerGap(255, Short.MAX_VALUE))
        );

        jLabel4.setText("by: Juan Diego Armando Caldas Alvarado");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("PRINCIPAL", jPanel3);

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnDatr.setText("Cargar DJ");
        btnDatr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDatrActionPerformed(evt);
            }
        });

        btnCreateExcel.setText("Crear Reporte");
        btnCreateExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateExcelActionPerformed(evt);
            }
        });

        btnClean.setText("Limpiar tabla");
        btnClean.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCleanActionPerformed(evt);
            }
        });

        btnUpdateDB.setText("Guardar en Base de Datos");
        btnUpdateDB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateDBActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnDatr)
                .addGap(18, 18, 18)
                .addComponent(btnUpdateDB)
                .addGap(18, 18, 18)
                .addComponent(btnCreateExcel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnClean)
                .addGap(19, 19, 19))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDatr)
                    .addComponent(btnCreateExcel)
                    .addComponent(btnClean)
                    .addComponent(btnUpdateDB))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jTable.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "UBIGEO", "CODIGO CPP", "DNI", "NOMBRES COMPLETOS", "N° D100", "FECHA D100", "N° S100", "FECHA S100", "N° FSU", "FECHA FSU"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true, true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable.setRowHeight(30);
        jTable.getTableHeader().setReorderingAllowed(false);
        jTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable);
        if (jTable.getColumnModel().getColumnCount() > 0) {
            jTable.getColumnModel().getColumn(0).setResizable(false);
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1063, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("REGISTRAR", jPanel5);

        tblConsulta.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        tblConsulta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "UBIGEO", "CODIGO CPP", "DNI", "NOMBRES COMPLETOS", "N° D100", "FECHA D100", "N° S100", "FECHA S100", "N° FSU", "FECHA FSU"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true, true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblConsulta.setRowHeight(30);
        tblConsulta.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(tblConsulta);
        if (tblConsulta.getColumnModel().getColumnCount() > 0) {
            tblConsulta.getColumnModel().getColumn(0).setResizable(false);
        }

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1067, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtDNI.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtDNI.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDNIKeyTyped(evt);
            }
        });

        btnSearch.setText("BUSCAR");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        cbxSelected.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbxSelected.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-Seleccionar-", "DNI", "FECHAS" }));
        cbxSelected.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxSelectedItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(cbxSelected, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtDNI, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSearch)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxSelected, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDNI, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch))
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("CONSULTAS", jPanel6);

        btnConfig.setText("Configurar Base de datos");
        btnConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfigActionPerformed(evt);
            }
        });

        jButton1.setText("Configurar path de archivo de archivamiento");

        jButton2.setText("Configurar path de reportes creados");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2)
                    .addComponent(jButton1)
                    .addComponent(btnConfig))
                .addContainerGap(797, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(btnConfig)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addContainerGap(390, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("CONFIGURACION", jPanel9);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnDatrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDatrActionPerformed
        try {
            List<String> f = openFiles();
            save(f);
            mostrar();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }//GEN-LAST:event_btnDatrActionPerformed

    private void btnCreateExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateExcelActionPerformed
        try {
            if (verifyDigitsS100()) {
                createExcel(archi.getData());
            }
        } catch (HeadlessException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }//GEN-LAST:event_btnCreateExcelActionPerformed

    private void btnCleanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCleanActionPerformed
        Integer response = JOptionPane.showConfirmDialog(null, "Desea limpiar la tabla?", "Limpiar tabla", 0);
        cleanTable(response);
    }//GEN-LAST:event_btnCleanActionPerformed

    void cleanTable(Integer response) {
        tabla = (DefaultTableModel) jTable.getModel();
        if (response == 0) {
            if (tabla.getRowCount() > 0) {
                for (int i = 0; i < tabla.getRowCount(); i++) {
                    tabla.setRowCount(0);
                }
                dataFSU.clear();
                dataS100.clear();
            } else {
                JOptionPane.showMessageDialog(null, "La tabla ya esta vacia");
            }
        }
    }

    void cleanTableConsulta() {
        tabla = (DefaultTableModel) tblConsulta.getModel();
        for (int i = 0; i < tabla.getRowCount(); i++) {
            tabla.setRowCount(0);
        }
    }

    void cleanTableData() {
        tabla = (DefaultTableModel) jTable.getModel();
        if (tabla.getRowCount() > 0) {
            for (int i = 0; i < tabla.getRowCount(); i++) {
                tabla.setRowCount(0);
            }
            dataFSU.clear();
            dataS100.clear();
        } else {
            JOptionPane.showMessageDialog(null, "La tabla ya esta vacia");
        }
    }

    //obtener el dato de la tabla
    private void jTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableMouseClicked
        if (jTable.getSelectedColumn() == 1) {
            createSelectedPanel();
            rowTable = jTable.getSelectedRow();
            jTable.setValueAt(depaCod, rowTable, 1);
        }
        if (jTable.getSelectedColumn() == 7) {
            JDateChooser date = new JDateChooser();
            date.setDateFormatString("dd-MM-yyyy");
            date.setSize(200, 100);
            JOptionPane op = new JOptionPane();
            int res = op.showConfirmDialog(null, date, "Seleccione", JOptionPane.OK_OPTION);
            op.setVisible(true);
            if (res == 0) {
                SimpleDateFormat sp = new SimpleDateFormat("dd-MM-yyyy");
                rowTable = jTable.getSelectedRow();
                jTable.setValueAt(sp.format(date.getDate()), rowTable, 7);
            }
        }
    }//GEN-LAST:event_jTableMouseClicked

    private void btnUpdateDBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateDBActionPerformed
        try {
            if (uploadToDB()) {
                JOptionPane.showMessageDialog(null, "Se guardo correctamente");
            }else{
                JOptionPane.showMessageDialog(null, "Error");
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Frm_Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnUpdateDBActionPerformed

    private void btnConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfigActionPerformed
        String password = JOptionPane.showInputDialog("Insert password");
        if (password.equals("Caldas123")) {
            JFileChooser f = new JFileChooser();
            int i = f.showOpenDialog(null);
            if (i == 0) {
                String DIRECTORY = f.getCurrentDirectory().toString().replace("\\", "/") + "/";
                File SELECTDIRECTORY = new File(DIRECTORY);
                String pathDB = "";
                String[] data = SELECTDIRECTORY.list();
                pathDB = DIRECTORY + data[0];
                createFileConfig(pathDB);
            }
        }
    }//GEN-LAST:event_btnConfigActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        if (cbxSelected.getSelectedItem().equals("DNI")) {
            String DNI = txtDNI.getText();
            if (DNI.length() == 8) {
                try {

                    mostrarConsulta(archi.searchDNI(DNI));
                } catch (ClassNotFoundException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            } else {
                JOptionPane.showMessageDialog(null, "El DNI debe tener 8 digitos");
            }
        } else if (cbxSelected.getSelectedItem().equals("FECHAS")) {
            JOptionPane.showMessageDialog(null, "Opcion aun no configurable");
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione una de las dos opciones");
        }
    }//GEN-LAST:event_btnSearchActionPerformed

    private void cbxSelectedItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxSelectedItemStateChanged
        if (cbxSelected.getSelectedItem().equals("DNI")) {
            txtDNI.setEnabled(true);
        } else if (cbxSelected.getSelectedItem().equals("FECHAS")) {
            txtDNI.setText("");
            txtDNI.setEnabled(false);
        } else if (cbxSelected.getSelectedItem().equals("-Seleccionar-")) {
            txtDNI.setText("");
            txtDNI.setEnabled(false);
        }
    }//GEN-LAST:event_cbxSelectedItemStateChanged

    private void txtDNIKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDNIKeyTyped
        String textoActual = txtDNI.getText();

        // Verificar si el texto tiene la longitud máxima permitida
        if (textoActual.length() >= 8) {
            // Si la longitud excede el límite, consumir el evento
            evt.consume();  // Esto evita que se ingrese un nuevo carácter
        }
        // Solo permitir dígitos numéricos
        char tecla = evt.getKeyChar();
        if (!Character.isDigit(tecla)) {
            evt.consume();  // Esto evita que se ingresen caracteres no numéricos
        }
    }//GEN-LAST:event_txtDNIKeyTyped

    void createFileConfig(String path) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("config.txt"));
            writer.write(path);
            JOptionPane.showMessageDialog(null, "Se configuro correctamente");
            writer.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Ocurrió un error al escribir en el archivo.");
            e.printStackTrace();
        }
    }

    void createSelectedPanel() {
        // Datos para la tabla
        String[] columnNames = {"DEPA_COD", "DEPA_NOMBRE"};
        Object[][] data = {
            {"0001", "MARCAVELICA"},
            {"0002", "CEREZAL"},
            {"0004", "FERNANDEZ BAJO"},
            {"0005", "LA BREITA"},
            {"0007", "PAZUL"},
            {"0008", "EL CHILCO"},
            {"0010", "EL ANGOLO UNO"},
            {"0011", "LA CANCHA"},
            {"0012", "SAUCESITO"},
            {"0013", "FAIQUE QUEMADO"},
            {"0014", "EL PENCO"},
            {"0015", "SALADOS"},
            {"0017", "TABLAZON"},
            {"0018", "LA BOCANA"},
            {"0019", "SAN JACINTO (JACINTO)"},
            {"0021", "EL PARQUE"},
            {"0022", "LA PEÑITA"},
            {"0023", "ALGARROBILLO"},
            {"0026", "EL ATASCADERO"},
            {"0027", "ANGELITOS"},
            {"0028", "CHAPANGOS"},
            {"0029", "POCITOS"},
            {"0031", "ORATANGA"},
            {"0032", "BELLAVISTA"},
            {"0033", "TIERRA BLANCA"},
            {"0034", "CABALLO MUERTO"},
            {"0036", "PAJARO BOBO"},
            {"0037", "LA VIBORA"},
            {"0038", "SAUSILLO"},
            {"0039", "PAPELILLO"},
            {"0040", "CHAPETONES"},
            {"0041", "CAÑAS"},
            {"0042", "LA NORIA"},
            {"0044", "PATIO DE SAMAN"},
            {"0045", "EL YUCAL"},
            {"0046", "SAMAN"},
            {"0047", "LA GOLONDRINA"},
            {"0048", "MALLARES"},
            {"0049", "LA SEGUNDA"},
            {"0050", "LA QUINTA"},
            {"0051", "MALLARITOS"},
            {"0053", "LAS PALMERAS"},
            {"0054", "VISTA FLORIDA"},
            {"0055", "MONTERON"},
            {"0056", "SAN MIGUEL DE TANGARARA"},
            {"0057", "PAPAYALILLO"},
            {"0058", "AGUA SALADA"},
            {"0061", "BURGOS"},
            {"0062", "SECTOR PESCADOS"},
            {"0063", "SAMAN CHICO"},
            {"0065", "SANTA CRUZ"},
            {"0066", "PAN DE AZUCAR"},
            {"0067", "PANANGA"},
            {"0068", "FATIQUE LOS LINOS"},
            {"0069", "JAGUAY GRANDE"},
            {"0070", "ALGODONAL"},
            {"0071", "COPA DE SOMBRERO"},
            {"0072", "RASTROS"},
            {"0073", "SAN ROLANDO"},};

        // Crear un modelo de tabla no editable
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Todas las celdas no son editables
            }
        };

        // Crear la tabla con el modelo no editable
        JTable table = new JTable(model);

        // Ajustar tabla dentro de un JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        table.setSize(100, 300);

        JOptionPane optionPane = new JOptionPane(scrollPane, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION);
        JDialog dialog = optionPane.createDialog("Departamentos");

        // Agregar un MouseListener a la tabla para detectar doble clic
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) { // Detectar doble clic
                    int row = table.getSelectedRow();
                    int column = table.getSelectedColumn();
                    if (row != -1 && column != -1) {
                        depaCod = table.getValueAt(row, 0);
                        dialog.dispose();
                    }
                }
            }
        });
        // Mostrar el diálogo
        dialog.setVisible(true);
    }

    boolean verifyDigitsS100() {
        tabla = (DefaultTableModel) jTable.getModel();
        for (int i = 0; i < tabla.getRowCount(); i++) {
            String data = (String) tabla.getValueAt(i, 6);
            String fecha = (String) tabla.getValueAt(i, 7);
            String cpp = (String) tabla.getValueAt(i, 1);

            if (data == null || data.trim().length() != 8) {
                JOptionPane.showMessageDialog(null, "Hay numeros de S100 mal colocados o vacios");
                return false;
            }
            if (cpp == null) {
                JOptionPane.showMessageDialog(null, "Hay codigos vacios");
                return false;
            }

            if (fecha == null) {
                JOptionPane.showMessageDialog(null, "Te falta completar algunas fechas");
                return false;
            }
        }
        return true;
    }

    boolean uploadToDB() throws ClassNotFoundException {
        if (verifyDigitsS100()) {
            try {
                if (jTable.getRowCount() > 0) {
                    tabla = (DefaultTableModel) jTable.getModel();
                    for (int i = 0; i < jTable.getRowCount(); i++) {
                        String UBIGEO = jTable.getValueAt(i, 0).toString();
                        String CPP = jTable.getValueAt(i, 1).toString();
                        String DNI = jTable.getValueAt(i, 2).toString();
                        String NOMBRES = jTable.getValueAt(i, 3).toString();
                        String D100 = jTable.getValueAt(i, 4).toString();
                        String FECHAD100 = jTable.getValueAt(i, 5).toString();
                        String S100 = jTable.getValueAt(i, 6).toString();
                        String FECHAS100 = jTable.getValueAt(i, 7).toString();
                        String FSU = jTable.getValueAt(i, 8).toString();
                        String FECHAFSU = jTable.getValueAt(i, 9).toString();
                        archi.subir(new Archivamiento(UBIGEO, CPP, DNI, NOMBRES, D100, FECHAD100, S100, FECHAS100, FSU, FECHAFSU));
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Le faltan datos a la tabla");
                }
                cleanTableData();
                return true;
            } catch (HeadlessException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
        return false;
    }

    void createExcel(List<Archivamiento> lista) {
        String path = "C:/Users/Diego/Documents/NetBeansProjects/Porjecto_muni/Archivamiento-copia.xls";

        try (FileInputStream fs = new FileInputStream(new File(path))) {
            Workbook book = new HSSFWorkbook(fs);
            Sheet hoja = book.getSheetAt(0);
            int item = 1;
            int i = 2;
            int data = 3;
            for (Archivamiento archivamiento : lista) {
                Row row = hoja.createRow(i);
                Cell ITEM = row.createCell(0);
                Cell UBIGEO = row.createCell(1);
                Cell CPP = row.createCell(2);
                Cell FORMULACPP = row.createCell(3);
                Cell NOMBRES = row.createCell(4);
                Cell DNI = row.createCell(5);
                Cell D100 = row.createCell(6);
                Cell FECHAd100 = row.createCell(7);
                Cell S100 = row.createCell(8);
                Cell FECHAs100 = row.createCell(9);
                Cell FSU = row.createCell(10);
                Cell FECHAFSU = row.createCell(11);
                Cell FORMULAYEAR = row.createCell(15);

                ITEM.setCellValue(item);
                UBIGEO.setCellValue(archivamiento.getUBIGEO());
                CPP.setCellValue(archivamiento.getCPP());
                FORMULACPP.setCellFormula("VLOOKUP(C" + (data) + ",Hoja2!$A$2:$B$60,2,FALSE)");
                NOMBRES.setCellValue(archivamiento.getNOMBRES());
                DNI.setCellValue(archivamiento.getDNI());
                D100.setCellValue(archivamiento.getND100());
                FECHAd100.setCellValue(archivamiento.getFECHAD100());
                S100.setCellValue(archivamiento.getS100());
                FECHAs100.setCellValue(archivamiento.getFECHAS100());
                FSU.setCellValue(archivamiento.getFSU());
                FECHAFSU.setCellValue(archivamiento.getFECHAFSU());
                FORMULAYEAR.setCellFormula("YEAR(H" + (data) + ")");
                data++;
                item++;
                i++;
            }
            Calendar dal = Calendar.getInstance();
            int day = dal.get(Calendar.DAY_OF_MONTH);
            int hour = dal.get(Calendar.HOUR_OF_DAY);
            int minute = dal.get(Calendar.MINUTE);
            int second = dal.get(Calendar.SECOND);
            try (FileOutputStream os = new FileOutputStream("C:/Users/Diego/Documents/NetBeansProjects/Porjecto_muni/Archivamiento" + day + hour + minute + second + ".xls")) {
                book.write(os);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
            JOptionPane.showMessageDialog(null, "Se creo correctamente");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    void mostrarConsulta(List<Archivamiento> data) {
        cleanTableConsulta();
        tabla = (DefaultTableModel) tblConsulta.getModel();
        if (data.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se encontro ese registro");
        } else {
            for (Archivamiento archivamiento : data) {
                table[0] = archivamiento.getUBIGEO();
                table[1] = archivamiento.getCPP();
                table[2] = archivamiento.getDNI();
                table[3] = archivamiento.getNOMBRES();
                table[4] = archivamiento.getND100();
                table[5] = archivamiento.getFECHAD100();
                table[6] = archivamiento.getS100();
                table[7] = archivamiento.getFECHAS100();
                table[8] = archivamiento.getFSU();
                table[9] = archivamiento.getFECHAFSU();
                tabla.addRow(table);
            }
        }

    }

    void mostrar() {
        tabla = (DefaultTableModel) jTable.getModel();
        for (Archivamiento archivamiento : dataFSU) {
            table[0] = archivamiento.getUBIGEO();
            table[1] = " ";
            table[2] = archivamiento.getDNI();
            table[3] = archivamiento.getNOMBRES();
            table[4] = archivamiento.getND100();
            table[5] = archivamiento.getFECHAD100();
            table[6] = " ";
            table[7] = " ";
            table[8] = archivamiento.getFSU();
            table[9] = archivamiento.getFECHAFSU();
            tabla.addRow(table);
        }

        for (Archivamiento archivamiento : dataS100) {
            table[0] = archivamiento.getUBIGEO();
            table[1] = " ";
            table[2] = archivamiento.getDNI();
            table[3] = archivamiento.getNOMBRES();
            table[4] = archivamiento.getND100();
            table[5] = archivamiento.getFECHAD100();
            table[6] = archivamiento.getS100();
            table[7] = archivamiento.getFECHAS100();
            table[8] = " ";
            table[9] = " ";
            tabla.addRow(table);
        }
    }

    public int getRowTable() {
        return rowTable;
    }

    public void setRowTable(int rowTable) {
        this.rowTable = rowTable;
    }

    public int getColumTable() {
        return columTable;
    }

    public void setColumTable(int columTable) {
        this.columTable = columTable;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Frm_Principal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClean;
    private javax.swing.JButton btnConfig;
    private javax.swing.JButton btnCreateExcel;
    private javax.swing.JButton btnDatr;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnUpdateDB;
    private javax.swing.JComboBox<String> cbxSelected;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    public javax.swing.JTable jTable;
    private javax.swing.JLabel logo;
    public javax.swing.JTable tblConsulta;
    private javax.swing.JTextField txtDNI;
    // End of variables declaration//GEN-END:variables
}
