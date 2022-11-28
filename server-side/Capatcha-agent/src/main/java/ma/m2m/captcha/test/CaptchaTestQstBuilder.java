package ma.m2m.captcha.test;

import ma.m2m.captcha.bean.CaptchaTestQst;

public interface CaptchaTestQstBuilder {
    void labelStep();
    void imageStep();
    void answerStep();
    void rest();
    CaptchaTestQst getResult();
}
