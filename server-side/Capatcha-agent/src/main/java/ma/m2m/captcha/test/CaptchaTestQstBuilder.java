package ma.m2m.captcha.test;

import ma.m2m.captcha.bean.CaptchaTest;
import ma.m2m.captcha.bean.CaptchaTestQst;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface CaptchaTestQstBuilder {
    void initStep();
    void labelStep();
    void answerStep();
    void imageStep();
    void rest();
    CaptchaTestQst getResult();
}
