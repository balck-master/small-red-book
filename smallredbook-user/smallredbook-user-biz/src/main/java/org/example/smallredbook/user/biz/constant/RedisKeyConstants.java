package org.example.smallredbook.user.biz.constant;

/**
 * @Author: tzy
 * @Description: redis键名 常量类 ，统一管理 Redis Key
 * @Date: 2024/7/13 17:38
 */
public class RedisKeyConstants {



    /**
     * 小哈书全局 ID 生成器 KEY
     */
    public static final String XIAOHASHU_ID_GENERATOR_KEY = "xiaohashu.id.generator";


    /**
     * 用户角色数据 KEY 前缀
     */
    private static final String USER_ROLES_KEY_PREFIX = "user:roles:";


    /**
     * 角色对应的权限集合 KEY 前缀
     */
    private static final String ROLE_PERMISSIONS_KEY_PREFIX = "role:permissions:";
    /**
     * 用户信息数据 KEY 前缀
     */
    private static final String USER_INFO_KEY_PREFIX = "user:info:";

    /**
     * 构建角色对应的权限 key
     * @param roleId
     * @return
     */
    public static String buildRolePermissionKey(Long roleId) {
        return USER_ROLES_KEY_PREFIX + roleId;
    }

    /**
     * 用户对应的角色集合 KEY
     * @param userId
     * @return
     */
    public static String buildUserRoleKey(Long userId) {
        return USER_ROLES_KEY_PREFIX + userId;
    }

    /**
     * 构建角色对应的权限集合 KEY
     * @param roleKey
     * @return
     */
    public static String buildRolePermissionsKey(String roleKey) {
        return ROLE_PERMISSIONS_KEY_PREFIX + roleKey;
    }

    /**
     * 构建角色对应的权限集合 KEY
     * @param userId
     * @return
     */
    public static String buildUserInfoKey(Long userId) {
        return USER_INFO_KEY_PREFIX + userId;
    }
}
