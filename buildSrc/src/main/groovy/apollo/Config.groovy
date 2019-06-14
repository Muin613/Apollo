class Config {
    public final static int ENV_DEV = 0
    public final static int ENV_TEST = 1
    public final static int ENV_ONLINE = 2
    public static int envType = ENV_DEV

    public static String envConfig() {
        return "\\config\\dev_url_config.gradle"
//     switch (envType) {
//            case ENV_DEV:
//                return "dev_url_config.gradle"
//                break
//            case ENV_TEST:
//                return "test_url_config.gradle"
//                break
//            case ENV_ONLINE:
//                return "online_url_config.gradle"
//                break
//            default:
//                return "dev_url_config.gradle"
//                break
//        }
    }
}