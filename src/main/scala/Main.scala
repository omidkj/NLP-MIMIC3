import java.io.{BufferedWriter, File, FileOutputStream, FileWriter}

import org.apache.ctakes.core.cr.TextReader
import org.apache.uima.fit.factory.{AnalysisEngineFactory, CollectionReaderFactory, JCasFactory}
import org.apache.uima.fit.pipeline.SimplePipeline
import org.apache.commons.io.FileUtils
import au.com.bytecode.opencsv.CSVWriter
import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.collection.JavaConverters._
import scala.io._
import java.io._
object Main extends App {
    

}


object TextXMLAE2 extends App {
  val crd = CollectionReaderFactory.createReaderDescription(
    classOf[TextReader],
    TextReader.PARAM_FILES, "notes/test1.txt"
  )
  // This is similar to loading an AE engine
  val ae = AnalysisEngineFactory.createEngineDescription(
    "desc.ctakes-clinical-pipeline.desc.analysis_engine.AggregatePlaintextUMLSProcessor"
  )

  val qae = AnalysisEngineFactory.createEngineDescription(
    classOf[QuickAE3]
  )

  SimplePipeline.runPipeline(crd,ae,qae)

}

object TestAE extends App{
  println("main wroks")
  val jCas = JCasFactory.createJCas
  val ctakes = AnalysisEngineFactory.createEngine(
    "desc.ctakes-clinical-pipeline.desc.analysis_engine.AggregatePlaintextUMLSProcessor"
  )
  val analysisEngine = AnalysisEngineFactory.createEngine(

    classOf[QuickAE3]
  )
  // analysisEngine.process(jCas)
  val outputFile = new BufferedWriter(new FileWriter("/Users/user/619/uima-annotator/src/main/scala/output/classification.csv"))
  val csvWriter = new CSVWriter(outputFile)
  val csvFields = Array("class")
  var listOfRecords = new ListBuffer[Array[String]]()
  listOfRecords += csvFields
  csvWriter.writeAll(listOfRecords.toList.asJava)
  outputFile.close()
  val outputFile2 = new BufferedWriter(new FileWriter("/Users/user/619/uima-annotator/src/main/scala/output/riskfactors.csv"))
  val csvWriter2 = new CSVWriter(outputFile2)
  val csvFields2 = Array("RiskFactors")
  var listOfRecords2 = new ListBuffer[Array[String]]()
  listOfRecords2 += csvFields2
  csvWriter2.writeAll(listOfRecords2.toList.asJava)
  outputFile2.close()
  val outputFile3 = new BufferedWriter(new FileWriter("/Users/user/619/uima-annotator/src/main/scala/output/filename.csv"))
  val csvWriter3 = new CSVWriter(outputFile3)
  val csvFields3 = Array("file_name")
  var listOfRecords3 = new ListBuffer[Array[String]]()
  listOfRecords3 += csvFields3
  csvWriter3.writeAll(listOfRecords3.toList.asJava)
  outputFile3.close()
  val outputFile4 = "/Users/user/619/uima-annotator/src/main/scala/output/filename.csv"
  val csvWriter4 = new CSVWriter(new FileWriter(outputFile4,true))

  val dir = new File("/Users/user/619/uima-annotator/src/main/scala/notes")
  val extensions: Array[String] = Array[String]("txt")
  val files = FileUtils.listFiles(dir,  extensions, false)
  val files_array = files.toArray

  for (f <- files_array){
    var name_string = f.toString()
    var fileContents = Source.fromFile(name_string).getLines.mkString
    println(">>>>>>>>>>>>>>>>>>>>>> processing file: " + name_string)
    csvWriter4.writeNext(name_string)
    jCas.reset()
    jCas.setDocumentText(fileContents)
    SimplePipeline.runPipeline(jCas, ctakes, analysisEngine)
    //BufferedSource.close()
    println(">>>>>>>>>>>>>>>>>>>>>> processing ends ")
    //println("<<<<<<<<<<<<<<<>>>>>>>>>>>   "+jCas.getontologyConceptArr)

    
  }
  csvWriter4.close()

}

object GenerateXML extends App{
  val aed = AnalysisEngineFactory.createEngineDescription(
    classOf[QuickAE3]
  )
  aed.toXML(new FileOutputStream("QuickAE.xml"))
}

object TestAECSV extends App{
  println("main wroks")
  val jCas = JCasFactory.createJCas
  val ctakes = AnalysisEngineFactory.createEngine(
    "desc.ctakes-clinical-pipeline.desc.analysis_engine.AggregatePlaintextUMLSProcessor"
  )
  val analysisEngine = AnalysisEngineFactory.createEngine(

    classOf[QuickAEcsv]
  )
  // analysisEngine.process(jCas)
  val outputFile = new BufferedWriter(new FileWriter("/Users/user/619/uima-annotator/src/main/scala/output/classification_csv.csv"))
  val csvWriter = new CSVWriter(outputFile)
  val csvFields = Array("class")
  var listOfRecords = new ListBuffer[Array[String]]()
  listOfRecords += csvFields
  csvWriter.writeAll(listOfRecords.toList.asJava)
  outputFile.close()
  val outputFile2 = new BufferedWriter(new FileWriter("/Users/user/619/uima-annotator/src/main/scala/output/riskfactors_csv.csv"))
  val csvWriter2 = new CSVWriter(outputFile2)
  val csvFields2 = Array("RiskFactors")
  var listOfRecords2 = new ListBuffer[Array[String]]()
  listOfRecords2 += csvFields2
  csvWriter2.writeAll(listOfRecords2.toList.asJava)
  outputFile2.close()

  val bufferedSource = io.Source.fromFile("/Users/user/619/uima-annotator/src/main/scala/noLine_data.csv")

  for (f <- bufferedSource.getLines.drop(1)){
    var columns = f.split("\t").map(_.trim)
    //println(s"${columns(17)}")
    jCas.reset()
    jCas.setDocumentText(columns(17))
    SimplePipeline.runPipeline(jCas, ctakes, analysisEngine)
    //BufferedSource.close()
    println(">>>>>>>>>>>>>>>>>>>>>> processing ends ")
    //println("<<<<<<<<<<<<<<<>>>>>>>>>>>   "+jCas.getontologyConceptArr)


  }
  bufferedSource.close
}
