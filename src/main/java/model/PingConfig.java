package model;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;

public class PingConfig {

    private final int timeOut;
    
    private final String tls;
    
    private final URI uri;
    
    public PingConfig(int timeOut, String tls, String uri) throws URISyntaxException {
        this.timeOut = timeOut;
        this.tls = tls;
        this.uri = new URIBuilder(uri).build();
    }

    public int timeOut() {
        return timeOut;
    }

    public String tls() {
        return tls;
    }

    public URI uri() {
        return uri;
    }

    @Override
    public String toString() {
        return "PingConfig [timeOut=" + timeOut + ", tls=" + tls + ", uri=" + uri + "]";
    }
    
    
}
