package model;

import java.net.URI;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(PingTask.class);
    
    private final PingConfig config;

    private PingTask(PingConfig config) {
        this.config = config;
    }

    public static final PingTask with(final PingConfig config) {
        return new PingTask(config);
    } 
    
    public void ping() {
        LOGGER.info("Starting HTTP call...");
        LOGGER.info("=====================");
        LOGGER.info("URI: {}", config.uri());
        
        try {
            
            HttpResponse httpResponse = this.httpClient()
                                            .execute(
                                                    host(),
                                                    httpGet()
                                            );
            
            LOGGER.info("");
            LOGGER.info("RESULT:");
            LOGGER.info("=======");
            LOGGER.info("");
            LOGGER.info("Http STATUS response: {} ", httpResponse.getStatusLine().getStatusCode());
            LOGGER.info("Body: {}", httpResponse.getEntity());

        } catch (Exception e) {
            LOGGER.error("Error on request.", e);
        }
    }

    private final HttpHost host() {
        URI uri = config.uri();
        return new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
    } 
    
    private final HttpRequestBase httpGet() {
        return new HttpGet(config.uri());
    }
    
    private final HttpClient httpClient() throws GeneralSecurityException {
        LOGGER.info("");
        LOGGER.info("Creating APACHE HTTP CLIENT... ");
        LOGGER.info("============================== ");
        
        return HttpClientBuilder.create()
                                .setDefaultRequestConfig(
                                        requestConfig()
                                )
                                .setRedirectStrategy(new LaxRedirectStrategy()) // or .disableRedirectHandling()
                                .setSSLSocketFactory(
                                        new SSLSocketFactory(sslContext())
                                )
                                .build();
    }

    private final RequestConfig requestConfig() {
        LOGGER.info("");
        LOGGER.info("Creating Request Config... ");
        LOGGER.info("============================== ");
        
        return RequestConfig.custom()
                            .setSocketTimeout(config.timeOut())
                            .setConnectTimeout(config.timeOut())
                            .setConnectionRequestTimeout(config.timeOut())
                            .build();
    }
    
    private final SSLContext sslContext() throws GeneralSecurityException {
        LOGGER.info("");
        LOGGER.info("Creating SSL Context... ");
        LOGGER.info("============================== ");
        
        SSLContext sslContext = SSLContext.getInstance(config.tls());
        sslContext.init(null, null,  new SecureRandom());
        SSLContext.setDefault(sslContext);

        return sslContext;
    }
    
}
