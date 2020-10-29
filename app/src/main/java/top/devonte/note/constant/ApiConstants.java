package top.devonte.note.constant;

public class ApiConstants {
    /**
     * 登录接口
     */
    public static final String LOGIN_API = Constants.BASE_URL + "/auth/login";
    /**
     * 注册接口
     */
    public static final String REGISTER_API = Constants.BASE_URL + "/auth/register";
    /**
     * 分页获取文档接口
     */
    public static final String GET_FILES_API = Constants.BASE_URL + "/file/page";
    /**
     * 分页获取文件夹接口
     */
    public static final String GET_FOLDERS_API = Constants.BASE_URL + "/folder";
    /**
     * 获取登录的用户信息接口
     */
    public static final String GET_USER_INFO_API = Constants.BASE_URL + "/auth/info";
    /**
     * 退出登录接口
     */
    public static final String LOG_OUT_API = Constants.BASE_URL + "/auth/logout";
    /**
     * 获取文档详细信息接口
     */
    public static final String GET_FILE_API = Constants.BASE_URL + "/file";

}
