package Main.staffservice;

public class reqSend {

    String userId;
    String reqUserid;
    int status;
    int attendstat;
    int reportstat;
    String usertypeid;
    int reqcount;

    public reqSend(String userId, String reqUserid, int status, int attendstat, int reportstat, String usertypeid, int reqcount) {
        this.userId = userId;
        this.reqUserid = reqUserid;
        this.status = status;
        this.attendstat = attendstat;
        this.reportstat = reportstat;
        this.usertypeid = usertypeid;
        this.reqcount = reqcount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReqUserid() {
        return reqUserid;
    }

    public void setReqUserid(String reqUserid) {
        this.reqUserid = reqUserid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAttendstat() {
        return attendstat;
    }

    public void setAttendstat(int attendstat) {
        this.attendstat = attendstat;
    }

    public int getReportstat() {
        return reportstat;
    }

    public void setReportstat(int reportstat) {
        this.reportstat = reportstat;
    }

    public String getUsertypeid() {
        return usertypeid;
    }

    public void setUsertypeid(String usertypeid) {
        this.usertypeid = usertypeid;
    }

    public int getReqcount() {
        return reqcount;
    }

    public void setReqcount(int reqcount) {
        this.reqcount = reqcount;
    }
}
