#=======================================================================================================================
#  BUILD IMAGE
#  Build the webapp using an sbt base image. This will create a WAR that can be shoved into any supporting webserver
#  (i.e. Tomcat)
#=======================================================================================================================
# build our WAR inside a pre-defined sbt environment, so that Java versions all match
FROM sbtscala/scala-sbt:eclipse-temurin-21.0.5_11_1.10.5_3.3.4 AS build
LABEL stage=build

# copy source files
WORKDIR /tmp/build
COPY . /tmp/build

# package WAR
RUN sbt package

#=======================================================================================================================
#  DEPLOYMENT WEBAPP
#  Deploy our war to a Tomcat webserver
#=======================================================================================================================
# use tomcat base image
FROM tomcat:11-jre21 AS webapp

# copy GTFS data into GTFS subdirectory
COPY gtfs $CATALINA_HOME/gtfs

# remove default ROOT webapp
RUN rm -rf $CATALINA_HOME/webapps/ROOT

# copy current version of cta-tracker from build image
COPY --from=build /tmp/build/target/scala-3.3.4/cta-tracker-backend_*.war $CATALINA_HOME/webapps/ROOT.war