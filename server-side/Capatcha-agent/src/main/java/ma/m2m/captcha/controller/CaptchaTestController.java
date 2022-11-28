package ma.m2m.captcha.controller;

import ma.m2m.captcha.bean.CaptchaTest;
import ma.m2m.captcha.test.CaptchaTestQstDirector;
import ma.m2m.captcha.test.DistortedImageTestQstBuilder;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/test")
public class CaptchaTestController {

    @GET
    @Path("/register")
    public String getId(){
        CaptchaTestQstDirector captchaTestQstDirector = new CaptchaTestQstDirector(new DistortedImageTestQstBuilder());
        captchaTestQstDirector.makeTestQst(new CaptchaTest());
        return "ok";
    }
}
