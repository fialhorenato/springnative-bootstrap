FROM ghcr.io/graalvm/jdk:20.3.6

ADD . /build
WORKDIR /build

# For SDKMAN to work we need unzip & zip
RUN yum install -y unzip zip

RUN \
    # Install SDKMAN
    curl -s "https://get.sdkman.io" | bash; \
    source "$HOME/.sdkman/bin/sdkman-init.sh"; \
    # Install Maven
    sdk install gradle; \
    # Install GraalVM Native Image
    gu install native-image;

RUN source "$HOME/.sdkman/bin/sdkman-init.sh"

RUN native-image --version

RUN source "$HOME/.sdkman/bin/sdkman-init.sh" && ./compile.sh


# We use a Docker multi-stage build here so that we only take the compiled native Spring Boot app from the first build container
FROM oraclelinux:7-slim

MAINTAINER Jonas Hecht

# Add Spring Boot Native app spring-boot-graal to Container
COPY --from=0 "/build/native/nativeCompile/lula" lula

# Fire up our Spring Boot Native app by default
CMD [ "sh", "-c", "./lula" ]