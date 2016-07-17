package framework.infra.config;

/**
 * Created by Mor on 14/07/2016.
 */
public interface ProxyConfigurer {
    Object wrapWithProxy(Object o, Class type);
}
