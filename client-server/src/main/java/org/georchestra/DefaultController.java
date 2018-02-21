
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


@Controller
@RequestMapping("/**")
public class DefaultController {

    @Autowired
    private ServletContext context;

    @Autowired
    private Test tests;
    private String response;

    @RequestMapping(method = RequestMethod.POST)
    public void handlePOSTRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(this.response);
    }
    @RequestMapping(method = RequestMethod.GET)
    public void handleGETRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(this.response);
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
