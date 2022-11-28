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
    private CaptchaTestQst captchaTestQst;
    private String answer;

    private static final int MIN_LENGTH = 4;
    private static final int MAX_LENGTH = 7;

    public void labelStep() {
        captchaTestQst= new CaptchaTestQst();
    }

    public void imageStep() {
        textToImage(this.answer);

    }

    public void answerStep() {
        this.answer = generateRandomString();
        this.captchaTestQst.setExpectedAnswer(this.answer);
    }

    private String generateRandomString() {
        Random random = new Random();
        StringBuilder randomString = new StringBuilder();
        int length = random.nextInt(MAX_LENGTH - MIN_LENGTH +1) + MIN_LENGTH;
        for (int i = 0; i < length; i++){
            int captchaNumber = random.nextInt(60);
            int randomInt = 0;
            if (captchaNumber < 26) {
                randomInt = 65 + captchaNumber;
            }
            else if (captchaNumber < 52){
                randomInt = 97 + (captchaNumber - 26);
            }
            else {
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

    private BufferedImage textToImage(String displayCode) {
        String text = displayCode;
        //BufferedImage img = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
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
        AffineTransform affineTransform = new AffineTransform();
        Font font = new Font("Pristina", Font.BOLD, 90);
        FontMetrics fontMetrics = g2d.getFontMetrics();
        Random random = new Random();
        g2d.setColor(new Color(98, 92, 92));
        int xi = 10;
        for (int i = 0; i < displayCode.length(); i++) {
            String charCode = String.valueOf(displayCode.charAt(i));
            int rotationAngle = random.nextInt(14) - 7;
            affineTransform.rotate(Math.toRadians(rotationAngle), 0, 0);
            Font rotatedFont = font.deriveFont(affineTransform);
            g2d.setFont(rotatedFont);
            g2d.drawString(charCode, xi, 100);
            xi += 70;
        }

        int w = 1000;
        int h = 1000;

        g2d.setStroke(new BasicStroke(3f));
        g2d.setPaint(new Color(44, 44, 44));
        for (int i = 0; i < 8; i++) {
            int x1 = -10;
            int y1 = random.nextInt(200);
            int x2 = 400;
            int y2 = random.nextInt(200);
            g2d.drawLine(x1, y1, x2, y2);
        }
        g2d.dispose();
        return bufferedImage;
    }
}
