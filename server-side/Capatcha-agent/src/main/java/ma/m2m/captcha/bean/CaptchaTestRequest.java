package ma.m2m.captcha.bean;

public class CaptchaTestRequest {

    private String hostIdentifier;

    private String RequestToken;


    public String getHostIdentifier() {
        return hostIdentifier;
    }

    public String getRequestToken() {
        return RequestToken;
    }

    public void setHostIdentifier(String hostIdentifier) {
        this.hostIdentifier = hostIdentifier;
    }

    public void setRequestToken(String requestToken) {
        RequestToken = requestToken;
    }
}
