package Main.signup;

public class signinadapter {
    String username;
    String password;
    String userloginid;
    String usrid;
    String usrtypeid;

    public signinadapter(String username, String password, String userloginid, String usrid, String usrtypeid) {
        this.username = username;
        this.password = password;
        this.userloginid = userloginid;
        this.usrid = usrid;
        this.usrtypeid = usrtypeid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserloginid() {
        return userloginid;
    }

    public void setUserloginid(String userloginid) {
        this.userloginid = userloginid;
    }

    public String getUsrid() {
        return usrid;
    }

    public void setUsrid(String usrid) {
        this.usrid = usrid;
    }

    public String getUsrtypeid() {
        return usrtypeid;
    }

    public void setUsrtypeid(String usrtypeid) {
        this.usrtypeid = usrtypeid;
    }
}
