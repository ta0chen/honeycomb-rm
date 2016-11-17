package com.polycom.honeycomb.rm.api;

import com.polycom.honeycomb.rm.domain.Role;
import com.polycom.honeycomb.rm.repository.RoleMongoRepository;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Tao Chen on 2016/11/8.
 */
@RestController @RequestMapping(value = "/api/roles")
public class RoleController {
    @Autowired RoleMongoRepository roleMongoRepository;

    @RequiresRoles("ADMIN") @RequestMapping(method = RequestMethod.GET)
    public List<Role> listRoles() {
        List<Role> roles = roleMongoRepository.findAll();
        return roles;
    }
}
