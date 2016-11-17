package com.polycom.honeycomb.rm;

import com.mongodb.Mongo;
import com.polycom.honeycomb.rm.domain.Permission;
import com.polycom.honeycomb.rm.domain.Role;
import com.polycom.honeycomb.rm.domain.User;
import com.polycom.honeycomb.rm.repository.PermissionMongoRepository;
import com.polycom.honeycomb.rm.repository.RoleMongoRepository;
import com.polycom.honeycomb.rm.repository.UserMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weigao on 13/9/2016.
 */
@Configuration @ControllerAdvice @SpringBootApplication
public class Application {
    @Value("${db_rebuild}") private boolean db_rebuildMongoData = false;

    @Autowired Mongo                     mongo;
    @Autowired RoleMongoRepository       roleMongoRepository;
    @Autowired PermissionMongoRepository permissionMongoRepository;
    @Autowired UserMongoRepository       userMongoRepository;

    @Bean public Jackson2RepositoryPopulatorFactoryBean repositoryPopulator() {
        Jackson2RepositoryPopulatorFactoryBean factoryBean = new Jackson2RepositoryPopulatorFactoryBean();

        if (db_rebuildMongoData) {
            mongo.dropDatabase("test");
            Resource initData = new ClassPathResource("init/initData.json");
            factoryBean.setResources(new Resource[] { initData });
        } else {
            factoryBean.setResources(new Resource[] {});
        }

        return factoryBean;
    }

    public void setupDefaultUserRepo() {
        if (db_rebuildMongoData) {
            //create the default admin user and its role
            Role roleAdmin = new Role();
            roleAdmin.setName("ADMIN");
            roleAdmin.setDescription("Administrator");
            List<Permission> permissions = permissionMongoRepository.findAll();
            roleAdmin.setPermissions(permissions);
            roleMongoRepository.save(roleAdmin);

            User userAdmin = new User();
            userAdmin.setEmail("tao.chen@polycom.com");
            userAdmin.setFirstName("admin");
            userAdmin.setLastName("admin");
            userAdmin.setPassword("Polycom123");
            userAdmin.setUserName("admin");
            userAdmin.setRoles(roleMongoRepository.findAll());
            userMongoRepository.save(userAdmin);

            //create the initial roles
            //1. pool admin role
            List<Permission> poolAdminPermissions = new ArrayList<Permission>();
            Role poolAdmin = new Role();
            poolAdmin.setName("POOL_ADMIN");
            poolAdmin.setDescription("Pool administrator");

            for (Permission permission : permissions) {
                if (!permission.getName().equals("POOL_OPERATOR") && !permission
                        .getName().equals("POOL_UPDATER")) {
                    poolAdminPermissions.add(permission);
                }
            }
            poolAdmin.setPermissions(poolAdminPermissions);
            roleMongoRepository.save(poolAdmin);

            //2. pool user role
            List<Permission> poolUserPermissions = new ArrayList<Permission>();
            Role poolUser = new Role();
            poolUser.setName("POOL_USER");
            poolUser.setDescription("Pool user");

            for (Permission permission : permissions) {
                if (permission.getName().equals("POOL_READER") || permission
                        .getName().equals("RESOURCE_READER") || permission
                        .getName().equals("RESOURCE_UPDATER") || permission
                        .getName().equals("USER_READER")) {
                    poolUserPermissions.add(permission);
                }
            }
            poolUser.setPermissions(poolUserPermissions);
            roleMongoRepository.save(poolUser);
        }
    }

    //    @Bean
    //    Environment env() {
    //        return Environment.initializeIfEmpty();
    //    }

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext ctx = SpringApplication
                .run(new Object[] { Application.class }, args);
        Application app = ctx.getBean(Application.class);
        app.setupDefaultUserRepo();
    }
}
