package Main.staffservice;

public class searchListusr {

    String Userid;
    String fname;
    String cpany;
    String shtCount;
    String avaU;
    String pPic;
    String reqStat;

    public searchListusr(String userid, String fname, String cpany, String shtCount, String avaU, String pPic, String reqStat) {
        Userid = userid;
        this.fname = fname;
        this.cpany = cpany;
        this.shtCount = shtCount;
        this.avaU = avaU;
        this.pPic = pPic;
        this.reqStat = reqStat;
    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        Userid = userid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getCpany() {
        return cpany;
    }

    public void setCpany(String cpany) {
        this.cpany = cpany;
    }

    public String getShtCount() {
        return shtCount;
    }

    public void setShtCount(String shtCount) {
        this.shtCount = shtCount;
    }

    public String getAvaU() {
        return avaU;
    }

    public void setAvaU(String avaU) {
        this.avaU = avaU;
    }

    public String getpPic() {
        return pPic;
    }

    public void setpPic(String pPic) {
        this.pPic = pPic;
    }

    public String getReqStat() {
        return reqStat;
    }

    public void setReqStat(String reqStat) {
        this.reqStat = reqStat;
    }
}
