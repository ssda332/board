#!/bin/bash

ROOT_PATH="/home/ec2-user"
CD_PATH="/home/ec2-user/board-project"
JAR="$CD_PATH/application.jar"

APP_LOG="$CD_PATH/application.log"
ERROR_LOG="$CD_PATH/error.log"
START_LOG="$CD_PATH/start.log"

NOW=$(date +%c)

echo "[$NOW] > jar 실행" >> $START_LOG
nohup java -jar -Dspring.profiles.active=prod $ROOT_PATH/build/libs/board-0.0.1-SNAPSHOT.jar > $APP_LOG 2> $ERROR_LOG &

SERVICE_PID=$(pgrep -f $JAR)
echo "[$NOW] > 서비스 PID: $SERVICE_PID" >> $START_LOG