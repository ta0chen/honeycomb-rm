package com.polycom.honeycomb.rm.ui;

import com.polycom.honeycomb.rm.repository.UserMongoRepository;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Created by Tao Chen on 16/10/21.
 */
@Controller public class LoginController {
    private static org.slf4j.Logger LOGGER = LoggerFactory
            .getLogger(LoginController.class);
    @Autowired UserMongoRepository userMongoRepository;

    @RequestMapping("/index.html") public String loginForm() {
        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(String userName,
            String password,
            Model model,
            RedirectAttributes redirectAttributes) {
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userName,
                                                                password);
        try {
            LOGGER.info("Begin authenticate the input user " + userName);
            currentUser.login(token);
            LOGGER.info("Finished authentication for the user " + userName);
        } catch (UnknownAccountException uae) {
            LOGGER.info("Failed to authenticate user " + userName
                                + " since it is not available in the system.");
            redirectAttributes.addFlashAttribute("message", "unknown account");
        } catch (IncorrectCredentialsException ice) {
            LOGGER.info("Failed to authenticate user " + userName
                                + " since the password " + password
                                + " is not correct");
            redirectAttributes.addFlashAttribute("message", "wrong password");
        } catch (LockedAccountException lae) {
            LOGGER.info("Failed to authenticate user " + userName
                                + " since it is locked.");
            redirectAttributes.addFlashAttribute("message", "user locked");
        } catch (ExcessiveAttemptsException eae) {
            LOGGER.info("Failed to authenticate user " + userName
                                + " since too many authentication failure.");
            redirectAttributes.addFlashAttribute("message",
                                                 "too many authentication failure");
        } catch (AuthenticationException ae) {
            LOGGER.info("User " + userName
                                + " authentication failed with exception");
            ae.printStackTrace();
            redirectAttributes
                    .addFlashAttribute("message", "incorrect credential");
        }

        if (currentUser.isAuthenticated()) {
            LOGGER.info("User " + userName
                                + " authentication passed. Will turn to the resource page soon.");
            model.addAttribute("name", userName);
            return "resources";
        } else {
            token.clear();
            return "index";
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public String logout() {
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
        return "redirect:/index.html";
    }
}
