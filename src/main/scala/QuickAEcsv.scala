

import java.io.{BufferedWriter, FileWriter}

import au.com.bytecode.opencsv.CSVWriter
import org.apache.ctakes.core.sentence.SentenceDetectorCtakes
import org.apache.ctakes.typesystem.`type`.textsem.{DiseaseDisorderMention, EntityMention, IdentifiedAnnotation, SignSymptomMention}
import org.apache.ctakes.typesystem.`type`.refsem.{OntologyConcept, UmlsConcept}
import org.apache.uima.cas.text.AnnotationFS
import org.apache.uima.fit.`type`.Sentence
import org.apache.uima.jcas.JCas
import org.apache.uima.fit.util.JCasUtil
import org.apache.uima.jcas.tcas.Annotation
import org.apache.uima.cas

import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer


class QuickAEcsv extends org.apache.uima.fit.component.JCasAnnotator_ImplBase{
  override def process(aJCAS:JCas): Unit = {
    println(">>>> i'am working")
    val all = JCasUtil.selectAll(aJCAS)
    println("all.size(): " + all.size())
    //val diseaseOrDisorders = JCasUtil.select(aJCAS, classOf[DiseaseDisorderMention])
    //val diseaseOrDisorders_array = diseaseOrDisorders.toArray(new Array[DiseaseDisorderMention](0))
    var p = 0
    var positive_case = false
    var polarity_case = false
    var uncertainty_case = false
    val outputFile = "/Users/user/619/uima-annotator/src/main/scala/output/classification_csv.csv"
    val csvWriter = new CSVWriter(new FileWriter(outputFile,true))
    val outputFile2 = "/Users/user/619/uima-annotator/src/main/scala/output/riskfactors_csv.csv"
    val csvWriter2 = new CSVWriter(new FileWriter(outputFile2,true))

    val diseaseOrDisorders = JCasUtil.select(aJCAS, classOf[DiseaseDisorderMention])
    val diseaseOrDisorders_array = diseaseOrDisorders.toArray(new Array[DiseaseDisorderMention](0))
    for(d <- diseaseOrDisorders_array) {
      var umlsconcept = JCasUtil.select(d.getOntologyConceptArr(), classOf[UmlsConcept])
      var umlsconcept_array = umlsconcept.toArray(new Array[UmlsConcept](0))
      //println(">>>>>>>  print umls size: " + umlsconcept_array.size)
      var pos = 0

      for (con <- umlsconcept_array) {

        //println(">>>>>>>  printing cui:  " + con.getCui + "<<<<<<<  index: " + pos + "  <<<<<<<  high_index: " +p)
        if (con.getCui == "C0034065" | con.getCui == "C0919697" | con.getCui == "C2747923"
          | con.getCui == "C0157540" | con.getCui == "C1535887" | con.getCui == "C0151947"
          | con.getCui == "C0034074" | con.getCui == "C0520546" | con.getCui == "C2721578"
          | con.getCui == "C0151946" | con.getCui == "C4524050" | con.getCui == "C0392108"
          | con.getCui == "C1868769") {
          positive_case = true
          if (d.getPolarity == -1){
            polarity_case = true
          }
          if (d.getUncertainty == 1) {
            uncertainty_case = true
          }
          println(">>>>>>>  index: " + pos + "  <<<<<<<  high_index: " +p)
          println(">>>>>>> CUI: "+ con.getCui)
          println(">>>>>>>>>>>>>   POLARIY: " + d.getPolarity)
          println(">>>>>>>>>>>>>   Uncertainty: " + d.getUncertainty)
          println(">>>>>>>>>>>>>   Confidence: " + d.getConfidence +"\n\n")
        }
        pos += 1
      }
      //println("\n\n\n")
      p += 1

    }

    /*if (positive_case) {
      val signsymptom = JCasUtil.select(aJCAS, classOf[SignSymptomMention])
      val signsymptom_array = signsymptom.toArray(new Array[SignSymptomMention](0))

      var pos1 = 0
      for (s <- signsymptom_array) {
        var symp_concept = JCasUtil.select(s.getOntologyConceptArr(), classOf[UmlsConcept])
        var symp_concept_array = symp_concept.toArray(new Array[UmlsConcept](0))
        var pos2 = 0
        //println(">>>>>>>  print symp_umls size: " + symp_concept_array.size)
        for (sy <- symp_concept_array) {
          //println(">>>>>-- symptom cui --->>>>>>> " + sy.getCui + "<<<<<<<  index: " + pos2 + "  <<<<<<<  high_index: " +pos1)
          pos2 += 1
        }
        //println("\n\n\n")
        pos1 += 1
      }
    } */
    var class_pe = 0
    var pol_pe = 0
    var uncer_pe = 0
    var class_pe_str = new String()
    var pol_pe_str = new String()
    var uncer_pe_str = new String()
    if (positive_case){
      class_pe = 1
    }
    if (polarity_case){
      pol_pe = 1
    }
    if (uncertainty_case) {
      uncer_pe = 1
    }
    pol_pe_str = pol_pe.toString
    uncer_pe_str = uncer_pe.toString
    class_pe_str = class_pe.toString
    val csvFields = class_pe_str+','+pol_pe_str+','+uncer_pe_str
    csvWriter.writeNext(csvFields)
    csvWriter.close()

    if (class_pe == 1) {
      val signsymptom = JCasUtil.select(aJCAS, classOf[SignSymptomMention])
      val signsymptom_array = signsymptom.toArray(new Array[SignSymptomMention](0))
      var risk_factor_list = new String("")
      for (s <- signsymptom_array) {
        var symp_concept = JCasUtil.select(s.getOntologyConceptArr(), classOf[UmlsConcept])
        var symp_concept_array = symp_concept.toArray(new Array[UmlsConcept](0))
        var risk_factor= new String("")
        for (sy <- symp_concept_array) {
          if (s.getPolarity == -1){
            risk_factor = risk_factor +"ng"+sy.getCui+","
          }
          risk_factor=  risk_factor +sy.getCui+","

        }
        risk_factor_list= risk_factor_list + risk_factor
      }
      csvWriter2.writeNext(risk_factor_list)

    }
    else {
      csvWriter2.writeNext("")
    }
    csvWriter2.close()

  }

}
