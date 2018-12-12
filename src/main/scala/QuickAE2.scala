

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


class QuickAE2 extends org.apache.uima.fit.component.JCasAnnotator_ImplBase{
  override def process(aJCAS:JCas): Unit = {
    println(">>>> i'am working")
    val all = JCasUtil.selectAll(aJCAS)
    println("all.size(): " + all.size())
    val diseaseOrDisorders = JCasUtil.select(aJCAS, classOf[DiseaseDisorderMention])
    println(">>> something:  : "+ diseaseOrDisorders.getClass)
    val diseaseOrDisorders_array = diseaseOrDisorders.toArray(new Array[DiseaseDisorderMention](0))
    println(">>> something-----:  : "+ diseaseOrDisorders_array.getClass)
    var p = 0
    var positive_case = false

    for(d <- diseaseOrDisorders_array) {
      var umlsconcept = JCasUtil.select(d.getOntologyConceptArr(), classOf[UmlsConcept])
      var umlsconcept_array = umlsconcept.toArray(new Array[UmlsConcept](0))
      println(">>>>>>>  print umls size: " + umlsconcept_array.size)
      var pos = 0

      for (con <- umlsconcept_array) {

        println(">>>>>>>  printing cui:  " + con.getCui + "<<<<<<<  index: " + pos + "  <<<<<<<  high_index: " +p)
        if (con.getCui == "C0034065") {
          positive_case = true
        }
        pos += 1
      }
      println("\n\n\n")
      p += 1

    }
    /*
    if (positive_case) {
      val signsymptom = JCasUtil.select(aJCAS, classOf[SignSymptomMention])
      val signsymptom_array = signsymptom.toArray(new Array[SignSymptomMention](0))

      var pos1 = 0
      for (s <- signsymptom_array) {
        var symp_concept = JCasUtil.select(s.getOntologyConceptArr(), classOf[UmlsConcept])
        var symp_concept_array = symp_concept.toArray(new Array[UmlsConcept](0))
        var pos2 = 0
        println(">>>>>>>  print symp_umls size: " + symp_concept_array.size)
        for (sy <- symp_concept_array) {
          println(">>>>>-- symptom cui --->>>>>>> " + sy.getCui + "<<<<<<<  index: " + pos2 + "  <<<<<<<  high_index: " +pos1)
          pos2 += 1
        }
      println("\n\n\n")
      pos1 += 1
      }
    }
    */
    // println("------------------- disease or disorder "+diseaseOrDisorders)
    //    val rawText = aJCAS.getDocumentText
    //    println(">>>>>> raw text: "+rawText)
    //    val regex = "[A-Z]+".r
    //    val matches = regex.findAllMatchIn(rawText)
    //    println("matches:")
    //    for (m <-matches){
    //      println("> "+m)
    //      val annotation = new Annotation(aJCAS)
    //      annotation.setBegin(m.start)
    //      annotation.setEnd(m.end)
    //      annotation.addToIndexes()
    //      println("annotation added: " + annotation)
    //    }
  }

}
