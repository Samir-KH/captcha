package ma.m2m.captcha.bean;



public class CaptchaTest {
    private long id;
    private String hostIdentifier;

    private String RequestToken;

    private CaptchaTestQst captchaTestQst;


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }

    public String getHostIdentifier() {
        return hostIdentifier;
    }

    public void setHostIdentifier(String hostIdentifier) {
        this.hostIdentifier = hostIdentifier;
    }

    public String getRequestToken() {
        return RequestToken;
    }

    public void setRequestToken(String requestToken) {
        RequestToken = requestToken;
    }


    public CaptchaTestQst getCaptchaTestQst() {
        return captchaTestQst;
    }

    public void setCaptchaTestQst(CaptchaTestQst captchaTestQst) {
        this.captchaTestQst = captchaTestQst;
    }
}


