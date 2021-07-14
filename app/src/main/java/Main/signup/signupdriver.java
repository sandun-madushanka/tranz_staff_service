package Main.signup;

public class signupdriver {

    String name;
    String nic;
    String vehicalno;
    String mobile;
    String email;
    String password;

    public signupdriver(String name, String nic, String vehicalno, String mobile, String email, String password) {
        this.name = name;
        this.nic = nic;
        this.vehicalno = vehicalno;
        this.mobile = mobile;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getVehicalno() {
        return vehicalno;
    }

    public void setVehicalno(String vehicalno) {
        this.vehicalno = vehicalno;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
