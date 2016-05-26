package org.multimediaprototype.auth.service;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


/**
 * Created by dx.yang on 15/11/24.
 */
@Service
public class AuthenticationProviderCustom implements AuthenticationProvider {

    private Logger logger = LogManager.getLogger(AuthenticationProviderCustom.class);

    @Autowired
    private SiteUserService siteUserService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        String username = token.getName();
        
        //从数据库找到的用户
        UserDetails userDetails = null;
        if(username != null) {
            userDetails = siteUserService.loadUserByUsername(username);
        }
        //
        if(userDetails == null) {
            logger.error("user name not found");
            throw new UsernameNotFoundException("用户名/密码无效");
        }else if (!userDetails.isEnabled()){
            logger.error("user disabled");
            throw new DisabledException("用户已被禁用");
        }else if (!userDetails.isAccountNonExpired()) {
            logger.error("account expired");
            throw new AccountExpiredException("账号已过期");
        }else if (!userDetails.isAccountNonLocked()) {
            logger.error("account locked");
            throw new LockedException("账号已被锁定");
        }else if (!userDetails.isCredentialsNonExpired()) {
            logger.error("credentials expired");
            throw new LockedException("凭证已过期");
        }
        // 数据库用户的密码
        String passwordInDB = userDetails.getPassword();

        // 用户输入的密码
        String rawPassword = (String)token.getCredentials();

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // 密码比较
        if (!passwordEncoder.matches(rawPassword, passwordInDB)) {
            logger.error("invalid username/passowrd");
            throw new BadCredentialsException("Invalid username/password");
        }
        
        

        //授权
        return new UsernamePasswordAuthenticationToken(userDetails, passwordInDB, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }
}
