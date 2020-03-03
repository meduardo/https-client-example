package main;

import java.net.URISyntaxException;

import model.PingConfig;
import model.PingTask;

public class PingApplication {

    public static void main(String[] args) throws URISyntaxException {
        
        PingTask.with(
                    new PingConfig(10000, "TLSv1.2", args[0])
                )
                .ping();
        
    }
}
