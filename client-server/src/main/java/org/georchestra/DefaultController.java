
package org.georchestra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/**")
public class DefaultController {

    @Autowired
    private ServletContext context;

    @Autowired
    private Test tests;

    private Map<String, Response> responses;

    @PostConstruct
    public void init(){
        this.responses = new HashMap<String, Response>();
    }

    @RequestMapping()
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Response configuredResponse = this.responses.get(request.getPathInfo());
        if(configuredResponse != null){
            // Predefined response, just send response
            response.setContentType(configuredResponse.getContentType());
            response.setStatus(configuredResponse.getResponseCode());
            PrintWriter out = response.getWriter();
            out.print(configuredResponse.getResponse());
            out.close();
        } else {
            // No response configured, send a page with some informations
            response.setContentType("text/html");
            response.setStatus(200);
            PrintWriter out = response.getWriter();
            out.print("<h1>Page information</h1>");
            out.print("<h2>Headers received</h2><ul id='headers'>");
            Enumeration<String> headerNames = request.getHeaderNames();
            while(headerNames.hasMoreElements()){
                String headerName = headerNames.nextElement();
                Enumeration<String> values = request.getHeaders(headerName);
                while(values.hasMoreElements()){
                    String value = values.nextElement();
                    out.print("<li>" + headerName + ": " + value + "</li>");
                }
            }
            out.print("</ul>");
            out.print("<h2>Path</h2>");
            out.print("<div id='path'>" + request.getPathInfo() + "</div>");
            out.close();
        }
    }

    public void addResponse(String path, Response response){
        this.responses.put(path, response);
    }

    public void removeResponse(String path){
        this.responses.remove(path);
    }

    public void clearResponses(){
        this.responses.clear();
    }
}
