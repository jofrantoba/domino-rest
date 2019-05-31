package org.dominokit.domino.rest.shared.request;

import java.util.List;

public interface RestConfig {
    RequestRouter<ServerRequest> getServerRouter();
    String getDefaultServiceRoot();

    String getDefaultJsonDateFormat();

    List<DynamicServiceRoot> getServiceRoots();

    List<RequestInterceptor> getRequestInterceptors();

    String getDefaultResourceRootPath();

    AsyncRunner asyncRunner();
}