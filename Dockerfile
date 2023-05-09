FROM amazoncorretto:17-alpine
RUN mkdir /opt/app
COPY out/artifacts/BruhBot_jar/DiscordBot.jar /opt/app
COPY birthdays.json /opt/app
COPY config.txt /opt/app
WORKDIR /opt/app
CMD ["java", "-jar", "/opt/app/DiscordBot.jar"]