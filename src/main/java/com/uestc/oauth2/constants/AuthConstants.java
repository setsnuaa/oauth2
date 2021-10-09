package com.uestc.oauth2.constants;

/**
 * @author jie.zhong
 * @version 1.0
 * @date 2021/9/22 16:59
 */
public interface AuthConstants {

    /**
     * 认证请求头key
     */
    String AUTHORIZATION_KEY = "Authorization";

    /**
     * JWT令牌前缀
     */
    String AUTHORIZATION_PREFIX = "bearer ";


    /**
     * Basic认证前缀
     */
    String BASIC_PREFIX = "Basic ";

    /**
     * JWT密钥
     */
    String SIGNING_KEY = "admin";

    /**
     * JWT载体key
     */
    String JWT_PAYLOAD_KEY = "payload";

    /**
     * JWT ID 唯一标识
     */
    String JWT_JTI = "jti";



    /**
     * Redis缓存权限规则key
     */
    String PERMISSION_ROLES_KEY = "auth:permission:roles";

    /**
     * 黑名单token前缀
     */
    String TOKEN_BLACKLIST_PREFIX = "auth:token:blacklist:";

    String CLIENT_DETAILS_FIELDS = "client_id, client_secret as client_secret, resource_ids, scope, "
            + "authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, "
            + "refresh_token_validity, additional_information, autoapprove";

    String BASE_CLIENT_DETAILS_SQL = "select " + CLIENT_DETAILS_FIELDS + " from oauth_client_details";

    String FIND_CLIENT_DETAILS_SQL = BASE_CLIENT_DETAILS_SQL + " order by client_id";

    String SELECT_CLIENT_DETAILS_SQL = BASE_CLIENT_DETAILS_SQL + " where client_id = ?";


    String USER_ID_KEY = "userId";

    String USER_NAME_KEY = "username";

    String AUTHORIZATION_KEYS="roles";

    String CLIENT_ID_KEY = "client_id";

    /**
     * 是否已实名认证
     */
    String IDENTITY = "identity";


    /**
     * JWT存储权限属性
     */
    String JWT_AUTHORITIES_KEY = "authorities";



    String LOGOUT_PATH = "/youlai-auth/oauth/logout";

}
