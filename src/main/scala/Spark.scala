/*import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._
import org.apache.spark.{SparkConf, SparkContext}
*/
/*object Spark extends App {

  val spark = SparkSession
    .builder()
    .master("spark://spark-master:7077")
    .appName("Spark")
    .getOrCreate()
  val conf = new SparkConf()
    .setMaster("spark://spark-master:7077")
    .setAppName("Spark")
    .setJars()
  import spark.implicits._

  val Data = spark.read.option("header", "true").option("inferSchema", "true").csv("diagnoses_icd.csv")

  Data.show(2)
  //Data.printSchema()
  //Data.count()
}
*/
/*
val Data2 = Data.groupBy("SUBJECT_ID").count()
Data2.show()
Data2.select(max("count")).show()
Data2.select(mean("count")).show()
//Data.count()
val partitionWindow = Window.partitionBy($"SUBJECT_ID").orderBy($"ADMITTIME")
//SQL
val rankTest = rank().over(partitionWindow)
//Data.count()
val Data3 = Data.select($"*", rankTest as "RANK")
Data3.printSchema()

val Data4 = Data3.select(Data3("SUBJECT_ID"), Data3("HADM_ID"), Data3("ADMITTIME"), Data3("RANK"), Data3("ADMISSION_TYPE"))
//Data4.write.csv("myfile2.csv")


val leadTest = lead($"ADMITTIME", 1, null).over(partitionWindow)
val Data5 = Data4.select($"*", leadTest as "NEXT_VALUE")
Data5.show()
//val Data5 = Data3.select($"*", leadTest as "NEXT_VALUE")
//Data5.withColumn("TIME_DIFFERENCE", Data5("NEXT_VALUE").cast("long")-Data5("ADMITTIME").cast("long"))
//val Data6 = Data5.withColumn("TIME_DIFFERENCE", months_between($"NEXT_VALUE",$"ADMITTIME"))
//Data6.show(100)
val Data6 = Data5.withColumn("TIME_DIFFERENCE", datediff($"NEXT_VALUE",$"ADMITTIME"))

// Deleting elective next admission
val leadTest2 = lead($"ADMISSION_TYPE", 1, null).over(partitionWindow)
val Data7 = Data6.select($"*", leadTest2 as "NEXT_ADMISSION_TYPE")
Data7.show(100)
Data7.printSchema()

// Creating Response variable
def createResponse(time_difference: Double , next_admission_type: String ): Int = {
  if((time_difference <=1) && (next_admission_type != "ELECTIVE" )) 1
  else 0
}

createResponse(2, "nok")
createResponse(.5, "ELECTIVE")
createResponse(.4, "whatever")
Data7.select($"ADMISSION_TYPE").distinct().show()
val Data8_0 = Data7.filter( Data7("ADMISSION_TYPE") !== "NEWBORN")
Data8_0.select(("ADMISSION_TYPE")).distinct().show()
//val Data8 = Data8_0.withColumn("RESPONSE", when(($"TIME_DIFFERENCE" <=1) && ($"NEXT_ADMISSION_TYPE" !== "ELECTIVE" ), 1).otherwise(0))
val Data8 = Data8_0.withColumn("RESPONSE", when((($"TIME_DIFFERENCE" <= 40) && ($"NEXT_ADMISSION_TYPE" !== "ELECTIVE" )), 1).otherwise(0))
Data8.show(100)
//import org.apache.spark.sql.functions.udf
//val CR_UDF = udf((x: Double, y: String) => (createResponse(x,y)))
Data8.select($"ADMISSION_TYPE").distinct().show()
//val Data8 = Data7.withColumn("RESPONSE", CR_UDF($"TIME_DIFFERENCE", $"NEXT_ADMISSION_Type"))
//val Data8 = Data7.select(CR_UDF($"TIME_DIFFERENCE", $"NEXT_ADMISSION_Type") as "RESPONSE")
//Data8.show()
val Data9 = Data8.select( $"HADM_ID", $"RESPONSE")
Data9.show()
Data9.count()
val Data10= Data9.groupBy("RESPONSE").count()
Data10.select("count").distinct().show()
//https://stackoverflow.com/questions/50188579/how-to-load-csv-file-with-records-on-multiple-lines
// .option("ignoreLeadingWhiteSpace",true)
//  .option("ignoreTrailingWhiteSpace",true).option("parserLib", "univocity").
val Note = spark.read.option("header", "true").option("mode", "FAILFAST").option("escape", "\"")
  .option("multiline", "true").option("inferSchema", "true").csv("NOTEEVENTS.csv")
//Note.select($"SUBJECT_ID", $"CATEGORY", $"TEXT").show(20, false)
Note.printSchema()
//val sqlDF = spark.sql("SELECT * FROM csv.`NOTEEVENTS.csv`")
//sqlDF.show()

val Discharge_Note = Note.filter($"CATEGORY" === "Discharge summary")
Discharge_Note.show()
val DN_C = Discharge_Note.groupBy("HADM_ID").count()
DN_C.select("count").distinct().show()

// As we can see there are multiple notes for some patients. For now I just take into account the last one.

//val Discharge_Note_use = Discharge_Note.select($"SUBJECT_ID", $"HADM_ID", $"TEXT", $"CHARTDATE" )

val partitionWindow2 = Window.partitionBy($"HADM_ID").orderBy($"CHARTDATE".desc)
val rankTest2 = rank().over(partitionWindow2)
//val Data3 = Data.select($"*", rankTest as "RANK")
//val Ranked_Discharge_Note = Discharge_Note.select($"*", rankTest2 as "RANK2")
//Ranked_Discharge_Note.show()

val Last_Discharge_Note = Discharge_Note.withColumn("ROW_NUM", row_number().over(partitionWindow2)).where($"ROW_NUM" ===1)
Last_Discharge_Note.show()
Last_Discharge_Note.select($"ROW_NUM").distinct().show()
//val DN_C2 = Last_Discharge_Note.groupBy("HADM_ID").count()
//DN_C2.select("count").distinct().show()
//val Last_Discharge_Note = Ranked_Discharge_Note.select(max($"RANK2"))
//Last_Discharge_Note.show()

// Now we are sure that for each HADM_ID we only have 1 record.
//Delete New_born
//join
val Discharge_Text = Last_Discharge_Note.select($"SUBJECT_ID", $"HADM_ID", $"TEXT")
Discharge_Text.show()
//val Final_Dataset = Discharge_Text.join(Data9, Discharge_Text.col("HADM_ID") === Data9.col("HADM_ID"), "right")
val Final_Dataset = Discharge_Text.join(Data9, "HADM_ID")
Final_Dataset.show(1, false)
Final_Dataset.count()
//Final_Dataset.write.format("csv").save("C:/USF/619/Prepared-Data")


}
*/