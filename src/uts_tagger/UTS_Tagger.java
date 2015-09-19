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
import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.CreoleResource;
import gate.creole.metadata.Optional;
import gate.creole.metadata.RunTime;
import gov.nih.nlm.uts.webservice.finder.UiLabel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author amaranto
 */
@CreoleResource(name = "UTS Tagger",
        comment = "An UTS Tagger")
public class UTS_Tagger extends AbstractLanguageAnalyser {

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
            /*AnnotationSet set = this.document.getAnnotations().get("Token");
             AnnotationSet out = this.document.getAnnotations("miPlugin");*/

            AnnotationSet set = this.document.getAnnotations().get(this.inputAnnotationSetName);
            AnnotationSet out = this.document.getAnnotations(this.outputAnnotationSetName);

            UTS_API api;
            
            if (this.paramOrProperties==ParamOrPropertiesOptions.Properties) {
                api=new UTS_API();
            } else {
                if(this.UTSUser!=""){
                    throw new ResourceInstantiationException(
                            "The tagger requires yout UTS User when you select Params config "
                                    + "in paramsOrProperties parameter.");
                }
                if(this.UTSPassword!=""){
                    throw new ResourceInstantiationException(
                            "The tagger requires your UTS Password when you select Params config "
                                    + "in paramsOrProperties parameter.");
                }
                if(this.UMLSRelease!=""){
                    throw new ResourceInstantiationException(
                            "The tagger requires UML Release dictionary to use when you select Params config "
                                    + "in paramsOrProperties parameter.");
                }
                if(this.termsToSearch.isEmpty()){
                    throw new ResourceInstantiationException(
                            "The tagger requires a list of tokens to search in UTS api.");
                }
                
                api=new UTS_API(this.UTSUser, this.UTSPassword, this.UMLSRelease);
            }
            
            //Llamado así para que utilice los datos del properties
            //UTS_API api = new UTS_API();

        
            for (Iterator<Annotation> itAnotacion = set.iterator(); itAnotacion.hasNext();) {
                Annotation nextAnotation = itAnotacion.next();

                String termino = nextAnotation.getFeatures().get("string").toString();
                FeatureMap features = Factory.newFeatureMap();
                
                //Modificar para que busque el tipo y no el término tal cuál.
                if (this.termsToSearch.contains(termino)) {
                    List<UiLabel> findTerm = api.findTerm(termino);
                    for (Iterator<UiLabel> itUiLabel = findTerm.iterator(); itUiLabel.hasNext();) {
                        UiLabel nextUiLabel = itUiLabel.next();
                        features.put(nextUiLabel.getUi(), nextUiLabel.getLabel());
                    }

                    out.add(nextAnotation.getStartNode().getOffset(), nextAnotation.getEndNode().getOffset(), "Mi etiqueta", features);
                }
            }
            this.fireProcessFinished();
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

    private String UTSUser;

    public String getUTSUser() {
        return UTSUser;
    }

    @Optional
    @RunTime
    @CreoleParameter(comment = "UTS User to acces API")
    public void setUTSUser(String UTSUser) {
        this.UTSUser = UTSUser;
    }

    private String UTSPassword;

    public String getUTSPassword() {
        return UTSPassword;
    }

    @Optional
    @RunTime
    @CreoleParameter(comment = "UTS password")
    public void setUTSPassword(String UTSPassword) {
        this.UTSPassword = UTSPassword;
    }

    private String UMLSRelease;

    public String getUMLSRelease() {
        return UMLSRelease;
    }

    @Optional
    @RunTime
    @CreoleParameter(comment = "Dictionary release to use in UTS")
    public void setUMLSRelease(String UMLSRelease) {
        this.UMLSRelease = UMLSRelease;
    }

    private ArrayList<String> termsToSearch;

    public ArrayList<String> getTermsToSearch() {
        return termsToSearch;
    }

    @RunTime
    @CreoleParameter(comment = "list of terms to search in uts")
    public void setTermsToSearch(ArrayList<String> termsToSearch) {
        this.termsToSearch = termsToSearch;
    }

    private String inputAnnotationSetName;

    public String getInputAnnotationSetName() {
        return inputAnnotationSetName;
    }

    @RunTime
    @CreoleParameter(comment = "name of the annotationSet used for input", defaultValue = "Token")
    public void setInputAnnotationSetName(String inputAnnotationSetName) {
        this.inputAnnotationSetName = inputAnnotationSetName;
    }

    private String outputAnnotationSetName;

    public String getOutputAnnotationSetName() {
        return outputAnnotationSetName;
    }

    @RunTime
    @CreoleParameter(comment = "name of the annotationSet used for output", defaultValue = "UTS")
    public void setOutputAnnotationSetName(String setName) {
        this.outputAnnotationSetName = setName;
    }
    
    private ParamOrPropertiesOptions paramOrProperties;
    
    public ParamOrPropertiesOptions getParamOrProperties() {
        return paramOrProperties;
    }
    
    @RunTime
    @CreoleParameter(comment = "Set if plugin takes connection options from Properties file or params", defaultValue = "Properties")
    public void setParamOrProperties(ParamOrPropertiesOptions paramOrProperties) {
        this.paramOrProperties = paramOrProperties;
    }

}
