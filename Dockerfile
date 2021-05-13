FROM openjdk:8-jdk
EXPOSE 8080:8080
RUN mkdir /app
COPY ./build/install/distributed-systems-with-load-tests/bin/distributed-systems-with-load-tests/ /app/
WORKDIR /app
CMD ["./distributed-systems-with-load-tests"]
