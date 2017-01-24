package com.polycom.honeycomb.rm.apitests;

import com.polycom.honeycomb.rm.constants.ConstantsOperations;
import com.polycom.honeycomb.rm.domain.Constants;
import com.polycom.honeycomb.rm.domain.ResourcePool;
import com.polycom.honeycomb.rm.domain.User;
import com.polycom.honeycomb.rm.pool.PoolOperations;
import com.polycom.honeycomb.rm.user.UserOperations;
import jodd.http.HttpResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Created by Tao Chen on 2016/12/12.
 */
@RunWith(SpringJUnit4ClassRunner.class) @DirtiesContext
public class ConstantsTests {
    private String              host             = "localhost";
    private String              admin            = "admin";
    private String              password         = "Polycom321";
    private ConstantsOperations configOperations = new ConstantsOperations(host);
    private PoolOperations      poolOperations   = new PoolOperations(host);
    private UserOperations      userOperations   = new UserOperations(host);

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
                                             "Polycom321",
                                             new String[] { "POOL_ADMIN" },
                                             new String[] { "pool1" });
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());

        response = userOperations.createUser(admin,
                                             password,
                                             "pool2_admin",
                                             "pool2",
                                             "admin",
                                             "Polycom321",
                                             new String[] { "POOL_ADMIN" },
                                             new String[] { "pool2" });
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());
    }

    @Test public void adminCanCreateConfigurations() {
        Map<String, String> detailConfig = new HashMap<String, String>();
        detailConfig.put("qaServer", "1.1.1.1");
        HttpResponse response = configOperations
                .createConfigurations(admin, password, "pool1", detailConfig);
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());

        List<Constants> configsList = configOperations
                .getConfigs(admin, password);
        Constants configs = configsList.stream()
                .filter(x -> x.getConstants().get("qaServer").equals("1.1.1.1"))
                .findAny().get();
        assertEquals("pool1", configs.getPoolName());

        response = configOperations
                .deleteConfigurations(admin, password, configs.getId());
        assertEquals(HttpStatus.NO_CONTENT.value(), response.statusCode());
    }

    @Test public void poolAdminCanCreateConfigurations() {
        Map<String, String> detailConfig = new HashMap<String, String>();
        detailConfig.put("qaServer", "1.1.1.1");
        HttpResponse response = configOperations.createConfigurations(
                "pool1_admin",
                password,
                "pool1",
                detailConfig);
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());

        List<Constants> configsList = configOperations
                .getConfigs("pool1_admin", password);
        Constants configs = configsList.stream()
                .filter(x -> x.getConstants().get("qaServer").equals("1.1.1.1"))
                .findAny().get();
        assertEquals("pool1", configs.getPoolName());

        response = configOperations
                .deleteConfigurations("pool1_admin", password, configs.getId());
        assertEquals(HttpStatus.NO_CONTENT.value(), response.statusCode());
    }

    @Test public void poolCanIsolateConfigs() {
        String configInPool1 = "";
        String configInPool2 = "";
        Map<String, String> detailConfig1 = new HashMap<String, String>();
        detailConfig1.put("qaServer", "1.1.1.1");
        HttpResponse response = configOperations.createConfigurations(
                "pool1_admin",
                password,
                "pool1",
                detailConfig1);
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());

        Map<String, String> detailConfig2 = new HashMap<String, String>();
        detailConfig2.put("pcProxy", "2.2.2.2");
        response = configOperations.createConfigurations("pool2_admin",
                                                         password,
                                                         "pool2",
                                                         detailConfig2);
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());

        //Pool1 admin try to get the configs in different pool
        List<Constants> configsList = configOperations
                .getConfigs("pool1_admin", password);
        Optional<Constants> foundConfigs = configsList.stream()
                .filter(x -> x.getConstants().get("pcProxy") != null && x
                        .getConstants().get("pcProxy").equals("2.2.2.2"))
                .findAny();
        assertFalse(foundConfigs.isPresent());

        configsList = configOperations.getConfigs("pool2_admin", password);
        foundConfigs = configsList.stream()
                .filter(x -> x.getConstants().get("pcProxy").equals("2.2.2.2"))
                .findAny();
        assertTrue(foundConfigs.isPresent());
        configInPool2 = foundConfigs.get().getId();

        //Pool2 admin try to get the configs in pool1
        configsList = configOperations.getConfigs("pool2_admin", password);
        foundConfigs = configsList.stream()
                .filter(x -> x.getConstants().get("qaServer") != null && x
                        .getConstants().get("qaServer").equals("1.1.1.1"))
                .findAny();
        assertFalse(foundConfigs.isPresent());

        configsList = configOperations.getConfigs("pool1_admin", password);
        foundConfigs = configsList.stream()
                .filter(x -> x.getConstants().get("qaServer").equals("1.1.1.1"))
                .findAny();
        assertTrue(foundConfigs.isPresent());
        configInPool1 = foundConfigs.get().getId();

        response = configOperations
                .deleteConfigurations("pool1_admin", password, configInPool1);
        assertEquals(HttpStatus.NO_CONTENT.value(), response.statusCode());

        response = configOperations
                .deleteConfigurations("pool2_admin", password, configInPool2);
        assertEquals(HttpStatus.NO_CONTENT.value(), response.statusCode());
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
