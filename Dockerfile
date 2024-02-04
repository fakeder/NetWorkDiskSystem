#基于ubuntu-java运行
FROM adoptopenjdk/openjdk8
#将打包好的jar包复制到容器中 重命名为DockerTest.jar
COPY target/NetWorkDiskSystem-0.0.1-SNAPSHOT.jar NetWorkDiskSystem.jar
#容器启动时运行
#CMD命令 容器启动时执行命令
CMD java -jar NetWorkDiskSystem.jar