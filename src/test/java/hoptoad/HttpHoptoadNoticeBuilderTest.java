package hoptoad;


import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HttpHoptoadNoticeBuilderTest {

    private static final Throwable EXCEPTION = new Exception();
    private static final String ENVIRONMENT = "Test Environment";
    private static final String API_KEY = "Test API Key";
    private static final String URL = "https://test.example.com/path?queryString=value&a=b";


    private HttpServletRequest mockRequest;
    private HashMap<String, String[]> requestParameters;
    private HttpSession mockSession;

    @Before
    public void setUp() {
        mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getRequestURL()).thenReturn(new StringBuffer(URL));

        requestParameters = new HashMap<String, String[]>();
        when(mockRequest.getParameterMap()).thenReturn(requestParameters);

        mockSession = mock(HttpSession.class);
        when(mockSession.getAttributeNames()).thenReturn(new Enumeration<String>() {
            public boolean hasMoreElements() {
                return false;
            }
            public String nextElement() {
                return null;
            }
        });
        when(mockRequest.getSession()).thenReturn(mockSession);
    }

    @Test
    public void testSetsRequestUrl() {

        HoptoadNotice notice = buildNotice(mockRequest);

        assertThat(notice.url(), equalTo(URL));
    }

    @Test
    public void testAddsRequestParameter() {
        final String parameterName = "param1";
        final String parameterValue = "value1";

        requestParameters.put(parameterName, new String[]{parameterValue});

        HoptoadNotice notice = buildNotice(mockRequest);

        assertThat((String) notice.request().get(parameterName), equalTo(parameterValue));
    }

    @Test
    public void testAddsSessionSessionAttributes() {

        final String sessionName1 = "name1";
        final String sessionValue1 = "value1";
        final String sessionName2 = "name2";
        final String sessionValue2 = "value2";

        when(mockSession.getAttributeNames()).thenReturn(new Vector<String>(Arrays.asList(sessionName1, sessionName2)).elements());
        when(mockSession.getAttribute(sessionName1)).thenReturn(sessionValue1);
        when(mockSession.getAttribute(sessionName2)).thenReturn(sessionValue2);

        HoptoadNotice notice = buildNotice(mockRequest);

        assertThat((String) notice.session().get(sessionName1), equalTo(sessionValue1));
        assertThat((String) notice.session().get(sessionName2), equalTo(sessionValue2));
    }

    private HoptoadNotice buildNotice(HttpServletRequest request) {
        return new HttpHoptoadNoticeBuilder(API_KEY, mock(Backtrace.class), EXCEPTION, ENVIRONMENT, request).newNotice();
    }
}
