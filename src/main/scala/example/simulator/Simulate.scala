package example.simulator

import java.util.Properties

import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.GetObjectRequest
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.io.Source

object Simulate {
  def main(args: Array[String]): Unit = {
    val usage =
      """
        |records per second
        |IP
        |S3 Bucket Name
        |S3 Key Name
      """.stripMargin
    if (args.length != 4)
      throw new java.lang.IllegalArgumentException("Missing argument")

    // Parse args
    val (recPerSec, ip, s3bucketName, s3keyName) = (args(0).toInt, args(1), args(2), args(3))

    // Load example data which will be sent
    val s3Client = AmazonS3ClientBuilder.defaultClient()
    val s3Object = s3Client.getObject(new GetObjectRequest(s3bucketName, s3keyName))

    val source = Source.fromInputStream(s3Object.getObjectContent)
    val lines = source.getLines.take(100).toArray

    // Set Kafka Properties
    val kafkaProps = new Properties
    kafkaProps.put("bootstrap.servers", f"$ip:9092")
    kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    // Publish messages
    println("This is the new jar")
    println("*** Publishing messages")
    val producer = new KafkaProducer[String, String](kafkaProps)
    var epoch = 0
    while(true) {
      println(f"Processing epoch: $epoch")
      val sleepInMs = 1000/recPerSec
      for ((l, i) <- lines.zipWithIndex) {
        Thread.sleep(sleepInMs)
        val record = new ProducerRecord("model-data", f"$epoch%09d_$i%06d", l)
        try {
          producer.send(record).get()
        } catch {
          case e: Exception => println("Got unknown exception: " + e)
        }
      }
      epoch += 1
    }
    producer.close()
  }
}
