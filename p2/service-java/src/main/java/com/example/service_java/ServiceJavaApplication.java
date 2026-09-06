package com.example.service_java;

import io.r2dbc.spi.Readable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@SpringBootApplication
public class ServiceJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceJavaApplication.class, args);
    }

}

class SqlReactiveUserDetailsService implements ReactiveUserDetailsService {

    private final DatabaseClient db;

    SqlReactiveUserDetailsService(DatabaseClient db) {
        this.db = db;
    }

    @Override
    public Mono<UserDetails> findByUsername(String user) {
        return db
                .sql("select * from users where username = :name ")
                .bind("name", user)
                .map((Function<Readable, UserDetails>) readable -> new User(
                        (String) readable.get("username"),
                        (String) readable.get("password"),
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))))
                .first();
    }
}

// https://start.spring.io/#!type=maven-project&language=java&platformVersion=4.0.8&packaging=jar&configurationFileFormat=properties&jvmVersion=25&groupId=com.example&artifactId=service-java&packageName=com.example.service-java&dependencies=webflux,r2dbc,jte,data-r2dbc,postgresql,security,spring-security-webauthn
@Configuration
class SecurityConfiguration {

    @Bean
    ReactiveUserDetailsService userDetailsService(DatabaseClient db) {
        return new SqlReactiveUserDetailsService(db);
    }

    @Bean
    Customizer<HttpSecurity> securityCustomizer() {
        return http -> http
                .authorizeHttpRequests(a -> a
                        .requestMatchers("/dog*").permitAll()
                        .anyRequest().authenticated()
                );
    }
}

@Controller
@ResponseBody
class MeController {

    @GetMapping("/me")
    Mono<Map<String, String>> me(Mono<Principal> principal) {
        return principal.map(Principal::getName).map(name -> Map.of("name", name));
    }
}

@Controller
@ResponseBody
class DogController {

    private final DogRepository repository;

    DogController(DogRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/dog")
    Mono<Dog> dogMono(@RequestParam String name) {
        return repository.findByName(name);
    }

    @GetMapping("/dogs")
    Flux<Dog> dogs() {
        return repository.findAll();
    }
}

interface DogRepository extends ReactiveCrudRepository<Dog, Integer> {
    Mono<Dog> findByName(String name);
}

record Dog(@Id int id, String name, String description) {
}