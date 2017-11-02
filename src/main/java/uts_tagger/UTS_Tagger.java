package uts_tagger;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Factory;
import gate.FeatureMap;
import gate.Resource;
import gate.creole.*;
import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.CreoleResource;
import gate.creole.metadata.RunTime;
import gov.nih.nlm.uts.webservice.UiLabel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author amaranto
 */
@CreoleResource(name = "UTS Tagger", comment = "An UTS Tagger")
public class UTS_Tagger extends AbstractLanguageAnalyser {

    @Override
    public Resource init() throws ResourceInstantiationException {
        return this;
    }

    @Override
    public void execute() throws ExecutionException {
        System.out.println("Iniciado");
        try {
            AnnotationSet set = this.document.getAnnotations().get(this.inputAnnotationSetName);
            AnnotationSet out = this.document.getAnnotations();

            UTS_API api;
            if (this.UTSUser.isEmpty()) {
                throw new ResourceInstantiationException(
                        "The tagger requires your UTS User.");
            }
            if (this.UTSPassword.isEmpty()) {
                throw new ResourceInstantiationException(
                        "The tagger requires your UTS Password.");
            }
            if (this.UMLSRelease.isEmpty()) {
                throw new ResourceInstantiationException(
                        "The tagger requires an UML Release dictionary to use.");
            }
            if (this.termsToSearch.isEmpty()) {
                throw new ResourceInstantiationException(
                        "The tagger requires a list of tokens to search in UTS api.");
            }

            api = new UTS_API(this.UTSUser, this.UTSPassword, this.UMLSRelease, this.maxResults);

            for (Iterator<Annotation> itAnotacion = set.iterator(); itAnotacion.hasNext();) {
                Annotation nextAnotation = itAnotacion.next();
                FeatureMap nextAnotationMap = nextAnotation.getFeatures();

                Object terminoObj = nextAnotationMap.get(this.wordString);
                Object termCategoryObj = nextAnotationMap.get(this.categoryName);

                if (terminoObj != null && termCategoryObj != null) {
                    String termino = terminoObj.toString();
                    String termCategory = termCategoryObj.toString();

                    FeatureMap features = Factory.newFeatureMap();

                    if (this.termsToSearch.contains(termCategory)) {
                        List<UiLabel> findTerm = api.findTerm(termino);
                        for (Iterator<UiLabel> itUiLabel = findTerm.iterator(); itUiLabel.hasNext();) {
                            UiLabel nextUiLabel = itUiLabel.next();
                            features.put(nextUiLabel.getUi(), nextUiLabel.getLabel());
                        }

                        out.add(nextAnotation.getStartNode().getOffset(), nextAnotation.getEndNode().getOffset(), this.outputAnnotationSetName, features);
                    }
                }
            }
            this.fireProcessFinished();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    @Override
    public void reInit() throws ResourceInstantiationException {
        System.out.println("Reiniciado");
    }

    //Params of the Gate plugin
    private String UTSUser;
    private String UTSPassword;
    private String UMLSRelease;
    private ArrayList<String> termsToSearch;
    private String inputAnnotationSetName;
    private String outputAnnotationSetName;
    private String categoryName;
    private String wordString;
    private Integer maxResults;

    public String getUTSUser() {
        return UTSUser;
    }

    @CreoleParameter(comment = "UTS User to acces API")
    public void setUTSUser(String UTSUser) {
        this.UTSUser = UTSUser;
    }

    public String getUTSPassword() {
        return UTSPassword;
    }

    @CreoleParameter(comment = "UTS password")
    public void setUTSPassword(String UTSPassword) {
        this.UTSPassword = UTSPassword;
    }

    public String getUMLSRelease() {
        return UMLSRelease;
    }

    @RunTime
    @CreoleParameter(comment = "Dictionary release to use in UTS")
    public void setUMLSRelease(String UMLSRelease) {
        this.UMLSRelease = UMLSRelease;
    }

    public ArrayList<String> getTermsToSearch() {
        return termsToSearch;
    }

    @RunTime
    @CreoleParameter(comment = "list of terms to search in uts", defaultValue = "NC;")
    public void setTermsToSearch(ArrayList<String> termsToSearch) {
        this.termsToSearch = termsToSearch;
    }

    public String getInputAnnotationSetName() {
        return inputAnnotationSetName;
    }

    @RunTime
    @CreoleParameter(comment = "name of the annotationSet used for input", defaultValue = "Token")
    public void setInputAnnotationSetName(String inputAnnotationSetName) {
        this.inputAnnotationSetName = inputAnnotationSetName;
    }

    public String getOutputAnnotationSetName() {
        return outputAnnotationSetName;
    }

    @RunTime
    @CreoleParameter(comment = "name of the annotationSet used for output", defaultValue = "UTS")
    public void setOutputAnnotationSetName(String setName) {
        this.outputAnnotationSetName = setName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    @RunTime
    @CreoleParameter(comment = "Set this param to specify the tag name for word category", defaultValue = "category")
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getWordString() {
        return wordString;
    }

    @RunTime
    @CreoleParameter(comment = "Set this param to specify the tag name for word text", defaultValue = "string")
    public void setWordString(String wordString) {
        this.wordString = wordString;
    }

    public Integer getMaxResults() {
        return maxResults;
    }

    @RunTime
    @CreoleParameter(comment = "Max number of result per word", defaultValue = "5")
    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }

}
