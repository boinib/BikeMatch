package webservices;

import security.AuthenticationFilter;
import security.AuthenticationResource;
import security.MySecurityContext;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/restservices")
public class MyApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(AuthenticationResource.class);
        classes.add(AuthenticationFilter.class);
        classes.add(MySecurityContext.class);
        classes.add(CorsFilter.class);
        classes.add(Fietsresource.class);
        classes.add(AfspraakResource.class);
        classes.add(AccessoireResource.class);
        return classes;
    }
}
