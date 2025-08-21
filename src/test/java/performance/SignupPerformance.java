package performance;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class SignupPerformance extends Simulation {

    private static final String BASE_URL = System.getProperty("baseUrl", "https://app-stg.swiftassess.com");

    HttpProtocolBuilder httpProtocol = http
            .baseUrl(BASE_URL)
            .inferHtmlResources()
            .acceptEncodingHeader("gzip, deflate")
            .userAgentHeader("Gatling/Java");

    ScenarioBuilder scn = scenario("Signup Flow")
            .exec(http("GET /Signup")
                    .get("/Signup")
                    .check(status().in(200, 302)));

    {
        setUp(
                scn.injectClosed(
                        // Baseline: 10 concurrent users for 60s
                        constantConcurrentUsers(10).during(60),

                        // Stress: ramp from 10 -> 500 in 30s, then hold 500 for 60s
                        rampConcurrentUsers(10).to(500).during(30),
                        constantConcurrentUsers(500).during(60),

                        // Spike: 1000 concurrent users for 60s
                        constantConcurrentUsers(1000).during(60)
                )
        ).protocols(httpProtocol)
                .assertions(
                        global().successfulRequests().percent().gt(95.0),
                        global().responseTime().percentile(95.0).lt(2000),
                        global().responseTime().percentile(99.0).lt(5000)
                );
    }
}
