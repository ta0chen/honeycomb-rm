package com.polycom.honeycomb.rm.apitests;

import com.polycom.honeycomb.rm.domain.Resource;
import com.polycom.honeycomb.rm.domain.ResourcePool;
import com.polycom.honeycomb.rm.domain.User;
import com.polycom.honeycomb.rm.pool.PoolOperations;
import com.polycom.honeycomb.rm.resource.ResourceOperations;
import com.polycom.honeycomb.rm.user.UserOperations;
import com.polycom.http.HttpException;
import jodd.http.HttpResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by Tao Chen on 2016/11/16.
 */
@RunWith(SpringJUnit4ClassRunner.class) @DirtiesContext
public class ResourceTests {
    private String             host               = "localhost";
    private String             admin              = "admin";
    private String             password           = "Polycom123";
    private PoolOperations     poolOperations     = new PoolOperations(host);
    private UserOperations     userOperations     = new UserOperations(host);
    private ResourceOperations resourceOperations = new ResourceOperations(host);

    @Rule public final ExpectedException exception = ExpectedException.none();

    @Before public void setUp() {
        //Create two pools
        HttpResponse response = poolOperations
                .createResourcePool(admin, password, "pool1", "pool1 desc");
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());

        response = poolOperations
                .createResourcePool(admin, password, "pool2", "pool2 desc");
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());

        response = userOperations.createUser(admin,
                                             password,
                                             "pool1_admin",
                                             "pool1",
                                             "admin",
                                             "Polycom123",
                                             new String[] { "POOL_ADMIN" },
                                             new String[] { "pool1" });
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());

        response = userOperations.createUser(admin,
                                             password,
                                             "pool1_user",
                                             "pool1",
                                             "admin",
                                             "Polycom123",
                                             new String[] { "POOL_USER" },
                                             new String[] { "pool1" });
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());

        response = userOperations.createUser(admin,
                                             password,
                                             "pool2_admin",
                                             "pool2",
                                             "admin",
                                             "Polycom123",
                                             new String[] { "POOL_ADMIN" },
                                             new String[] { "pool2" });
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());

        response = userOperations.createUser(admin,
                                             password,
                                             "pool2_user",
                                             "pool2",
                                             "admin",
                                             "Polycom123",
                                             new String[] { "POOL_USER" },
                                             new String[] { "pool2" });
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());
    }

    @Test public void adminCanCreateAndGetAllResources() {
        String rpdId;
        String gsId;

        //Admin create resources and assign them to different pools
        Map<String, Object> rpdInfos = new HashMap<String, Object>();
        rpdInfos.put("ip", "1.1.1.1");
        rpdInfos.put("port", "2323");

        HttpResponse response = resourceOperations
                .createResource(admin, password, "RpdWin", new String[] {
                        "rprm" }, "pool1", rpdInfos);
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());

        Map<String, Object> gsInfos = new HashMap<String, Object>();
        gsInfos.put("ip", "2.2.2.2");
        gsInfos.put("port", "24");
        response = resourceOperations
                .createResource(admin, password, "GroupSeries", new String[] {
                        "rpd" }, "pool2", gsInfos);
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());

        //Admin get the resources in the pools
        List<Resource> resources = resourceOperations
                .getResource(admin, password);

        Optional<Resource> foundResource = resources.stream()
                .filter(x -> Arrays.asList(x.getKeywords())
                        .containsAll(Arrays.asList(new String[] { "rprm" })))
                .findAny();
        assertTrue(foundResource.isPresent());
        rpdId = foundResource.get().getId();

        foundResource = resources.stream()
                .filter(x -> Arrays.asList(x.getKeywords())
                        .containsAll(Arrays.asList(new String[] { "rpd" })))
                .findAny();
        assertTrue(foundResource.isPresent());
        gsId = foundResource.get().getId();

        //Delete the resources
        response = resourceOperations.deleteResource(admin, password, rpdId);
        assertEquals(HttpStatus.NO_CONTENT.value(), response.statusCode());

        response = resourceOperations.deleteResource(admin, password, gsId);
        assertEquals(HttpStatus.NO_CONTENT.value(), response.statusCode());
    }

    @Test public void poolAdminCanOnlyGetResourcesInItsPool() {
        String rpdId;
        String gsId;
        String pool1_admin = "pool1_admin";
        String pool2_admin = "pool2_admin";

        //pool admin create resources and assign them to different pools
        Map<String, Object> rpdInfos = new HashMap<String, Object>();
        rpdInfos.put("ip", "1.1.1.1");
        rpdInfos.put("port", "2323");

        HttpResponse response = resourceOperations
                .createResource(pool1_admin, password, "RpdWin", new String[] {
                        "rprm" }, "pool1", rpdInfos);
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());

        Map<String, Object> gsInfos = new HashMap<String, Object>();
        gsInfos.put("ip", "2.2.2.2");
        gsInfos.put("port", "24");
        response = resourceOperations.createResource(pool2_admin,
                                                     password,
                                                     "GroupSeries",
                                                     new String[] {
                                                             "rpd" },
                                                     "pool2",
                                                     gsInfos);
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());

        //pool1_admin retrieves resources
        List<Resource> resources = resourceOperations
                .getResource(pool1_admin, password);

        Optional<Resource> foundResource = resources.stream()
                .filter(x -> Arrays.asList(x.getKeywords())
                        .containsAll(Arrays.asList(new String[] { "rprm" })))
                .findAny();
        //check if pool1_admin can get the resource in its pool
        assertTrue(foundResource.isPresent());
        rpdId = foundResource.get().getId();

        foundResource = resources.stream()
                .filter(x -> Arrays.asList(x.getKeywords())
                        .containsAll(Arrays.asList(new String[] { "rpd" })))
                .findAny();
        //check if pool1_admin cannot get the resource in other pool
        assertFalse(foundResource.isPresent());

        //pool2_admin can get resource in its pool
        resources = resourceOperations.getResource(pool2_admin, password);
        foundResource = resources.stream()
                .filter(x -> Arrays.asList(x.getKeywords())
                        .containsAll(Arrays.asList(new String[] { "rpd" })))
                .findAny();
        assertTrue(foundResource.isPresent());
        gsId = foundResource.get().getId();

        //Delete the resources
        response = resourceOperations
                .deleteResource(pool1_admin, password, rpdId);
        assertEquals(HttpStatus.NO_CONTENT.value(), response.statusCode());

        response = resourceOperations
                .deleteResource(pool2_admin, password, gsId);
        assertEquals(HttpStatus.NO_CONTENT.value(), response.statusCode());
    }

    @Test public void poolUserCanOnlyGetResourcesInItsPool() {
        String rpdId;
        String gsId;
        String pool1_admin = "pool1_admin";
        String pool2_admin = "pool2_admin";
        String pool1_user = "pool1_user";
        String pool2_user = "pool2_user";

        //pool admin create resources and assign them to different pools
        Map<String, Object> rpdInfos = new HashMap<String, Object>();
        rpdInfos.put("ip", "1.1.1.1");
        rpdInfos.put("port", "2323");

        HttpResponse response = resourceOperations
                .createResource(pool1_admin, password, "RpdWin", new String[] {
                        "rprm" }, "pool1", rpdInfos);
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());

        Map<String, Object> gsInfos = new HashMap<String, Object>();
        gsInfos.put("ip", "2.2.2.2");
        gsInfos.put("port", "24");
        response = resourceOperations.createResource(pool2_admin,
                                                     password,
                                                     "GroupSeries",
                                                     new String[] {
                                                             "rpd" },
                                                     "pool2",
                                                     gsInfos);
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());

        //pool1_user retrieves resources
        List<Resource> resources = resourceOperations
                .getResource(pool1_user, password);

        Optional<Resource> foundResource = resources.stream()
                .filter(x -> Arrays.asList(x.getKeywords())
                        .containsAll(Arrays.asList(new String[] { "rprm" })))
                .findAny();
        //check if pool1_user can get the resource in its pool
        assertTrue(foundResource.isPresent());
        rpdId = foundResource.get().getId();

        foundResource = resources.stream()
                .filter(x -> Arrays.asList(x.getKeywords())
                        .containsAll(Arrays.asList(new String[] { "rpd" })))
                .findAny();
        //check if pool1_user cannot get the resource in other pool
        assertFalse(foundResource.isPresent());

        //pool2_user can get resource in its pool
        resources = resourceOperations.getResource(pool2_user, password);
        foundResource = resources.stream()
                .filter(x -> Arrays.asList(x.getKeywords())
                        .containsAll(Arrays.asList(new String[] { "rpd" })))
                .findAny();
        assertTrue(foundResource.isPresent());
        gsId = foundResource.get().getId();

        //pool admin delete the resources
        response = resourceOperations
                .deleteResource(pool1_admin, password, rpdId);
        assertEquals(HttpStatus.NO_CONTENT.value(), response.statusCode());

        response = resourceOperations
                .deleteResource(pool2_admin, password, gsId);
        assertEquals(HttpStatus.NO_CONTENT.value(), response.statusCode());
    }

    @Test public void poolUserCannotDeleteResource() throws Exception {
        String rpdId;
        String gsId;
        String pool1_admin = "pool1_admin";
        String pool1_user = "pool1_user";

        //pool admin create resources and assign them to different pools
        Map<String, Object> rpdInfos = new HashMap<String, Object>();
        rpdInfos.put("ip", "1.1.1.1");
        rpdInfos.put("port", "2323");

        HttpResponse response = resourceOperations
                .createResource(pool1_admin, password, "RpdWin", new String[] {
                        "rprm" }, "pool1", rpdInfos);
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());

        //pool1_user retrieves resources
        List<Resource> resources = resourceOperations
                .getResource(pool1_user, password);

        Optional<Resource> foundResource = resources.stream()
                .filter(x -> Arrays.asList(x.getKeywords())
                        .containsAll(Arrays.asList(new String[] { "rprm" })))
                .findAny();
        //check if pool1_user can get the resource in its pool
        assertTrue(foundResource.isPresent());
        rpdId = foundResource.get().getId();

        //pool1_user cannot delete the resource in its pool
        try {
            resourceOperations.deleteResource(pool1_user, password, rpdId);
        } catch (Exception e) {
            assertEquals(HttpException.class, e.getClass());
        }

        //pool admin can delete the resources
        response = resourceOperations
                .deleteResource(pool1_admin, password, rpdId);
        assertEquals(HttpStatus.NO_CONTENT.value(), response.statusCode());
    }

    @Test(expected = HttpException.class)
    public void poolUserCannotCreateResource() {
        String pool1_user = "pool1_user";

        //pool admin create resources and assign them to different pools
        Map<String, Object> rpdInfos = new HashMap<String, Object>();
        rpdInfos.put("ip", "1.1.1.1");
        rpdInfos.put("port", "2323");

        HttpResponse response = resourceOperations
                .createResource(pool1_user, password, "RpdWin", new String[] {
                        "rprm" }, "pool1", rpdInfos);
    }

    @After public void tearDown() {
        //remove the pool1 and pool2
        List<ResourcePool> totalPools = poolOperations
                .getResourcePool(admin, password);
        for (ResourcePool pool : totalPools) {
            if (!pool.getName().equals("default")) {
                HttpResponse response = poolOperations
                        .deleteResourcePool(admin, password, pool.getId());
                assertEquals(HttpStatus.NO_CONTENT.value(),
                             response.statusCode());
            }
        }

        //remove the users
        List<User> totalUsers = userOperations.getUsers(admin, password);
        for (User user : totalUsers) {
            if (!user.getUserName().equals("admin")) {
                HttpResponse response = userOperations
                        .deleteUser(admin, password, user.getId());
                assertEquals(HttpStatus.NO_CONTENT.value(),
                             response.statusCode());
            }
        }
    }
}
