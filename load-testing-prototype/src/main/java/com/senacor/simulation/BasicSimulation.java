package com.senacor.simulation;

import io.gatling.core.json.JsonParsers;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.Duration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class BasicSimulation extends Simulation {
    Iterator<Map<String, Object>> feeder =
            Stream.generate((Supplier<Map<String, Object>>) () -> new HashMap<>() {{
                put("username", RandomStringUtils.randomAlphanumeric(5, 6));
                put("password", RandomStringUtils.randomAlphanumeric(10, 15));
                put("firstName", RandomStringUtils.randomAlphabetic(1).toUpperCase() + RandomStringUtils.randomAlphabetic(4, 11).toLowerCase());
                put("lastName", RandomStringUtils.randomAlphabetic(1).toUpperCase() + RandomStringUtils.randomAlphabetic(6, 14).toLowerCase());
                put("newFirstName", RandomStringUtils.randomAlphabetic(1).toUpperCase() + RandomStringUtils.randomAlphabetic(5, 7).toLowerCase());
                put("title", RandomStringUtils.randomAlphabetic(1).toUpperCase() + RandomStringUtils.randomAlphabetic(5, 15).toLowerCase());
                put("subtitle", RandomStringUtils.randomAlphabetic(1).toUpperCase() + RandomStringUtils.randomAlphabetic(10, 15).toLowerCase());
                put("isbn", "978" + RandomStringUtils.randomNumeric(10));
            }}).iterator();

    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080") // Here is the root for all relative URLs
            .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9") // Here are the common headers
            .acceptEncodingHeader("gzip, deflate, br")
            .acceptLanguageHeader("en-US,en;q=0.9")
            .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Safari/537.36");

    ScenarioBuilder scn = scenario("Default scenario")
            .feed(feeder) // feed predefined data
            .exec(http("req01 get / 401")
                    .get("/").check(status().is(401)))
            .pause(3, 6)
            // user visits base page and resource is not mapped
            .exec(http("req02 get /api 404")
                    .get("/api").check(status().is(404)))
            .pause(5, 7)
            // user checks api private unauthenticated
            .exec(http("req03 get /api/private 401")
                    .get("/api/private").check(status().is(401)))
            .pause(3)
            // user authenticates
            .exec(http("req04 post /api/public/authenticate")
                    .post("/api/public/authenticate")
                    .asJson()
                    .body(StringBody("{\"username\":\"#{username}\",\"password\":\"#{password}\"}"))
                    .check(header("Authorization").saveAs("jwt"))
                    .check(status().is(200)))
            .pause(1, 2)
            // user checks api resource authenticated
            .exec(http("req05 get /api/private")
                    .get("/api/private").check(status().is(200))
                    .header("Authorization", "Bearer #{jwt}"))
            .pause(3)
            // user visits authors endpoint and scrolls through authors
            .exec(http("req06 get /api/private/authors")
                    .get("/api/private/authors").check(status().is(200))
                    .header("Authorization", "Bearer #{jwt}"))
            .pause(2)
            // user looks at specific author
            .exec(http("req07 get /api/private/authors/1")
                    .get("/api/private/authors/1").check(status().is(200))
                    .header("Authorization", "Bearer #{jwt}"))
            .pause(4, 6)
            // user returns to author overview to look at other authors
            .exec(http("req08 get /api/private/authors")
                    .get("/api/private/authors").check(status().is(200))
                    .header("Authorization", "Bearer #{jwt}"))
            .pause(5)
            // user visits specific author
            .exec(http("req09 get /api/private/authors/3")
                    .get("/api/private/authors/3").check(status().is(200))
                    .header("Authorization", "Bearer #{jwt}"))
            .pause(3, 8)
            // user edits author
            .exec(http("req10 post /api/private/authors")
                    .post("/api/private/authors")
                    .asJson()
                    .body(StringBody("{\"firstName\":\"#{firstName}\",\"lastName\":\"#{lastName}\"}"))
                    .check(bodyString().transform(data -> new JsonParsers().parse(data).get("id").asInt()).saveAs("authorId"))
                    .check(status().is(201))
                    .header("Authorization", "Bearer #{jwt}"))
            .pause(Duration.ofMillis(378))
            // back to overview
            .exec(http("req11 get /api/private/authors")
                    .get("/api/private/authors").check(status().is(200))
                    .header("Authorization", "Bearer #{jwt}"))
            .pause(1, 2)
            // look at newly created author
            .exec(http("req12 get /api/private/authors/{authorId}")
                    .get("/api/private/authors/#{authorId}").check(status().is(200))
                    .header("Authorization", "Bearer #{jwt}"))
            .pause(3, 5)
            // edit author and change first name
            .exec(http("req13 patch /api/private/authors/{authorId}")
                    .patch("/api/private/authors/#{authorId}")
                    .asJson()
                    .body(StringBody("{\"firstName\":\"#{newFirstName}\"}"))
                    .check(bodyString().transform(data -> new JsonParsers().parse(data).get("firstName").asText()).isEL("#{newFirstName}"))
                    .check(status().is(200))
                    .header("Authorization", "Bearer #{jwt}"))
            .pause(2, 3)
            // add book with new author
            .exec(http("req14 post /api/private/books")
                    .post("/api/private/books")
                    .asJson()
                    .body(StringBody("{\"title\":\"#{title}\",\"subtitle\":\"#{subtitle}\",\"isbn\":\"#{isbn}\",\"author\":\"authors/#{authorId}\"}"))
                    .check(bodyString().transform(data -> new JsonParsers().parse(data).get("id").asInt()).saveAs("bookId"))
                    .check(status().is(201))
                    .header("Authorization", "Bearer #{jwt}"))
            .pause(Duration.ofMillis(140))
            // set subtitle to null
            .exec(http("req15 patch /api/private/books/{bookId}")
                    .patch("/api/private/books/#{bookId}")
                    .asJson()
                    .body(StringBody("{\"subtitle\":null}"))
                    .check(bodyString().transform(data -> new JsonParsers().parse(data).get("subtitle").isNull()).is(true))
                    .check(status().is(200))
                    .header("Authorization", "Bearer #{jwt}"))
            .pause(4)
            // delete book
            .exec(http("req16 delete /api/private/books/{bookId}")
                    .delete("/api/private/books/#{bookId}")
                    .check(bodyString().is(""))
                    .check(status().is(204))
                    .header("Authorization", "Bearer #{jwt}"))
            .pause(2, 7)
            // add book again
            .exec(http("req17 post /api/private/books")
                    .post("/api/private/books")
                    .asJson()
                    .body(StringBody("{\"title\":\"#{title}\",\"subtitle\":\"#{subtitle}\",\"isbn\":\"#{isbn}\",\"author\":\"authors/#{authorId}\"}"))
                    .check(bodyString().transform(data -> new JsonParsers().parse(data).get("id").asInt()).saveAs("bookId"))
                    .check(status().is(201))
                    .header("Authorization", "Bearer #{jwt}"))
            .pause(2)
            // delete author
            .exec(http("req18 delete /api/private/authors/{authorId}")
                    .delete("/api/private/authors/#{authorId}")
                    .check(bodyString().is(""))
                    .check(status().is(204))
                    .header("Authorization", "Bearer #{jwt}"))
            .pause(5, 6)
            // check if book is deleted as well
            .exec(http("req19 get /api/private/books/{bookId} 404")
                    .get("/api/private/books/#{bookId}").check(status().is(404))
                    .header("Authorization", "Bearer #{jwt}"));

    {
        setUp(scn.injectOpen(
                rampUsers(30).during(15),
                rampUsersPerSec(5).to(10).during(30).randomized())
        ).protocols(httpProtocol);
    }
}
