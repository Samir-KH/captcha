package ma.m2m.captcha.test;



import ma.m2m.captcha.exception.ImageNotFoundException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class TestImageManager {


    public static final String JPEG = "JPEG";
    private final File testImagesFolder;

    public TestImageManager(File testImagesFolder) {
        this.testImagesFolder = testImagesFolder;
    }


    public void saveImage(BufferedImage image, String imageNameWithExtension) {
        File imageFile = new File(testImagesFolder, imageNameWithExtension);
        try {
            ImageIO.write(image, "png",imageFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public File readImage(String imageNameWithExtension) throws ImageNotFoundException {
        File imageFile = new File(testImagesFolder, imageNameWithExtension);
        if (imageFile.exists()) return imageFile;
        throw new ImageNotFoundException("The given test image name does not correspond to any exiting image: " + imageNameWithExtension);
    }

    public void removeImage(String imageNameWithExtension){
        File img = new File(testImagesFolder, imageNameWithExtension);
        img.delete();
    }
}
