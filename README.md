# simulatorAWS

The purpose of this project was to test the deployment of a Spark ML model with Spark Streaming and Apache Kafka in AWS. This project has two components. This is the first component. The second component is the [rtplAWS](https://github.com/gabinchi/rtplAWS/tree/master). This component essentially does the following:
1.  Randomly pull records from the original dataset used to create the Spark ML model.
2.  Publish the records to the Apache Kafka server through a Kafka Producer.


