package com.innervix.model3d.domain.auth.mapper;


import com.innervix.model3d.domain.auth.model.AdminUser;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

public interface AdminMapper {
    Optional<AdminUser> findByUsername(@Param("username") String username);

    boolean existsAny();

    int create(@Param("username") String username,
               @Param("passwordHash") String passwordHash,
               @Param("displayName") String displayName);
}
