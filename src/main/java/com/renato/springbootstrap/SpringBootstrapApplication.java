package com.renato.springbootstrap;

import io.r2dbc.postgresql.PostgresqlConnectionFactoryProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.nativex.hint.NativeHint;
import org.springframework.nativex.hint.TypeHint;

import java.net.URI;
import java.time.Instant;
import java.time.ZonedDateTime;

@NativeHint(trigger = PostgresqlConnectionFactoryProvider.class, types = {@TypeHint(types = { Instant[].class, ZonedDateTime[].class, URI[].class }, access = {})})
@SpringBootApplication
public class SpringBootstrapApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringBootstrapApplication.class, args);
	}
}
