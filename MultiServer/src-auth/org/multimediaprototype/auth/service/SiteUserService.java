package org.multimediaprototype.auth.service;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.multimediaprototype.auth.dao.SiteUserMapper;
import org.multimediaprototype.auth.model.SiteUser;
import org.multimediaprototype.auth.model.SiteUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.web.context.request.*;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by dx.yang on 15/11/13.
 */

@Service(value = "SiteUserService")
public class SiteUserService implements UserDetailsService {

    private Logger logger = LogManager.getLogger(SiteUserService.class);

    @Autowired
    private SiteUserMapper siteUserMapper;

    // 用户名和密码校验
    private Pattern usernamePattern = Pattern.compile("^[a-zA-Z0-9_-]{6,20}$");
    private Pattern passwordPattern = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})");

    private boolean validator(String needCheck, Pattern checker) {
        Matcher matcher = checker.matcher(needCheck);
        return matcher.matches();
    }

    /**
     * <h2>获取当前登录用户detail信息</h2>
     * <p>获取当前登录用户的detial, 详见{@link SiteUserDetail}</p>
     * @return SiteUserDetail
     *
     * <br/>
     *
     * example
     * <pre class="brush:java">
     *
     * // 获取当前登录用户
     * SiteUserDetail user = siteUserService.getCurrentUser();
     * // 获取用户名
     * String username = user.getName();
     * // 获取用户id
     * Long userId = user.getId();
     *
     * </pre>
     */
    public SiteUserDetail getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       
        if (auth.getName().equals("anonymousUser")) {
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ANONYMOUSUSER");
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(authority);
            return new SiteUserDetail(1L, "test", "passwd", true, true, true, true, authorities);
        } else {
            return (SiteUserDetail) auth.getPrincipal();
        }

    }

    /**
     * 获取用户列表
     * 两个参数只需传一个
     * @param id 用户id
     * @param name 用户名
     * @return SiteUser的list
     */
    public List<SiteUser> get(Long id, String name) {
        return siteUserMapper.get(id, name);
    }

    /**
     * 新用户注册
     * @param username 必填
     * @param password 必填
     * @return
     * @throws Exception
     */
    public Integer insert(String username, String password) throws Exception {


        if (username == null || password == null) {
            throw new Exception("用户名和密码不能为空!");
        }

        if (!validator(username, usernamePattern)) {
            throw new Exception("用户名不符合规范!");
        }

        if (!validator(password, passwordPattern)) {
            throw new Exception("密码不符合规范!");
        }

        if (get(null, username).size() != 0) {
            throw new Exception("用户名以被占用!");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);
        return siteUserMapper.insert(username, encodedPassword);
    }


    public Integer update(Long id, String username, String password, String authorities, Boolean enabled) throws Exception {
        if (id == null) {
            throw new Exception("用户id不能为空");
        }
        if (username != null && !validator(username, usernamePattern)) {
            throw new Exception("用户名不符合规范!");
        }

        if (password != null && !validator(password, passwordPattern)) {
            throw new Exception("密码不符合规范!");
        }

        if (username != null && get(null, username).size() != 0) {
            throw new Exception("用户名以被占用!");
        }

        if (password != null) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            password = passwordEncoder.encode(password);
        }
        return siteUserMapper.update(id, username, password, authorities, enabled);

    }

    /**
     * 根据用户名获取UserDetails
     * UserDetailsService接口方法, 用于AuthenticationProvider获取用户基本信息
     * @param username
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        List<SiteUser> list = siteUserMapper.get(null, username);

        if (list.size() == 0) {
            return null;
        }

        SiteUser user = list.get(0);
        String authoritiesString = user.getAuthorities();
        String[] authorities = StringUtils.split(authoritiesString, ",");
        Collection<GrantedAuthority> auths = new ArrayList<>();
        for (int i = 0; i < authorities.length; i++) {
            SimpleGrantedAuthority auth = new SimpleGrantedAuthority(authorities[i]);
            auths.add(auth);
        }
        String password = user.getPassword();
        Boolean enabled = user.isEnabled();
        //User authUserModel = new User(username, password, true, true, true, enabled, auths);
        SiteUserDetail authUserModel = new SiteUserDetail(user.getId(), username, password, true, true, true, enabled, auths);

        return authUserModel;
    }

}
