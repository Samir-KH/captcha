package ma.m2m.captcha;

import ma.m2m.captcha.bean.CaptchaTest;
import ma.m2m.captcha.bean.CaptchaTestQst;
import ma.m2m.captcha.bean.CaptchaTestRequest;
import ma.m2m.captcha.bean.Host;
import ma.m2m.captcha.dao.HostDao;
import ma.m2m.captcha.test.CaptchaTestQstDirector;
import ma.m2m.captcha.exception.HostNotFoundException;
import ma.m2m.captcha.exception.ImageNotFoundException;
import ma.m2m.captcha.exception.NoCaptchaTestIsStartedException;
import ma.m2m.captcha.exception.ValidationException;
import ma.m2m.captcha.test.TestImageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Random;

@Component
public class MXCaptchaAgent {


    public static final String TEXT_OBJECT = "test_object";
    public static final int REQUEST_TOKEN_LENGTH = 10;
    private HttpSession session;
    private final HostDao hostDao;

    private final CaptchaTestQstDirector captchaTestQstDirector;

    private final TestImageManager testImageManager;


    @Autowired
    public MXCaptchaAgent(HostDao hostDao, CaptchaTestQstDirector captchaTestQstDirector, TestImageManager testImageManager) {
        this.hostDao = hostDao;
        this.captchaTestQstDirector = captchaTestQstDirector;
        this.testImageManager = testImageManager;
    }

    public void passRequest(HttpServletRequest httpServletRequest){
        this.session = httpServletRequest.getSession(true);;
    }
    // user should only pass one test, so when a startTest request is received we override the old one
    public CaptchaTest startCaptchaTest(CaptchaTestRequest captchaTestRequest) throws HostNotFoundException, ValidationException {
        isSessionConfigured();
        if (validateHostIdentifier(captchaTestRequest.getHostIdentifier())){
            throw new HostNotFoundException(String.format("identifier '%s' not found", captchaTestRequest.getHostIdentifier()));
        }
        if (validateRequestTokenLayout(captchaTestRequest.getRequestToken())){
            throw new ValidationException(String.format("Invalid request token length '%s': it should be 10 character", captchaTestRequest.getRequestToken()));
        }

        CaptchaTest captchaTest = (CaptchaTest) session.getAttribute(TEXT_OBJECT);
        removeOldCaptchaTestQst(captchaTest);
        captchaTest = initiateCaptchaTest(captchaTestRequest);
        CaptchaTestQst captchaTestQst = buildCaptchaTestQst(captchaTest, 0);
        captchaTest.setCaptchaTestQst(captchaTestQst);
        session.setAttribute(TEXT_OBJECT, captchaTest);
        return captchaTest;

    }

    private void removeOldCaptchaTestQst(CaptchaTest captchaTest) {
        if ( captchaTest != null){
            testImageManager.removeImage(captchaTest.getCaptchaTestQst().getTestImageName());
        }
    }

    private CaptchaTest initiateCaptchaTest(CaptchaTestRequest captchaTestRequest) {
        CaptchaTest captchaTest = new CaptchaTest();
        long mills = System.currentTimeMillis() * 1000 + (new Random()).nextInt(1000);
        captchaTest.setRequestToken(captchaTestRequest.getRequestToken());
        captchaTest.setHostIdentifier(captchaTestRequest.getHostIdentifier());
        captchaTest.setId(mills);
        return captchaTest;
    }

    private CaptchaTestQst buildCaptchaTestQst(CaptchaTest captchaTest, int captchaTestQstId) {
        CaptchaTestQst captchaTestQst = captchaTestQstDirector.makeTestQst(captchaTest, captchaTestQstId);
        testImageManager.saveImage(captchaTestQst.getTestImageObject(), captchaTestQst.getTestImageName());
        captchaTestQst.setTestImageObject(null);
        return captchaTestQst;
    }

    private boolean validateRequestTokenLayout(String requestToken) {
        return requestToken.length() == REQUEST_TOKEN_LENGTH;
    }

    private boolean validateHostIdentifier(String hostIdentifier) {
        return getHostByHostIdentifier(hostIdentifier) == null;
    }

    private Host getHostByHostIdentifier(String hostIdentifier) {
        return hostDao.getByHostIdentifier(hostIdentifier);
    }

    private void isSessionConfigured() {
        if (this.session == null) throw new RuntimeException("Session is not set: You must pass the httpServletRequest object to retrieve the session");
    }

    public File getImage(String imageName) throws ImageNotFoundException {
        return testImageManager.readImage(imageName);
    }

    public CaptchaTest getCurrentCaptchaTest() throws NoCaptchaTestIsStartedException {
        isSessionConfigured();
        CaptchaTest captchaTest = (CaptchaTest) this.session.getAttribute(TEXT_OBJECT);
        if (captchaTest == null) throw new NoCaptchaTestIsStartedException("No captcha test started yet");
        return captchaTest;
    }

    public void resetTestQst() throws NoCaptchaTestIsStartedException {
        isSessionConfigured();
        CaptchaTest currentCaptchaTest = getCurrentCaptchaTest();
        CaptchaTestQst currentCaptchaTestQst = currentCaptchaTest.getCaptchaTestQst();
        testImageManager.removeImage(currentCaptchaTestQst.getTestImageName());
        CaptchaTestQst captchaTestQst = buildCaptchaTestQst(currentCaptchaTest, currentCaptchaTestQst.getId() + 1);
        currentCaptchaTest.setCaptchaTestQst(captchaTestQst);
        session.setAttribute(TEXT_OBJECT, currentCaptchaTest);
    }
}
