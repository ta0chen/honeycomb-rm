package com.polycom.honeycomb.rm.api;

import com.polycom.honeycomb.rm.api.exception.ConstantsNotFoundException;
import com.polycom.honeycomb.rm.api.exception.IllegalRequestException;
import com.polycom.honeycomb.rm.api.exception.ResourcePoolNotFoundException;
import com.polycom.honeycomb.rm.domain.Constants;
import com.polycom.honeycomb.rm.domain.ResourcePool;
import com.polycom.honeycomb.rm.repository.ConstantsMangoRepository;
import com.polycom.honeycomb.rm.repository.ResourcePoolMangoRepository;
import com.polycom.honeycomb.rm.utils.CommonUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by Tao Chen on 2016/12/9.
 */
@RestController @RequestMapping(value = "/api/constants")
public class ConstantsController {
    private static org.slf4j.Logger LOGGER = LoggerFactory
            .getLogger(ConstantsController.class);
    @Autowired                           ResourcePoolMangoRepository resourcePoolRepository;
    @Autowired
                                         ConstantsMangoRepository    constantsMangoRepository;
    @Autowired @Qualifier("commonUtils")
                                         CommonUtils                 commonUtils;

    @RequiresRoles(value = { "ADMIN", "POOL_ADMIN" }, logical = Logical.OR)
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Constants createConstants(@RequestBody Constants constants) {
        List<ResourcePool> resourcePools = commonUtils
                .getAvailableResourcePoolForLoginUser();

        //find the target pool according to the input message
        Optional<ResourcePool> foundPool = resourcePools.stream()
                .filter(x -> constants.getPoolName().equals(x.getName()))
                .findAny();

        if (!foundPool.isPresent()) {
            throw new ResourcePoolNotFoundException(constants.getPoolName());
        }

        constants.setId(null);
        constantsMangoRepository.save(constants);

        ResourcePool resourcePool = foundPool.get();
        resourcePool.getConstants().add(constants);
        resourcePoolRepository.save(resourcePool);

        return constants;
    }

    @RequiresRoles(value = {
            "ADMIN", "POOL_ADMIN", "POOL_USER" }, logical = Logical.OR)
    @RequestMapping(method = RequestMethod.GET)
    public List<Constants> listConstants() {
        return commonUtils.getAvailableConstantsForLoginUser();
    }

    @RequiresRoles(value = {
            "ADMIN", "POOL_ADMIN" }, logical = Logical.OR)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteConstants(@PathVariable("id") String id) {
        List<Constants> constants = commonUtils
                .getAvailableConstantsForLoginUser();
        Optional<Constants> constant = commonUtils
                .findConstantsById(constants, id);

        if (!constant.isPresent()) {
            throw new ConstantsNotFoundException(id);
        }

        ResourcePool resourcePool = commonUtils
                .findResourcePoolByConstants(constant.get());

        if (resourcePool == null) {
            throw new ResourcePoolNotFoundException(resourcePool);
        }

        List<Constants> configsOfThisPool = resourcePool.getConstants();
        if (configsOfThisPool.contains(constant.get())) {
            configsOfThisPool.remove(constant.get());
            resourcePoolRepository.save(resourcePool);
        }
        constantsMangoRepository.delete(id);
    }

    @RequiresRoles(value = {
            "ADMIN", "POOL_ADMIN" }, logical = Logical.OR)
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public Constants updateConfigurations(@PathVariable("id") String id,
            @RequestBody Constants constants) {
        List<Constants> availConfigs = commonUtils
                .getAvailableConstantsForLoginUser();

        Optional<Constants> foundConfig = availConfigs.stream()
                .filter(x -> x.getId().equals(id)).findAny();

        if (!foundConfig.isPresent()) {
            throw new ConstantsNotFoundException(id);
        }

        constants.setId(id);
        constantsMangoRepository.save(constants);

        //handle pool name updating
        Constants legacyConfigs = foundConfig.get();
        String legacyPoolName = legacyConfigs.getPoolName();
        String updatePoolName = constants.getPoolName();

        if (!updatePoolName.equals(legacyPoolName)) {
            /*Check if the target pool is in the login user's pool list.
            * Otherwise, will reject updating the pool for the resource.*/
            String[] poolList = commonUtils.getLoginUsersPoolList();
            if (poolList != null) {
                if (!Arrays.asList(poolList).containsAll(Arrays.asList(
                        legacyPoolName,
                        updatePoolName))) {
                    throw new IllegalRequestException(updatePoolName);
                }
            }

            //Check the target pool id for its availability
            ResourcePool updateResourcePool = resourcePoolRepository
                    .findOne(updatePoolName);
            if (updateResourcePool == null) {
                throw new ResourcePoolNotFoundException(updatePoolName);
            }

            ResourcePool oldResourcePool = resourcePoolRepository
                    .findOne(legacyPoolName);

            List<Constants> configsOfLegacyPool = oldResourcePool
                    .getConstants();
            if (!configsOfLegacyPool.contains(constants)) {
                configsOfLegacyPool.remove(constants);
                resourcePoolRepository.save(oldResourcePool);
            }

            updateResourcePool.getConstants().add(constants);
            resourcePoolRepository.save(updateResourcePool);
        }
        return constants;
    }
}
