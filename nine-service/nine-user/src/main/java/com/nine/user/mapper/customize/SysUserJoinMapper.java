package com.nine.user.mapper.customize;

import com.nine.user.dao.SysPermit;
import com.nine.user.dao.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserJoinMapper {

    List<SysRole> selectAllRole();

    List<SysPermit> selectAllPermit();

    List<SysRole> selectRoleByUserId(@Param("userId") Long userId);

    List<SysPermit> selectPermitByUserId(@Param("userId") Long userId);

    List<SysPermit> selectPermitByRoleId(@Param("roleId") Integer roleId);

}
