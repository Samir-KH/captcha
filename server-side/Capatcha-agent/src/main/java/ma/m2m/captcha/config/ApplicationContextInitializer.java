package ma.m2m.captcha.config;

import ma.m2m.captcha.test.CaptchaTestQstDirector;
import ma.m2m.captcha.test.DistortedImageTestQstBuilder;
import ma.m2m.captcha.test.TestImageManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.File;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
public class ApplicationContextInitializer implements WebApplicationInitializer {

    private static final String TEST_IMAGES_FOLDER_PATH = "C:\\MXCaptcha\\tests";

    public void onStartup(ServletContext servletContext) throws ServletException {

        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();

        servletContext.addListener(new ContextLoaderListener(context));
        servletContext.setInitParameter("contextConfigLocation", "ma.m2m.captcha");
    }

    @Bean
    public CaptchaTestQstDirector getCaptchaTestQstDirector(TestImageManager testImageManager){
        return new CaptchaTestQstDirector(new DistortedImageTestQstBuilder(), testImageManager);
    }

    @Bean
    public TestImageManager getTestImageManager(){
        File testImagesFolder = new File(TEST_IMAGES_FOLDER_PATH);
        return new TestImageManager(testImagesFolder);
    }
}