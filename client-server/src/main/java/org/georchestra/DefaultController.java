
package org.georchestra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@Controller
@RequestMapping("/**")
public class DefaultController {

    @Autowired
    private ServletContext context;

    @Autowired
    private Test tests;
    private int responseCode;
    private String responseType;
    private String response;

    @RequestMapping()
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<title>Example</title>" +
                "<body bgcolor=FFFFFF>Hello " + path + "</body></html>");
        out.close();
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
