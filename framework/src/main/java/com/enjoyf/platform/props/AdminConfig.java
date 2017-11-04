/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.props;

import com.enjoyf.platform.service.admin.AdminRole;
import com.enjoyf.platform.service.admin.Privilege;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-9-17 上午11:49
 * Description:
 */
public class AdminConfig {
    private static final Logger logger = LoggerFactory.getLogger(AdminConfig.class);

    //attributes
    private static AdminConfig instance;
    private FiveProps props;

    //
    private Map<String, Privilege> privilagesMap;
    private Map<String, AdminRole> rolesMap;

    // the key of the Privilages
    private static final String KEY_ADMIN_ROLES_LIST = "tools.admin.roles.list";
    private static final String KEY_PRIVILEGES_LIST = "tools.privileges.list";

    private static final String PREFIX_KEY_ROLE = "tools.role.";
    private static final String PREFIX_KEY_PRIVILEGE = "tools.privilege.";

    private static final String SUFFIX_KEY_NAME = ".name";
    private static final String SUFFIX_KEY_PRIVILEGE_LIST = ".privileges.list";


    public static synchronized AdminConfig get() {
        if (instance == null) {
            instance = new AdminConfig();
        }

        return instance;
    }

    private AdminConfig() {
        props = new FiveProps(EnvConfig.get().getAdminConfigFile());

        init();
    }

    private void init() {
        logger.info("Admin Props init start.");

        //
        privilagesMap = new HashMap<String, Privilege>();
        rolesMap = new LinkedHashMap<String, AdminRole>();

        // read the privileges.
        List<String> privilegeCodes = props.getList(KEY_PRIVILEGES_LIST);

        for (String pCode : privilegeCodes) {
            Privilege privilege = new Privilege(pCode);
            privilege.setName(props.get(PREFIX_KEY_PRIVILEGE + pCode + SUFFIX_KEY_NAME));

            privilagesMap.put(privilege.getCode(), privilege);
        }

        // read the role configures.
        List<String> roleCodes = props.getList(KEY_ADMIN_ROLES_LIST);

        for (String rCode : roleCodes) {
            AdminRole role = new AdminRole(rCode);
            role.setName(props.get(PREFIX_KEY_ROLE + rCode + SUFFIX_KEY_NAME));

            Set<Privilege> rolePrivileges = new LinkedHashSet<Privilege>();
            List<String> rolePrivilegeCodes = props.getList(PREFIX_KEY_ROLE + rCode + SUFFIX_KEY_PRIVILEGE_LIST);
            for (String rpCode : rolePrivilegeCodes) {
                Privilege p = privilagesMap.get(rpCode);

                if (p != null) {
                    rolePrivileges.add(p);
                }
            }

            role.setPrivilages(rolePrivileges);

            rolesMap.put(role.getCode(), role);
        }

        logger.info("Admin Props init finished.");
    }

    public Map<String, Privilege> getPrivilagesMap() {
        return privilagesMap;
    }

    public Map<String, AdminRole> getRolesMap() {
        return rolesMap;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
