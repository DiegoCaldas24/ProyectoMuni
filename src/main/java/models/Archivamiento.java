package models;

public class Archivamiento {
    private String UBIGEO;
    private String CPP;
    private String DNI;
    private String NOMBRES;
    private String ND100;
    private String FECHAD100;
    private String S100;
    private String FECHAS100;
    private String FSU;
    private String FECHAFSU;
    
    public Archivamiento(){}
    
    public Archivamiento(String UBIGEO,String DNI, String NOMBRES, String ND100, String FECHAD100, String FSU, String FECHAFSU) {
        this.UBIGEO = UBIGEO;
        this.DNI = DNI;
        this.NOMBRES = NOMBRES;
        this.ND100 = ND100;
        this.FECHAD100 = FECHAD100;
        this.FSU = FSU;
        this.FECHAFSU = FECHAFSU;
    }

    public Archivamiento(String UBIGEO,String DNI, String NOMBRES, String ND100, String FECHAD100,String S100,String FECHAS100, String vacio) {
        this.UBIGEO = UBIGEO;
        this.DNI = DNI;
        this.NOMBRES = NOMBRES;
        this.ND100 = ND100;
        this.FECHAD100 = FECHAD100;
        this.S100 = S100;
        this.FECHAS100 = FECHAS100;
    }
    
    public Archivamiento(String UBIGEO,String CPP, String DNI, String NOMBRES, String ND100, String FECHAD100, String S100, String FECHAS100, String FSU, String FECHAFSU) {
        this.UBIGEO = UBIGEO;
        this.CPP = CPP;
        this.DNI = DNI;
        this.NOMBRES = NOMBRES;
        this.ND100 = ND100;
        this.FECHAD100 = FECHAD100;
        this.S100 = S100;
        this.FECHAS100 = FECHAS100;
        this.FSU = FSU;
        this.FECHAFSU = FECHAFSU;
    }

    public String getCPP() {
        return CPP;
    }

    public void setCPP(String CPP) {
        this.CPP = CPP;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public String getNOMBRES() {
        return NOMBRES;
    }

    public void setNOMBRES(String NOMBRES) {
        this.NOMBRES = NOMBRES;
    }

    public String getND100() {
        return ND100;
    }

    public void setND100(String ND100) {
        this.ND100 = ND100;
    }

    public String getFECHAD100() {
        return FECHAD100;
    }

    public void setFECHAD100(String FECHAD100) {
        this.FECHAD100 = FECHAD100;
    }

    public String getS100() {
        return S100;
    }

    public void setS100(String S100) {
        this.S100 = S100;
    }

    public String getFECHAS100() {
        return FECHAS100;
    }

    public void setFECHAS100(String FECHAS100) {
        this.FECHAS100 = FECHAS100;
    }

    public String getFSU() {
        return FSU;
    }

    public void setFSU(String FSU) {
        this.FSU = FSU;
    }

    public String getFECHAFSU() {
        return FECHAFSU;
    }

    public void setFECHAFSU(String FECHAFSU) {
        this.FECHAFSU = FECHAFSU;
    }
    
    public String getUBIGEO() {
        return UBIGEO;
    }

    public void setUBIGEO(String UBIGEO) {
        this.UBIGEO = UBIGEO;
    }
}
