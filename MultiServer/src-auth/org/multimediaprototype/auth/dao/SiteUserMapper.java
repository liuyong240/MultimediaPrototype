package org.multimediaprototype.auth.dao;

import org.apache.ibatis.annotations.Param;
import org.multimediaprototype.auth.model.SiteUser;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Created by dx.yang on 15/11/13.
 */

@Repository
public interface SiteUserMapper {

    List<SiteUser> get(@Param("id") Long id, @Param("username") String username);

    Integer insert(@Param("username") String username, @Param("password") String password);

    Integer update(
            @Param("id") Long id,
            @Param("username") String username,
            @Param("password") String password,
            @Param("authorities") String authorities,
            @Param("enabled") Boolean enabled
    );
    
}
