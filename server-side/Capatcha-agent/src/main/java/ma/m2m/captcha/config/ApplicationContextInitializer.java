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
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
public class ApplicationContextInitializer implements WebApplicationInitializer {

    private final String TEST_IMAGES_FOLDER_PATH = "C:\\CaptchaAgent\\test";
    private  File testImagesFolder = new File(TEST_IMAGES_FOLDER_PATH);

    public ApplicationContextInitializer() {
        if(!testImagesFolder.exists()) {
            testImagesFolder.mkdirs();
        }
    }

    public void onStartup(ServletContext servletContext) throws ServletException {

        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        servletContext.addListener(new ContextLoaderListener(context));
        servletContext.addListener(new CaptchaSessionListener(new TestImageManager(testImagesFolder)));
        servletContext.setInitParameter("contextConfigLocation", "ma.m2m.captcha");
        DelegatingFilterProxy corsFilter = new DelegatingFilterProxy("corsFilter");
        servletContext.addFilter("corsFilter",  corsFilter).addMappingForUrlPatterns(null, false, "/*");
    }

    @Bean
    public CaptchaTestQstDirector getCaptchaTestQstDirector(TestImageManager testImageManager){
        return new CaptchaTestQstDirector(new DistortedImageTestQstBuilder(), testImageManager);
    }

    @Bean
    public TestImageManager getTestImageManager(){
        return new TestImageManager(testImagesFolder);
    }

    @Bean(name = "corsFilter")
    public CORSFilter getCORSFilter(){
        return new CORSFilter();
    }

}