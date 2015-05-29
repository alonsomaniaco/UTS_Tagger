/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts_tagger;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Factory;
import gate.FeatureMap;
import gate.Resource;
import gate.Utils;
import gate.annotation.AnnotationImpl;
import gate.creole.*;
import gate.creole.metadata.CreoleResource;
import gov.nih.nlm.uts.webservice.finder.UiLabel;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author amaranto
 */

@CreoleResource(name = "UTS Tagger", 
                comment = "An UTS Tagger") 
public class UTS_Tagger extends AbstractLanguageAnalyser{

    @Override
    public Resource init() throws ResourceInstantiationException {
        return this;
    }

    @Override
    public void execute() throws ExecutionException {
        System.out.println("Iniciado");
       
       /**
        * Código para procesar el texto por nuestra propia cuenta.
        */
       /*String contenido=this.document.getContent().toString();
       String[] palabras=contenido.split(" ");
       for(String palabra:palabras){
           String palabraReplaced = palabra.replaceAll(",", "").replaceAll("\\.", "")
                   .replaceAll(";", "").replaceAll(":", "");
           System.out.println(palabraReplaced);
       }*/
        try {
            AnnotationSet set = this.document.getAnnotations().get("Token");
            AnnotationSet out = this.document.getAnnotations("miPlugin");
            //Llamado así para que utilice los datos del properties
            UTS_API api=new UTS_API();
            
            for (Iterator<Annotation> itAnotacion = set.iterator(); itAnotacion.hasNext();) {
                Annotation nextAnotation = itAnotacion.next();
                
                String termino=nextAnotation.getFeatures().get("string").toString();
                FeatureMap features=Factory.newFeatureMap();
                
                List<UiLabel> findTerm = api.findTerm(termino);
                for (Iterator<UiLabel> itUiLabel = findTerm.iterator(); itUiLabel.hasNext();) {
                    UiLabel nextUiLabel = itUiLabel.next();
                    features.put(nextUiLabel.getUi(),nextUiLabel.getLabel());
                }
                
                out.add(nextAnotation.getStartNode().getOffset()
                        , nextAnotation.getEndNode().getOffset(), "Mi etiqueta", features);
            }
            /*while (iterator.hasNext()) {
                Annotation an = iterator.next();
                
                String termino=an.getFeatures().get("string").toString();
                List<UiLabel> findTerm = api.findTerm(termino);
                

                out.add(an.getStartNode().getOffset(), an.getEndNode().getOffset()
                        , "Mi etiqueta", Utils.featureMap("type", "cosa"));
            }*/
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    @Override
    public void reInit() throws ResourceInstantiationException {
        System.out.println("Reiniciado");
    }
        
}
