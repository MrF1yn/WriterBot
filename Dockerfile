FROM openjdk:11
COPY WriterBot.jar /home/WriterBot.jar
CMD ["java","-jar","/home/WriterBot.jar"]