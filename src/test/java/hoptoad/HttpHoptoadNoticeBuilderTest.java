package hoptoad;


import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
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
    private static final String CONTROLLER_NAME = "testController";


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
    public void testConstructor_setsRequestUrl() {

        HoptoadNotice notice = buildNotice(mockRequest);

        assertThat(notice.url(), equalTo(URL));
    }

    @Test
    public void testConstructor_setsControllerName() {

        HoptoadNotice notice = buildNotice(mockRequest);

        assertThat(notice.component(), equalTo(CONTROLLER_NAME));
    }

    @Test
    public void testConstructor_addsRequestParameter() {
        final String parameterName = "param1";
        final String parameterValue = "value1";

        requestParameters.put(parameterName, new String[]{parameterValue});

        HoptoadNotice notice = buildNotice(mockRequest);

        assertThat((String) notice.request().get(parameterName), equalTo(parameterValue));
    }

    @Test
    public void testConstructor_addsSessionSessionAttributes() {

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

    @Test
    public void testConstructor_addsHeadersToEnv() {

        final String header1Name = "header1";
        final String header2Name = "header2";

        final String header1Value = "header1value";
        final String header2Value = "header2value";

        List<String> headers = Arrays.asList(header1Name, header2Name);
        when(mockRequest.getHeaderNames()).thenReturn( new org.apache.commons.collections.iterators.IteratorEnumeration(headers.iterator()));
        when(mockRequest.getHeader(header1Name)).thenReturn(header1Value);
        when(mockRequest.getHeader(header2Name)).thenReturn(header2Value);

        HoptoadNotice notice = buildNotice(mockRequest);

        assertThat((String) notice.environment().get("[HttpHeader] " + header1Name), equalTo(header1Value));
        assertThat((String) notice.environment().get("[HttpHeader] " + header2Name), equalTo(header2Value));
    }

    private HoptoadNotice buildNotice(HttpServletRequest request) {
        return new HttpHoptoadNoticeBuilder(API_KEY, mock(Backtrace.class), EXCEPTION, ENVIRONMENT, request, CONTROLLER_NAME).newNotice();
    }
}
