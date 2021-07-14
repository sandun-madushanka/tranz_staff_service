package Main.payment;

public class PaymentS {

    String userid;
    String driverid;
    String uTyp;
    String name;
    String defaultFee;
    String month;
    String payment;
    String balance;
    String due;
    String statusPayment;
    String notificationpayment;
    String feeamount;
    String imgUrl;

    public PaymentS(String userid, String driverid, String uTyp, String name, String defaultFee, String month, String payment, String balance, String due, String statusPayment, String notificationpayment, String feeamount, String imgUrl) {
        this.userid = userid;
        this.driverid = driverid;
        this.uTyp = uTyp;
        this.name = name;
        this.defaultFee = defaultFee;
        this.month = month;
        this.payment = payment;
        this.balance = balance;
        this.due = due;
        this.statusPayment = statusPayment;
        this.notificationpayment = notificationpayment;
        this.feeamount = feeamount;
        this.imgUrl = imgUrl;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDriverid() {
        return driverid;
    }

    public void setDriverid(String driverid) {
        this.driverid = driverid;
    }

    public String getuTyp() {
        return uTyp;
    }

    public void setuTyp(String uTyp) {
        this.uTyp = uTyp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultFee() {
        return defaultFee;
    }

    public void setDefaultFee(String defaultFee) {
        this.defaultFee = defaultFee;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getDue() {
        return due;
    }

    public void setDue(String due) {
        this.due = due;
    }

    public String getStatusPayment() {
        return statusPayment;
    }

    public void setStatusPayment(String statusPayment) {
        this.statusPayment = statusPayment;
    }

    public String getNotificationpayment() {
        return notificationpayment;
    }

    public void setNotificationpayment(String notificationpayment) {
        this.notificationpayment = notificationpayment;
    }

    public String getFeeamount() {
        return feeamount;
    }

    public void setFeeamount(String feeamount) {
        this.feeamount = feeamount;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
