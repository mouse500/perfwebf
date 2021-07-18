package com.sypark.problem;

import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.*;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@SpringBootApplication

public class ProblemApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProblemApplication.class, args);
	}

	@Bean
	public RouterFunction<ServerResponse> routes() {
		return RouterFunctions.route()
				.GET("/ping", this::handlerPing)
				.GET("/hashes", this::handlerHashes)
				.build();
	}

	private Mono<ServerResponse> handlerPing(ServerRequest request)
	{
		log.debug("/ping");
		return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).bodyValue("PONG");
	}

	private Mono<ServerResponse> handlerHashes(ServerRequest request)
	{
		log.debug("/hashes");
		return Mono.just("")
				.flatMap(this::workloadWithHttpCall)
				.flatMap(this::workloadWithHttpCall)
				.flatMap(this::workloadWithHttpCall)
				.flatMap(this::workloadWithHttpCall)
				.flatMap(this::workloadWithHttpCall)
				.flatMap(this::workloadWithHttpCall)
				.flatMap(this::workloadWithHttpCall)
				.flatMap(this::workloadWithHttpCall)
				.flatMap(this::workloadWithHttpCall)
				.flatMap(this::workloadWithHttpCall)
				.flatMap(hashes -> ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).bodyValue(hashes));
	}

	WebClient webClient = WebClient.builder().baseUrl("http://127.0.0.1:8088/").build();
	private Mono<String> workloadWithHttpCall(String prevHash) {
		return webClient.get()
				.uri(URI.create("http://127.0.0.1:8088/data"))
				.exchange()
				.flatMap(clientResponse -> clientResponse.bodyToMono(String.class))
				.map(data -> {
					StringBuffer hexString = new StringBuffer();
					try {
						// MD5 Hash
						MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
						digest.update((data+prevHash).getBytes());
						byte messageDigest[] = digest.digest();

						// Hex String
						for (int i = 0; i < messageDigest.length; i++) {
							hexString.append(Integer.toHexString((messageDigest[i] & 0xff)+0x100).substring(1));
							//hexString.append(Integer.toHexString(messageDigest[i]));
						}
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
					}
					return hexString.toString();
				});
	}


}