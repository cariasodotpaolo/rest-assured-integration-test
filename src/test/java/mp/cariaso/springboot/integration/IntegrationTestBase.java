package mp.cariaso.springboot.integration;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = IntegrationTestConfiguration.class)
public class IntegrationTestBase {

    @Value("${server.contextPath}")
    private String contextPath;

    @Value("${server.port}")
    private String port;

    public String getContextUrl() {
        return String.format("http://localhost:%s%s", port, contextPath);
    }

    public String getBaseUri() {
        return String.format("http://localhost%s", contextPath);
    }

    public String getContextPath() {
        return contextPath;
    }

    public int getPort() {
        return Integer.parseInt(port);
    }
}
