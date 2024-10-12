package com.nine.user.service.base;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nine.domain.user.Permit;
import com.nine.user.mapper.PermitMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class PermitService extends ServiceImpl<PermitMapper, Permit> implements IPermitService {

    private final PermitMapper permitMapper;

}
