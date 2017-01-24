package com.polycom.honeycomb.rm.api;

import com.polycom.honeycomb.rm.api.exception.DefaultResourcePoolCanNotEditException;
import com.polycom.honeycomb.rm.api.exception.ResourceNotFoundException;
import com.polycom.honeycomb.rm.api.exception.ResourcePoolCanNotBeDeletedWithResourcesException;
import com.polycom.honeycomb.rm.api.exception.ResourcePoolNotFoundException;
import com.polycom.honeycomb.rm.domain.Resource;
import com.polycom.honeycomb.rm.domain.ResourcePool;
import com.polycom.honeycomb.rm.repository.ResourceMangoRepository;
import com.polycom.honeycomb.rm.repository.ResourcePoolMangoRepository;
import com.polycom.honeycomb.rm.utils.CommonUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Created by weigao on 14/9/2016.
 */
@RestController @RequestMapping(value = "/api/pools")
public class ResourcePoolController {
    private static org.slf4j.Logger LOGGER = LoggerFactory
            .getLogger(ResourcePoolController.class);
    @Autowired ResourcePoolMangoRepository resourcePoolRepository;
    @Autowired                           ResourceMangoRepository resourceRepository;
    @Autowired @Qualifier("commonUtils") CommonUtils             commonUtils;

    @RequiresRoles("ADMIN") @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResourcePool createResourcePool(
            @RequestBody ResourcePool newResourcePool) {
        newResourcePool.setId(null);
        resourcePoolRepository.save(newResourcePool);

        return newResourcePool;
    }

    @RequiresRoles(value = {
            "ADMIN", "POOL_ADMIN", "POOL_USER" }, logical = Logical.OR)
    @RequestMapping(method = RequestMethod.GET)
    public List<ResourcePool> listResourcePools() {
        List<ResourcePool> pools = commonUtils
                .getAvailableResourcePoolForLoginUser();

        return pools;
    }

    @RequiresRoles(value = { "ADMIN", "POOL_ADMIN" }, logical = Logical.OR)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResourcePool getOneResourcePool(@PathVariable("id") String id) {
        List<ResourcePool> pools = commonUtils
                .getAvailableResourcePoolForLoginUser();
        Optional<ResourcePool> foundResourcePool = commonUtils
                .findResourcePoolById(pools, id);

        if (!foundResourcePool.isPresent()) {
            throw new ResourcePoolNotFoundException(id);
        }

        return foundResourcePool.get();
    }

    @RequiresRoles("ADMIN")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteResourcePool(@PathVariable("id") String id) {
        if (id.equals(ResourcePool.DEFAULT_POOL_ID)) {
            throw new DefaultResourcePoolCanNotEditException();
        }

        //should block removal when there are resources in the pool
        ResourcePool pool = resourcePoolRepository.findOne(id);
        if (pool.getResources().stream().filter(x -> x != null).findAny()
                .isPresent()) {
            LOGGER.error(
                    "There are resources in the pool. So it cannot be removed.");
            throw new ResourcePoolCanNotBeDeletedWithResourcesException();
        }
        resourcePoolRepository.delete(id);
    }

    @RequiresUser
    @RequiresRoles(value = { "ADMIN", "POOL_ADMIN" }, logical = Logical.OR)
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/resources/{resourceId}")
    public ResourcePool addOneResourceToResourcePool(
            @PathVariable("id") String id,
            @PathVariable("resourceId") String resourceId) {
        List<ResourcePool> pools = commonUtils
                .getAvailableResourcePoolForLoginUser();
        Optional<ResourcePool> foundResourcePool = commonUtils
                .findResourcePoolById(pools, id);

        if (!foundResourcePool.isPresent()) {
            throw new ResourcePoolNotFoundException(id);
        }

        ResourcePool resourcePool = foundResourcePool.get();

        List<Resource> resources = commonUtils
                .getAvailableResourcesForLoginUser();
        Optional<Resource> foundResource = commonUtils
                .findResourceById(resources, resourceId);

        if (!foundResource.isPresent()) {
            throw new ResourceNotFoundException(resourceId);
        }

        Resource resource = foundResource.get();
        resourcePool.getResources().add(resource);
        resource.setPoolId(id);
        resourcePoolRepository.save(resourcePool);
        resourceRepository.save(resource);
        return resourcePool;
    }

    @RequiresRoles(value = {
            "ADMIN", "POOL_ADMIN", "POOL_USER" }, logical = Logical.OR)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/resources")
    public List<Resource> listResourcesOfOnePool(
            @PathVariable("id") String id) {
        List<Resource> resourceList = commonUtils
                .getAvailableResourcesForLoginUser();
        List<ResourcePool> resourcePools = commonUtils
                .getAvailableResourcePoolForLoginUser();
        Optional<ResourcePool> foundPool = commonUtils
                .findResourcePoolById(resourcePools, id);

        if (!foundPool.isPresent()) {
            throw new ResourcePoolNotFoundException(id);
        }

        List<Resource> resources = commonUtils
                .findResourceByPoolId(resourceList, id);
        return resources;
    }

    @RequiresRoles(value = { "ADMIN", "POOL_ADMIN" }, logical = Logical.OR)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}/resources/{resourceId}")
    public ResourcePool removeOneResourceFromResourcePool(
            @PathVariable("id") String id,
            @PathVariable("resourceId") String restourceId) {
        List<Resource> resourceList = commonUtils
                .getAvailableResourcesForLoginUser();
        List<ResourcePool> resourcePools = commonUtils
                .getAvailableResourcePoolForLoginUser();
        Optional<ResourcePool> foundPool = commonUtils
                .findResourcePoolById(resourcePools, id);

        if (!foundPool.isPresent()) {
            throw new ResourcePoolNotFoundException(id);
        }

        Optional<Resource> foundResource = commonUtils
                .findResourceById(resourceList, restourceId);
        if (!foundResource.isPresent()) {
            throw new ResourceNotFoundException(restourceId);
        }

        Resource resource = foundResource.get();
        List<Resource> resourcesOfThisPool = foundPool.get().getResources();

        if (resourcesOfThisPool.contains(resource)) {
            resourcesOfThisPool.remove(resource);
            resource.setPoolId(null);
            resourcePoolRepository.save(foundPool.get());
            resourceRepository.save(resource);
        }

        return foundPool.get();
    }
}
