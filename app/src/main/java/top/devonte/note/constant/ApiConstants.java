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
    public static final String GET_FILES_API = Constants.BASE_URL + "/file/p";
    /**
     * 分页获取文件夹接口
     */
    public static final String GET_FOLDERS_API = Constants.BASE_URL + "/folders";
    /**
     * 退出登录接口
     */
    public static final String LOG_OUT_API = Constants.BASE_URL + "/auth/logout";
    /**
     * 获取登录用户信息接口
     */
    public static final String AUTH_INFO_API = Constants.BASE_URL + "/auth/info";
    /**
     * 创建文档接口
     */
    public static final String RESTFUL_FILE_API = Constants.BASE_URL + "/file";
    /**
     * 下载文档接口
     */
    public static final String DOWNLOAD_FILE_API = Constants.BASE_URL + "/file/download";
    /**
     * 统计有用户数据接口
     */
    public static final String ANALYTIC_USER_INFO_API = Constants.BASE_URL + "/analytic/info";

}
