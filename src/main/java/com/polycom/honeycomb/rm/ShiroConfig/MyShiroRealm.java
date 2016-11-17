package com.polycom.honeycomb.rm.ShiroConfig;

import com.polycom.honeycomb.rm.domain.Permission;
import com.polycom.honeycomb.rm.domain.Role;
import com.polycom.honeycomb.rm.domain.User;
import com.polycom.honeycomb.rm.repository.UserMongoRepository;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Tao Chen on 16/10/24.
 */
public class MyShiroRealm extends AuthorizingRealm {
    private static org.slf4j.Logger LOGGER = LoggerFactory
            .getLogger(MyShiroRealm.class);
    @Autowired UserMongoRepository userMongoRepository;

    /**
     * Retrieves the AuthorizationInfo for the given principals from the underlying data store.  When returning
     * an instance from this method, you might want to consider using an instance of
     * {@link SimpleAuthorizationInfo SimpleAuthorizationInfo}, as it is suitable in most cases.
     *
     * @param principalCollection the primary identifying principals of the AuthorizationInfo that should be retrieved.
     * @return the AuthorizationInfo associated with this principals.
     * @see SimpleAuthorizationInfo
     */
    @Override protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection principalCollection) {
        LOGGER.info("Doing authorization in the realm...");
        SimpleAuthorizationInfo info = null;
        String loginName = (String) super
                .getAvailablePrincipal(principalCollection);
        User user = userMongoRepository.findByUserName(loginName);

        if (user == null) {
            throw new UnknownAccountException(
                    "User " + loginName + " does not exist.");
        }

        LOGGER.info("Authorization passed...");
        info = new SimpleAuthorizationInfo();
        Set<String> roleNames = new HashSet<String>();
        List<Role> roles = user.getRoles();
        for (Role role : roles) {
            roleNames.add(role.getName());
        }
        info.setRoles(roleNames);

        List<String> permissions = new ArrayList<String>();
        List<Role> roleList = user.getRoles();
        for (Role role : roleList) {
            List<Permission> perms = role.getPermissions();
            for (Permission permission : perms)
                permissions.add(permission.getName());
        }
        info.addStringPermissions(permissions);
        return info;
    }

    /**
     * Retrieves authentication data from an implementation-specific datasource (RDBMS, LDAP, etc) for the given
     * authentication token.
     * <p/>
     * For most datasources, this means just 'pulling' authentication data for an associated subject/user and nothing
     * more and letting Shiro do the rest.  But in some systems, this method could actually perform EIS specific
     * log-in logic in addition to just retrieving data - it is up to the Realm implementation.
     * <p/>
     * A {@code null} return value means that no account could be associated with the specified token.
     *
     * @param authenticationToken the authentication token containing the user's principal and credentials.
     * @return an {@link AuthenticationInfo} object containing account data resulting from the
     * authentication ONLY if the lookup is successful (i.e. account exists and is valid, etc.)
     * @throws AuthenticationException if there is an error acquiring data or performing
     *                                 realm-specific authentication logic for the specified <tt>token</tt>
     */
    @Override protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken authenticationToken)
            throws AuthenticationException {
        AuthenticationInfo rtn = null;
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        User user = userMongoRepository.findByUserName(token.getUsername());

        if (user == null) {
            throw new UnknownAccountException(
                    "User " + token.getUsername() + " does not exist.");
        }

        if (user.getPassword().equals(String.valueOf(token.getPassword()))) {
            LOGGER.info("Authentication passed...");
            rtn = new SimpleAuthenticationInfo(token.getUsername(),
                                               token.getPassword(),
                                               getName());
        }
        return rtn;
    }
}
