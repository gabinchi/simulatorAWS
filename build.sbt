name := "simulatorAWS"

version := "0.1"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  "org.apache.kafka" % "kafka_2.12" % "0.11.0.0",
  "com.amazonaws" % "aws-java-sdk-s3" % "1.11.214"
)

mainClass in Compile := Some("example.simulator.Simulate")
