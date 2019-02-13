package okhttp.dispatcher;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static org.assertj.core.api.Assertions.assertThat;

public class DispatcherTest {
	private MockWebServer server = new MockWebServer();
	
	@After
	public void serverShutdown() throws IOException {
		server.shutdown();
	}
	
	@Test
	public void simpleDispatcher() throws IOException {
		server.start();
		
		final List<RecordedRequest> requests = new ArrayList<>();
		final Dispatcher dispatcher = new Dispatcher() {
			@Override
			public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
				requests.add(request);
				return new MockResponse();
			}
		};
		
		assertThat(requests.size()).isEqualTo(0);
		
		server.setDispatcher(dispatcher);
		
		final URL url = server.url("/").url();
		final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.getResponseCode();
		
		assertThat(requests.size()).isEqualTo(1);
	}
}
