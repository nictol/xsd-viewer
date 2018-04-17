package com.emc.xsdviewer.webapp;

/**
 * Created by ivan on 20.07.17.
 */

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LandingPageController {

    @RequestMapping("/a")
    public String showHomePage(Map<String, Object> model) {
        return "static/index.html";
    }
}
