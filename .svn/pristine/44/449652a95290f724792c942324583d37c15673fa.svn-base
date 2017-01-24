package com.polycom.honeycomb.rm.ui;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Tao Chen on 2016/11/23.
 */
@Controller public class ResourceShowController {
    private static org.slf4j.Logger LOGGER = LoggerFactory
            .getLogger(ResourceShowController.class);

    @RequestMapping("/resources.html") public String resourceForm(Model model) {
        Subject currentUser = SecurityUtils.getSubject();
        model.addAttribute("name", currentUser.getPrincipal().toString());
        return "resources";
    }
}
