FROM openjdk:8

ENV P_HOME /opt/perfwebf
ENV JAR_FILE ROOT.jar

RUN set -e \
    && mkdir -p ${P_HOME}

# Install dependencies
RUN apt-get update && \
apt-get install -y nodejs

##COPY to the home
COPY . ${P_HOME}/
EXPOSE 8080
WORKDIR ${P_HOME}

RUN ./gradlew build -x test
RUN rm -f ./${JAR_FILE}
RUN mv ./build/libs/${JAR_FILE} .

CMD ./run.sh
