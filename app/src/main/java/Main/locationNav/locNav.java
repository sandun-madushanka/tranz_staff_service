package Main.locationNav;

public class locNav {
    private String utyid;
    private String userid;
    private double longitude;
    private double latitude;
    private Integer status;

    public locNav(String utyid, String userid, double longitude, double latitude, Integer status) {
        this.utyid = utyid;
        this.userid = userid;
        this.longitude = longitude;
        this.latitude = latitude;
        this.status = status;
    }

    public String getUtyid() {
        return utyid;
    }

    public void setUtyid(String utyid) {
        this.utyid = utyid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
