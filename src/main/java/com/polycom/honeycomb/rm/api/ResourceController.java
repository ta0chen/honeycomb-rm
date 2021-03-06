package com.polycom.honeycomb.rm.api;

import com.polycom.honeycomb.rm.api.exception.IllegalRequestException;
import com.polycom.honeycomb.rm.api.exception.ResourceNotFoundException;
import com.polycom.honeycomb.rm.api.exception.ResourcePoolNotFoundException;
import com.polycom.honeycomb.rm.domain.Resource;
import com.polycom.honeycomb.rm.domain.ResourcePool;
import com.polycom.honeycomb.rm.repository.ResourceMangoRepository;
import com.polycom.honeycomb.rm.repository.ResourcePoolMangoRepository;
import com.polycom.honeycomb.rm.repository.UserMongoRepository;
import com.polycom.honeycomb.rm.utils.CommonUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by weigao on 6/9/2016.
 */
@CrossOrigin @RestController @RequestMapping(value = "/api/resources")
public class ResourceController {
    private static org.slf4j.Logger LOGGER = LoggerFactory
            .getLogger(ResourceController.class);
    @Autowired                           UserMongoRepository         userMongoRepository;
    @Autowired
                                         ResourceMangoRepository     resourceRepository;
    @Autowired
                                         ResourcePoolMangoRepository resourcePoolRepository;
    @Autowired @Qualifier("commonUtils")
                                         CommonUtils                 commonUtils;

    @RequiresRoles(value = { "ADMIN", "POOL_ADMIN" }, logical = Logical.OR)
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Resource createResource(@RequestBody Resource newResource) {
        List<ResourcePool> resourcePools = commonUtils
                .getAvailableResourcePoolForLoginUser();

        //find the target pool according to the input message
        Optional<ResourcePool> foundPool = resourcePools.stream()
                .filter(x -> newResource.getPoolId().equals(x.getName()))
                .findAny();

        if (!foundPool.isPresent()) {
            throw new ResourcePoolNotFoundException(newResource.getPoolId());
        }

        newResource.setId(null);
        resourceRepository.save(newResource);

        ResourcePool resourcePool = foundPool.get();
        resourcePool.getResources().add(newResource);
        resourcePoolRepository.save(resourcePool);

        return newResource;
    }

    @RequiresRoles(value = {
            "ADMIN", "POOL_ADMIN", "POOL_USER" }, logical = Logical.OR)
    @RequestMapping(method = RequestMethod.GET)
    public Page<Resource> listResources(Pageable pageable) {
        List<Resource> resources = commonUtils
                .getAvailableResourcesForLoginUser();
        return new PageImpl<Resource>(resources);
    }

    @RequiresRoles(value = {
            "ADMIN", "POOL_ADMIN", "POOL_USER" }, logical = Logical.OR)
    @RequestMapping(method = RequestMethod.GET, value = "/type={resourceType}")
    public List<Resource> listResourcesByItsType(
            @PathVariable("resourceType") String resourceType) {
        List<Resource> resources = commonUtils
                .getAvailableResourcesForLoginUser();

        //find the resources whose resource type is the specified
        List<Resource> foundResources = commonUtils
                .findResourceAccordingToItsType(resources, resourceType);
        if (foundResources == null) {
            throw new ResourceNotFoundException(resourceType);
        }
        return foundResources;
    }

    @RequiresRoles(value = {
            "ADMIN", "POOL_ADMIN", "POOL_USER" }, logical = Logical.OR)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public Resource getOneResource(@PathVariable("id") String id) {
        List<Resource> resources = commonUtils
                .getAvailableResourcesForLoginUser();
        Optional<Resource> resource = commonUtils
                .findResourceById(resources, id);

        if (!resource.isPresent()) {
            throw new ResourceNotFoundException(id);
        }

        return resource.get();
    }

    @RequiresRoles(value = {
            "ADMIN", "POOL_ADMIN" }, logical = Logical.OR)
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public Resource updateResource(@PathVariable("id") String id,
            @RequestBody Resource updateResource) {
        List<Resource> resources = commonUtils
                .getAvailableResourcesForLoginUser();
        Optional<Resource> foundResource = commonUtils
                .findResourceById(resources, id);

        if (!foundResource.isPresent()) {
            throw new ResourceNotFoundException(id);
        }

        Resource resource = foundResource.get();
        String oldPoolId = resource.getPoolId();
        String updatePoolId = updateResource.getPoolId();

        updateResource.setId(id);
        resourceRepository.save(updateResource);

        if (!oldPoolId.equals(updatePoolId)) {
            /*Check if the target pool is in the login user's pool list.
            * Otherwise, will reject updating the pool for the resource.*/
            String[] poolList = commonUtils.getLoginUsersPoolList();
            if (poolList != null) {
                if (!Arrays.asList(poolList)
                        .containsAll(Arrays.asList(oldPoolId, updatePoolId))) {
                    throw new IllegalRequestException(updateResource);
                }
            }

            //Check the target pool id for its availability
            ResourcePool updateResourcePool = resourcePoolRepository
                    .findOne(updatePoolId);
            if (updateResourcePool == null) {
                throw new ResourcePoolNotFoundException(updatePoolId);
            }

            ResourcePool oldResourcePool = resourcePoolRepository
                    .findOne(oldPoolId);
            List<Resource> resourcesOfOldPool = oldResourcePool.getResources();
            if (resourcesOfOldPool.contains(updateResource)) {
                resourcesOfOldPool.remove(updateResource);
                resourcePoolRepository.save(oldResourcePool);
            }

            updateResourcePool.getResources().add(updateResource);
            resourcePoolRepository.save(updateResourcePool);
        }

        return updateResource;
    }

    @RequiresRoles(value = {
            "ADMIN", "POOL_ADMIN" }, logical = Logical.OR)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteResource(@PathVariable("id") String id) {
        List<Resource> resources = commonUtils
                .getAvailableResourcesForLoginUser();
        Optional<Resource> resource = commonUtils
                .findResourceById(resources, id);

        if (!resource.isPresent()) {
            throw new ResourceNotFoundException(id);
        }

        ResourcePool resourcePool = commonUtils
                .findResourcePoolByResource(resource.get());

        if (resourcePool == null) {
            throw new ResourcePoolNotFoundException(resourcePool);
        }

        List<Resource> resourcesOfThisPool = resourcePool.getResources();
        if (resourcesOfThisPool.contains(resource.get())) {
            resourcesOfThisPool.remove(resource.get());
            resourcePoolRepository.save(resourcePool);
        }
        resourceRepository.delete(id);
    }
}
