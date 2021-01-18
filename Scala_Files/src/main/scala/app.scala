import org.apache.spark.sql.SparkSession

object app {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession
      .builder()
      .appName("spark-example")
      .master("local")
      .getOrCreate()

    val df = spark.read.option("header", true).option("inferSchema", true).csv("data/test_data.csv")
    df.show(10)

    df.printSchema()

  }

}
