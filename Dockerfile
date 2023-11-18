FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
ARG SPRING_PROFILE=dev

COPY ${JAR_FILE} anifriends.jar

ENV spring_profile=${SPRING_PROFILE}

ENTRYPOINT java -jar anifriends.jar \
            --spring.profiles.active=${spring_profile}
