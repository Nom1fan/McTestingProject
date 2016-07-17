package framework.infra.config;

import com.sun.media.jfxmedia.logging.Logger;
import framework.infra.data.TestUser;
import framework.infra.network.ServerProxy;
import framework.infra.network.ServerProxyImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mor on 14/07/2016.
 */
public class JavaConfig implements Config {

    private Map<Class, Class> ifc2ImplClass = new HashMap() {{
        put(ServerProxy.class, ServerProxyImpl.class);
    }};

    public JavaConfig() {

    }

    @Override
    public Class getImplClass(Class ifc) {
        return ifc2ImplClass.get(ifc);
    }
}
