package ma.m2m.captcha.controller;

import ma.m2m.captcha.exception.*;
import ma.m2m.captcha.MXCaptchaAgent;
import org.springframework.beans.factory.annotation.Autowired;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;

import ma.m2m.captcha.bean.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Path("/agent")
public class CaptchaAgentController {

    private static final String HOST_NOT_FOUND = "The given host identifier doesn't correspond to any host";

    private final MXCaptchaAgent mxCaptchaAgent;


    @Autowired
    public CaptchaAgentController(MXCaptchaAgent mxCaptchaAgent) {
        this.mxCaptchaAgent = mxCaptchaAgent;
    }


    @POST
    @Path("/test.start")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getId(@Context HttpServletRequest httpServletRequest, CaptchaTestRequest captchaTestRequest) {
        mxCaptchaAgent.passRequest(httpServletRequest);
        try {
            CaptchaTest captchaTest = mxCaptchaAgent.startCaptchaTest(captchaTestRequest);
            CaptchaTestQst captchaTestQst = captchaTest.getCaptchaTestQst();
            CaptchTestQstResponse captchaTestResponse = captchaTestQstResponseDTO(captchaTest, captchaTestQst);
            return Response.ok(captchaTestResponse).build();
        } catch (HostNotFoundException e) {
            ResponseError responseError = new ResponseError();
            responseError.setSubject(captchaTestRequest);
            responseError.setMessage(HOST_NOT_FOUND);
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity(responseError).build());
        } catch (ValidationException e) {
            ResponseError responseError = new ResponseError();
            responseError.setSubject(captchaTestRequest);
            responseError.setMessage(e.getMessage());
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity(responseError).build());
        } catch (NullCaptchaTestRequest e) {
            ResponseError responseError = new ResponseError();
            responseError.setSubject(captchaTestRequest);
            responseError.setMessage("Null captcha test request");
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity(responseError).build());

        }

    }

    @GET
    @Path("/test.image")
    @Produces({"images/*", "application/json"})
    public Response getTestImage(@QueryParam("name") String imageName) {
        try {
            File imageFile = mxCaptchaAgent.getImage(imageName);
            String mimeType = new MimetypesFileTypeMap().getContentType(imageFile);
            return Response.ok(imageFile, mimeType).build();
        } catch (ImageNotFoundException e) {
            ResponseError responseError = new ResponseError();
            responseError.setSubject(imageName);
            responseError.setMessage(e.getMessage());
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity(responseError).build());
        }

    }

    @GET
    @Path("/test.qst.reset")
    @Produces(MediaType.APPLICATION_JSON)
    public Response resetTestQst(@Context HttpServletRequest httpServletRequest) {
        mxCaptchaAgent.passRequest(httpServletRequest);
        try {
            mxCaptchaAgent.resetTestQst();
            CaptchaTest captchaTest = mxCaptchaAgent.getCurrentCaptchaTest();
            CaptchaTestQst captchaTestQst = captchaTest.getCaptchaTestQst();
            CaptchTestQstResponse captchaTestResponse = captchaTestQstResponseDTO(captchaTest, captchaTestQst);
            return Response.ok(captchaTestResponse).build();
        } catch (NoCaptchaTestIsStartedException e) {
            ResponseError responseError = new ResponseError();
            responseError.setMessage(e.getMessage());
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity(responseError).build());
        }
    }

    @GET
    @Path("/test.qst.response")
    @Produces(MediaType.APPLICATION_JSON)
    public Response captchaTestQstResponse(@Context HttpServletRequest httpServletRequest, @QueryParam("response") String userResponse) {
        mxCaptchaAgent.passRequest(httpServletRequest);
        try {
            if (mxCaptchaAgent.isResponseCorrect(userResponse)){
                String hashedToken = mxCaptchaAgent.responseToTestQst();
                Map<String, String> response = new HashMap<>();
                response.put("hashedToken", hashedToken);
                return Response.ok(response).build();
            }
            return resetTestQst(httpServletRequest);
        } catch (NoCaptchaTestIsStartedException e) {
            ResponseError responseError = new ResponseError();
            responseError.setMessage(e.getMessage());
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity(responseError).build());

        }
    }

    private CaptchTestQstResponse captchaTestQstResponseDTO(CaptchaTest captchaTest, CaptchaTestQst captchaTestQst) {
        CaptchTestQstResponse captchaTestResponse = new CaptchTestQstResponse();
        captchaTestResponse.setTestId(String.valueOf(captchaTest.getId()));
        captchaTestResponse.setImageLabel(captchaTestQst.getLabel());
        captchaTestResponse.setImageName(captchaTestQst.getTestImageName());
        return captchaTestResponse;
    }




}
