package org.multimediaprototype.auth.controller;

import org.multimediaprototype.auth.service.SiteUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by dx.yang on 15/11/24.
 */


/**
 * 注册登录页面
 */
@Controller
@RequestMapping("/auth")
public class AuthViewController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/auth/login");
        return view;
    }
    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public ModelAndView signup() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/auth/signup");
        return view;
    }
}
