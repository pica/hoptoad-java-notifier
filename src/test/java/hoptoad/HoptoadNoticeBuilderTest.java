package hoptoad;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.*;

public class HoptoadNoticeBuilderTest {

	@Test
	public void testBuildNoticeErrorClass() {
		HoptoadNoticeBuilder builder = new HoptoadNoticeBuilder("apiKey", new RuntimeException("errorMessage"));
		HoptoadNotice notice = builder.newNotice();
		assertThat(notice.errorClass(), is(equalTo("java.lang.RuntimeException")));
	}

	@Test
	public void testErrorClass_nestedExceptions_returnsBaseExceptionClass() {
        Throwable ex1 = new ArrayStoreException();
        Throwable ex2 = new RuntimeException("other message", ex1);
        HoptoadNoticeBuilder builder = new HoptoadNoticeBuilder("apiKey", new RuntimeException("errorMessage", ex2));
		assertTrue(builder.errorClassIs("java.lang.ArrayStoreException"));
	}
}
