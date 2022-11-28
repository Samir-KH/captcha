package ma.m2m.captcha.test;

import ma.m2m.captcha.bean.CaptchaTest;
import ma.m2m.captcha.bean.CaptchaTestQst;

public class CaptchaTestQstDirector {

    CaptchaTestQstBuilder captchaTestQstBuilder;

    public CaptchaTestQstDirector(CaptchaTestQstBuilder captchaTestQstBuilder) {
        this.captchaTestQstBuilder = captchaTestQstBuilder;
    }

    public void changeCaptchaTestQstBuilder(CaptchaTestQstBuilder captchaTestQstBuilder) {
        this.captchaTestQstBuilder = captchaTestQstBuilder;
    }

    public CaptchaTestQst makeTestQst(CaptchaTest test){
        captchaTestQstBuilder.rest();
        captchaTestQstBuilder.labelStep();
        captchaTestQstBuilder.answerStep();
        captchaTestQstBuilder.imageStep();
        CaptchaTestQst captchaTestQst = captchaTestQstBuilder.getResult();
        captchaTestQstBuilder.rest();
        return captchaTestQst;
    }
}
