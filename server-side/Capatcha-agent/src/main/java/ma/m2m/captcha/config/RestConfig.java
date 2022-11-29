package ma.m2m.captcha.config;


import ma.m2m.captcha.controller.CaptchaAgentController;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/captcha")
public class RestConfig extends Application {
    public Set<Class<?>> getClasses() {
        return new HashSet<Class<?>>(
                Arrays.asList(
                        CaptchaAgentController.class));
    }
}
