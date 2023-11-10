package com.example.servertoserverdat22c;

import com.example.servertoserverdat22c.dtos.Gender;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class RemoteApiTester implements CommandLineRunner {

    private Mono<String> callSlowEndpoint(){
        Mono<String> slowResponse = WebClient.create()
                .get()
                .uri("http://localhost:8080/random-string-slow")
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(e-> System.out.println("UUUPS : "+e.getMessage()));
        return slowResponse;
    }

    public void callEndpointBlocking(){
        long start = System.currentTimeMillis();
        List<String> ramdomStrings = new ArrayList<>();

        Mono<String> slowResponse = callSlowEndpoint();
        ramdomStrings.add(slowResponse.block()); //Three seconds spent

        slowResponse = callSlowEndpoint();
        ramdomStrings.add(slowResponse.block());//Three seconds spent

        slowResponse = callSlowEndpoint();
        ramdomStrings.add(slowResponse.block());//Three seconds spent
        long end = System.currentTimeMillis();
        ramdomStrings.add(0,"Time spent BLOCKING (ms): "+(end-start));

        //System.out.println(ramdomStrings.stream().collect(Collectors.joining(",")));
        System.out.println(String.join(",",ramdomStrings));
    }

    public void callSlowEndpointNonBlocking() {
        long start = System.currentTimeMillis();
        Mono<String> sr1 = callSlowEndpoint();
        Mono<String> sr2 = callSlowEndpoint();
        Mono<String> sr3 = callSlowEndpoint();

        var rs = Mono.zip(sr1, sr2, sr3).map(tuple3 -> {
            List<String> randomStrings = new ArrayList<>();
            randomStrings.add(tuple3.getT1());
            randomStrings.add(tuple3.getT2());
            randomStrings.add(tuple3.getT3());
//            long end = System.currentTimeMillis();
//            randomStrings.add(0, "Time spent NON-BLOCKING (ms): " + (end - start));
            return randomStrings;
        });
        List<String> randoms = rs.block(); //We only block when all the three Mono's has fulfilled
        long end = System.currentTimeMillis();
        randoms.add(0, "Time spent NON-BLOCKING (ms): " + (end - start));

        System.out.println(randoms.stream().collect(Collectors.joining(",")));
    }

    Mono<Gender> getGenderForName(String name) {
        WebClient client = WebClient.create();
        Mono<Gender> gender = client.get()
                .uri("https://api.genderize.io?name="+name)
                .retrieve()
                .bodyToMono(Gender.class);
        return gender;
    }

    List<String> names = Arrays.asList("lars", "peter", "sanne", "kim", "david", "maja");

    public void getGendersBlocking() {
        long start = System.currentTimeMillis();
        List<Gender> genders = names.stream().map(name -> getGenderForName(name).block()).toList();
        long end = System.currentTimeMillis();
        System.out.println("Time for six external requests, BLOCKING: "+ (end-start));
        System.out.println(genders);
    }

    public void getGendersNonBlocking() {
        long start = System.currentTimeMillis();
        var genders = names.stream().map(name -> getGenderForName(name)).toList();
        Flux<Gender> flux = Flux.merge(Flux.concat(genders));
        List<Gender> res = flux.collectList().block();
        long end = System.currentTimeMillis();
        System.out.println("Time for six external requests, NON-BLOCKING: "+ (end-start));
        System.out.println(res);
    }



    @Override
    public void run(String... args) throws Exception {
        //...
        //String randomString = callSlowEndpoint().block();
        //System.out.println(randomString);

        //callEndpointBlocking();
        //callSlowEndpointNonBlocking();

        //getGendersBlocking();
        getGendersNonBlocking();
    }
}

