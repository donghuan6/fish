package com.nine.user.mapper.customize;

import com.nine.domain.user.Permit;
import com.nine.domain.user.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserJoinMapper {


    List<Role> selectRoleByUserId(@Param("userId") Long userId);

    List<Permit> selectPermitByUserId(@Param("userId") Integer userId);

    List<Permit> selectPermitByRoleId(@Param("roleId") Integer roleId);

}
