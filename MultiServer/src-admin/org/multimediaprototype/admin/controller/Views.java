package org.multimediaprototype.admin.controller;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.multimediaprototype.auth.model.SiteUserDetail;
import org.multimediaprototype.auth.service.SiteUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


/**
 * Created by dx.yang on 15/11/12.
 */

/*
*   页面controller
* */
@Controller
@RequestMapping("/admin")
public class Views {

    @Autowired
    private SiteUserService siteUserService;

    private static final Logger logger = LogManager.getLogger(Views.class);

    // 主页面入口
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getIndex() {
        ModelAndView m = new ModelAndView();
        m.setViewName("/admin/index");

        // 获取当前登录用户Detail
        SiteUserDetail user = siteUserService.getCurrentUser();

        m.addObject("user", user.getUsername());
        return m;
    }
}
