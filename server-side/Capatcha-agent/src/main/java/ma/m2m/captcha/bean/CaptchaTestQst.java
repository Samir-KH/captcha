package ma.m2m.captcha.bean;

import java.awt.image.BufferedImage;

public class CaptchaTestQst {

    private int id;

    private String label;

    private String testImageName;

    private String expectedAnswer;

    private BufferedImage testImageObject;


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTestImageName() {
        return testImageName;
    }

    public void setTestImageName(String testImageName) {
        this.testImageName = testImageName;
    }

    public String getExpectedAnswer() {
        return expectedAnswer;
    }

    public void setExpectedAnswer(String expectedAnswer) {
        this.expectedAnswer = expectedAnswer;
    }

    public BufferedImage getTestImageObject() {
        return testImageObject;
    }

    public void setTestImageObject(BufferedImage testImageObject) {
        this.testImageObject = testImageObject;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
