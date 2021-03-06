package com.polycom.honeycomb.rm.apitests;

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

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by Tao Chen on 2016/11/9.
 */
@RunWith(SpringJUnit4ClassRunner.class) @DirtiesContext public class UserTests {
    private String         host           = "localhost";
    private String         admin          = "admin";
    private String         password       = "Polycom321";
    private PoolOperations poolOperations = new PoolOperations(host);
    private UserOperations userOperations = new UserOperations(host);

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

    @Test public void adminListUsers() throws Exception {
        List<User> users = userOperations.getUsers(admin, password);

        User pool1_admin = users.stream()
                .filter(x -> x.getUserName().equals("pool1_admin")).findAny()
                .get();

        assertEquals("pool1_admin", pool1_admin.getUserName());

        User pool2_admin = users.stream()
                .filter(x -> x.getUserName().equals("pool2_admin")).findAny()
                .get();

        assertEquals("pool2_admin", pool2_admin.getUserName());
    }

    @Test public void poolAdminListUsers() {
        //Create user and assign it to pool
        HttpResponse response = userOperations.createUser("pool1_admin",
                                                          password,
                                                          "pool1_user1",
                                                          "pool1",
                                                          "user1",
                                                          "Polycom321",
                                                          new String[] {
                                                                  "POOL_USER" },
                                                          new String[] { "pool1" });
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());

        response = userOperations.createUser("pool1_admin",
                                             password,
                                             "pool1_user2",
                                             "pool1",
                                             "user2",
                                             "Polycom321",
                                             new String[] { "POOL_USER" },
                                             new String[] { "pool1" });
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());

        //pool admin trying to list the users in it pool
        List<User> users = userOperations.getUsers("pool1_admin", "Polycom321");

        User pool1_user1 = users.stream()
                .filter(x -> x.getUserName().equals("pool1_user1")).findAny()
                .get();
        assertEquals("pool1_user1", pool1_user1.getUserName());

        User pool1_user2 = users.stream()
                .filter(x -> x.getUserName().equals("pool1_user2")).findAny()
                .get();
        assertEquals("pool1_user2", pool1_user2.getUserName());

        //pool admin will not see the admin
        Optional<User> admin_user = users.stream()
                .filter(x -> x.getUserName().equals("admin")).findAny();
        assertFalse(admin_user.isPresent());

        //pool admin will not see the user in other pool
        Optional<User> pool2_admin = users.stream()
                .filter(x -> x.getUserName().equals("pool2_admin")).findAny();
        assertFalse(pool2_admin.isPresent());

        //remove the user created just now
        for (User user : users) {
            if (user.getUserName().equals("pool1_user1") || user.getUserName()
                    .equals("pool1_user2")) {
                response = userOperations
                        .deleteUser(admin, password, user.getId());
                assertEquals(HttpStatus.NO_CONTENT.value(),
                             response.statusCode());
            }
        }
    }

    @Test public void poolUserListUsers() {
        HttpResponse response = userOperations.createUser("pool1_admin",
                                                          password,
                                                          "pool1_user1",
                                                          "pool1",
                                                          "user1",
                                                          "Polycom321",
                                                          new String[] {
                                                                  "POOL_USER" },
                                                          new String[] { "pool1" });
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());

        List<User> users = userOperations.getUsers("pool1_user1", "Polycom321");

        User pool1_user1 = users.stream()
                .filter(x -> x.getUserName().equals("pool1_user1")).findAny()
                .get();
        assertEquals("pool1_user1", pool1_user1.getUserName());

        User pool1_admin = users.stream()
                .filter(x -> x.getUserName().equals("pool1_admin")).findAny()
                .get();
        assertEquals("pool1_admin", pool1_admin.getUserName());

        //pool admin will not see the admin
        Optional<User> admin_user = users.stream()
                .filter(x -> x.getUserName().equals("admin")).findAny();
        assertFalse(admin_user.isPresent());

        //pool admin will not see the user in other pool
        Optional<User> pool2_admin = users.stream()
                .filter(x -> x.getUserName().equals("pool2_admin")).findAny();
        assertFalse(pool2_admin.isPresent());

        //remove the user created just now
        for (User user : users) {
            if (user.getUserName().equals("pool1_user1")) {
                response = userOperations
                        .deleteUser(admin, password, user.getId());
                assertEquals(HttpStatus.NO_CONTENT.value(),
                             response.statusCode());
            }
        }
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
