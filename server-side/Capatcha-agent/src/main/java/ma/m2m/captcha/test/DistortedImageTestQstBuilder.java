package ma.m2m.captcha.test;

import ma.m2m.captcha.bean.CaptchaTestQst;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;

public class DistortedImageTestQstBuilder implements CaptchaTestQstBuilder {




    private static final String NOISE_IMAGE_PATH = "noise.png";
    private static final String CAPTCHA_QST_LABEL = "Please type de code contained in the image below";
    private CaptchaTestQst captchaTestQst;
    private String answer;

    private static final int LENGTH = 5;

    public void initStep() {
        captchaTestQst = new CaptchaTestQst();
    }

    public void labelStep() {
        captchaTestQst.setLabel(CAPTCHA_QST_LABEL);
    }

    public void answerStep() {
        this.answer = generateRandomString();
    }

    public void imageStep() {
        BufferedImage bufferedImage = textToDistortedImage(this.answer);
        captchaTestQst.setTestImageObject(bufferedImage);
    }

    private String generateRandomString() {
        Random random = new Random();
        StringBuilder randomString = new StringBuilder();
        int length = LENGTH;
        for (int i = 0; i < length; i++) {
            int captchaNumber = random.nextInt(60);
            int randomInt = 0;
            if (captchaNumber < 26) {
                randomInt = 65 + captchaNumber;
            } else if (captchaNumber < 52) {
                randomInt = 97 + (captchaNumber - 26);
            } else {
                randomInt = 48 + (captchaNumber - 52);
            }
            randomString.append((char) randomInt);
        }
        return randomString.toString();
    }

    public void rest() {
        this.answer = null;
        this.captchaTestQst = null;
    }

    public CaptchaTestQst getResult() {
        return this.captchaTestQst;
    }


    private BufferedImage textToDistortedImage(String displayCode) {
        BufferedImage bufferedImage;
        try {
            URI noiseImagePath = this.getClass().getClassLoader().getResource(NOISE_IMAGE_PATH).toURI();
            bufferedImage = ImageIO.read(new File(noiseImagePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        AffineTransform affineTransform = new AffineTransform();
        Font font = new Font("Kristen ITC", Font.BOLD, 80);
        FontMetrics fontMetrics = g2d.getFontMetrics();
        Random random = new Random();
        g2d.setColor(new Color(70, 70, 70, 190));
        int rotationAngle = random.nextInt(30) - 15;
        affineTransform.rotate(Math.toRadians(rotationAngle), 0, 0);
        Font rotatedFont = font.deriveFont(affineTransform);
        g2d.setFont(rotatedFont);
        g2d.drawString(displayCode, 10, 120);
        g2d.dispose();
        return bufferedImage;
    }
}
