package Main.attendence;

public class AttendenceU {

    private String userid;
    private Integer timetype;
    private Integer status;
    private Integer notificationstatus;
    private String dateatt;
    private String monthatt;
    private String msgnotifi;
    private String extrS;

    public AttendenceU(String userid, Integer timetype, Integer status, Integer notificationstatus, String dateatt, String monthatt, String msgnotifi, String extrS) {
        this.userid = userid;
        this.timetype = timetype;
        this.status = status;
        this.notificationstatus = notificationstatus;
        this.dateatt = dateatt;
        this.monthatt = monthatt;
        this.msgnotifi = msgnotifi;
        this.extrS = extrS;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Integer getTimetype() {
        return timetype;
    }

    public void setTimetype(Integer timetype) {
        this.timetype = timetype;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getNotificationstatus() {
        return notificationstatus;
    }

    public void setNotificationstatus(Integer notificationstatus) {
        this.notificationstatus = notificationstatus;
    }

    public String getDateatt() {
        return dateatt;
    }

    public void setDateatt(String dateatt) {
        this.dateatt = dateatt;
    }

    public String getMonthatt() {
        return monthatt;
    }

    public void setMonthatt(String monthatt) {
        this.monthatt = monthatt;
    }

    public String getMsgnotifi() {
        return msgnotifi;
    }

    public void setMsgnotifi(String msgnotifi) {
        this.msgnotifi = msgnotifi;
    }

    public String getExtrS() {
        return extrS;
    }

    public void setExtrS(String extrS) {
        this.extrS = extrS;
    }
}
