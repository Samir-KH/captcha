package ma.m2m.captcha.bean;

public class CaptchaTestRequest {

    private String hostIdentifier;

    private String requestToken;


    public String getHostIdentifier() {
        return hostIdentifier;
    }

    public String getRequestToken() {
        return requestToken;
    }

    public void setHostIdentifier(String hostIdentifier) {
        this.hostIdentifier = hostIdentifier;
    }

    public void setRequestToken(String requestToken) {
        this.requestToken = requestToken;
    }
}
