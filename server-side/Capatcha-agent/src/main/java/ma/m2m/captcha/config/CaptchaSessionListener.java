package ma.m2m.captcha.config;

import ma.m2m.captcha.MXCaptchaAgent;
import ma.m2m.captcha.test.TestImageManager;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;


public class CaptchaSessionListener implements HttpSessionListener {

    private final TestImageManager testImageManager;


    public CaptchaSessionListener(TestImageManager testImageManager) {
        this.testImageManager = testImageManager;
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession httpSession = se.getSession();
        MXCaptchaAgent.removeCaptchaTestQstImage(testImageManager, httpSession);
    }
}
