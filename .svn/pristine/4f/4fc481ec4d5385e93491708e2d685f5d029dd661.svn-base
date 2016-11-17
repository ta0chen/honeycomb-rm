package com.polycom.honeycomb.rm.apitests;

import com.polycom.honeycomb.rm.domain.ResourcePool;
import com.polycom.honeycomb.rm.domain.User;
import com.polycom.honeycomb.rm.pool.PoolOperations;
import com.polycom.honeycomb.rm.user.UserOperations;
import com.polycom.http.HttpException;
import jodd.http.HttpResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Created by Tao Chen on 2016/11/16.
 */
@RunWith(SpringJUnit4ClassRunner.class) @DirtiesContext public class PoolTests {
    private String         host           = "localhost";
    private String         admin          = "admin";
    private String         password       = "Polycom123";
    private PoolOperations poolOperations = new PoolOperations(host);
    private UserOperations userOperations = new UserOperations(host);

    private String pool1Id       = null;
    private String defaultPoolId = null;

    @Before public void setUp() {
        //Create two pools
        HttpResponse response = poolOperations
                .createResourcePool(admin, password, "pool1", "pool1 desc");
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

        List<ResourcePool> totalPools = poolOperations
                .getResourcePool(admin, password);
        ResourcePool default_pool = totalPools.stream()
                .filter(x -> x.getName().equals("default")).findAny().get();
        ResourcePool pool1 = totalPools.stream()
                .filter(x -> x.getName().equals("pool1")).findAny().get();

        defaultPoolId = default_pool.getId();
        pool1Id = pool1.getId();
    }

    @Test(expected = HttpException.class) public void poolAdminCreatePool() {
        HttpResponse response = poolOperations.createResourcePool("pool1_admin",
                                                                  password,
                                                                  "pool2",
                                                                  "pool2 desc");
    }

    @Test(expected = HttpException.class) public void poolUserCreatePool() {
        HttpResponse response = poolOperations.createResourcePool("pool1_user",
                                                                  password,
                                                                  "pool2",
                                                                  "pool2 desc");
    }

    @Test(expected = HttpException.class)
    public void poolAdminDeleteDefaultPool() {
        HttpResponse response = poolOperations
                .deleteResourcePool("pool1_admin", password, defaultPoolId);
    }

    @Test(expected = HttpException.class)
    public void poolUserDeleteDefaultPool() {
        HttpResponse response = poolOperations
                .deleteResourcePool("pool1_user", password, defaultPoolId);
    }

    @Test(expected = HttpException.class)
    public void poolAdminDeleteSelfPool() {
        HttpResponse response = poolOperations
                .deleteResourcePool("pool1_admin", password, pool1Id);
    }

    @Test(expected = HttpException.class) public void poolUserDeleteSelfPool() {
        HttpResponse response = poolOperations
                .deleteResourcePool("pool1_user", password, pool1Id);
    }

    @Test public void poolAdminGetPool() {
        List<ResourcePool> pools = poolOperations
                .getResourcePool("pool1_admin", password);
        Optional<ResourcePool> foundPool = pools.stream()
                .filter(x -> x.getName().equals("default")).findAny();
        assertFalse(foundPool.isPresent());

        foundPool = pools.stream().filter(x -> x.getName().equals("pool1"))
                .findAny();
        assertTrue(foundPool.isPresent());
    }

    @Test public void poolUserGetPool() {
        List<ResourcePool> pools = poolOperations
                .getResourcePool("pool1_user", password);
        Optional<ResourcePool> foundPool = pools.stream()
                .filter(x -> x.getName().equals("default")).findAny();
        assertFalse(foundPool.isPresent());

        foundPool = pools.stream().filter(x -> x.getName().equals("pool1"))
                .findAny();
        assertTrue(foundPool.isPresent());
    }

    @After public void tearDown() {
        //remove the pool1
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
