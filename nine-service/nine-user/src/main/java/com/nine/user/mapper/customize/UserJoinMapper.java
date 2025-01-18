package com.nine.user.mapper.customize;

import com.nine.user.dao.Permit;
import com.nine.user.dao.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserJoinMapper {


    List<Role> selectRoleByUserId(@Param("userId") Long userId);

    List<Permit> selectPermitByUserId(@Param("userId") Long userId);

    List<Permit> selectPermitByRoleId(@Param("roleId") Integer roleId);

}
