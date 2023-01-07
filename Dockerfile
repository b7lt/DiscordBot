FROM amazoncorretto:latest
RUN mkdir /opt/app
COPY DiscordBot.jar /opt/app
COPY birthdays.json /opt/app
COPY config.txt /opt/app
WORKDIR /opt/app
CMD ["java", "-jar", "/opt/app/DiscordBot.jar"]