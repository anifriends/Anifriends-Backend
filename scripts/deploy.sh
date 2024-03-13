#!/bin/bash

# 변수 설정
build_jar=$(ls /home/ubuntu/app/deploy/build/libs/*.jar)
jar_name=$(basename "$build_jar")
deploy_path=/home/ubuntu/app/deploy/
deploy_log=$deploy_path/deloy.log

echo ">>> build 파일명: $jar_name" >> $deploy_log

echo ">>> build 파일 복사" >> $deploy_log
cp "$build_jar" $deploy_path

echo ">>> 현재 실행중인 애플리케이션 pid 확인" >> $deploy_log
CURRENT_PID=$(pgrep -f "$jar_name")

if [ -z "$CURRENT_PID" ]
then
  echo ">>> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> $deploy_log
else
  echo ">>> kill -15 $CURRENT_PID" >> $deploy_log
  kill -15 "$CURRENT_PID"
  sleep 5
fi

DEPLOY_JAR=$deploy_path$jar_name
echo ">>> DEPLOY_JAR 배포"    >> $deploy_log
nohup java -jar "$DEPLOY_JAR" --spring.profiles.active=dev >> /home/ubuntu/app.log 2>$deploy_path/deploy_err.log &
