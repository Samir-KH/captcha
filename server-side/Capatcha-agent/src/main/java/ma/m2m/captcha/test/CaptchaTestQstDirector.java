package ma.m2m.captcha.test;

import ma.m2m.captcha.bean.CaptchaTest;
import ma.m2m.captcha.bean.CaptchaTestQst;

import java.awt.image.BufferedImage;

public class CaptchaTestQstDirector {

    private CaptchaTestQstBuilder captchaTestQstBuilder;
    private final TestImageManager testImageManager;

    public CaptchaTestQstDirector(CaptchaTestQstBuilder captchaTestQstBuilder, TestImageManager testImageManager) {
        this.captchaTestQstBuilder = captchaTestQstBuilder;
        this.testImageManager = testImageManager;
    }

    public void changeCaptchaTestQstBuilder(CaptchaTestQstBuilder captchaTestQstBuilder) {
        this.captchaTestQstBuilder = captchaTestQstBuilder;
    }

    public CaptchaTestQst makeTestQst(CaptchaTest captchaTest, int captchaTestQstId) {
        captchaTestQstBuilder.rest();
        captchaTestQstBuilder.initStep();
        captchaTestQstBuilder.labelStep();
        captchaTestQstBuilder.answerStep();
        captchaTestQstBuilder.imageStep();
        CaptchaTestQst captchaTestQst = captchaTestQstBuilder.getResult();
        captchaTestQst.setId(captchaTestQstId);
        String imageWithExtension = "test_" + captchaTest.getId() +""+ captchaTestQstId + "."+"png";
        captchaTestQst.setTestImageName(imageWithExtension);
        captchaTestQstBuilder.rest();
        return captchaTestQst;
    }
}
