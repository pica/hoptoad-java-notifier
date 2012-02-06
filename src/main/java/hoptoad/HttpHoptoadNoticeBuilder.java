package hoptoad;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class HttpHoptoadNoticeBuilder extends HoptoadNoticeBuilder {

    private static final String HEADER_PREFIX = "[HttpHeader] ";

    public HttpHoptoadNoticeBuilder(final String apiKey, final Backtrace backtraceBuilder, final Throwable throwable, final String env, HttpServletRequest request, String controllerName) {
        super(apiKey, backtraceBuilder, throwable, env);

        setRequest(request.getRequestURL().toString(), controllerName);
        setParameters(request.getParameterMap());
        setSession(request.getSession());
        addHeaders(request);

    }

    private void addHeaders(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            Map<String, Object> headersMap = new HashMap<String, Object>();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                headersMap.put(HEADER_PREFIX + headerName, request.getHeader(headerName));
            }
            environment(headersMap);
        }
    }

    private void setParameters(Map<String, String[]> requestParameters) {
        Map<String, Object> airbrakeRequestParameters = new HashMap<String, Object>();

        for (String key : requestParameters.keySet()) {
            airbrakeRequestParameters.put(key, requestParameters.get(key)[0]);
        }

        request(airbrakeRequestParameters);
    }

    private void setSession(HttpSession session) {

        Map<String, Object> sessionAttributes = new HashMap<String, Object>();

        Enumeration attributeNames = session.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String name = (String) attributeNames.nextElement();
            sessionAttributes.put(name, session.getAttribute(name));
        }

        session(sessionAttributes);
    }
}
