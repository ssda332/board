#!/bin/bash

ROOT_PATH="/home/ec2-user"
CD_PATH="/home/ec2-user/board-project"
JAR="$CD_PATH/application.jar"

APP_LOG="$CD_PATH/application.log"
ERROR_LOG="$CD_PATH/error.log"
START_LOG="$CD_PATH/start.log"

NOW=$(date +%c)

echo "[$NOW] $JAR 복사" >> $START_LOG
cp $ROOT_PATH/build/libs/board-0.0.1-SNAPSHOT.jar $JAR

echo "[$NOW] > $JAR 실행" >> $START_LOG
nohup java -jar $JAR > $APP_LOG 2> $ERROR_LOG &

SERVICE_PID=$(pgrep -f $JAR)
echo "[$NOW] > 서비스 PID: $SERVICE_PID" >> $START_LOG