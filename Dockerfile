FROM openjdk:8-jdk
EXPOSE 8080:8080
RUN mkdir /app
COPY ./build/install/distributed-systems-with-load-tests /app/
WORKDIR /app/bin
CMD ["./distributed-systems-with-load-tests"]
