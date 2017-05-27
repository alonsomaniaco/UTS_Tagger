package uts_tagger;

import gov.nih.nlm.uts.webservice.Psf;
import gov.nih.nlm.uts.webservice.UiLabel;
import gov.nih.nlm.uts.webservice.UtsFault_Exception;
import gov.nih.nlm.uts.webservice.UtsWsFinderController;
import gov.nih.nlm.uts.webservice.UtsWsFinderControllerImplService;
import gov.nih.nlm.uts.webservice.UtsWsSecurityController;
import gov.nih.nlm.uts.webservice.UtsWsSecurityControllerImplService;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author amaranto
 */
public class UTS_API {
    
    private String username;
    private String password;
    private String umlsRelease;
    private static String serviceName="http://umlsks.nlm.nih.gov";
    private UtsWsSecurityController securityService = (new UtsWsSecurityControllerImplService())
                    .getUtsWsSecurityControllerImplPort();

    //create the reference variables
    private UtsWsFinderController utsFinderService = (new UtsWsFinderControllerImplService())
            .getUtsWsFinderControllerImplPort();
    
    //Obtiene el Proxy Grant Ticket - dura 8 horas es necesario para generar "single-use" tickets.
    private String ticketGrantingTicket;
    
    private int maxResults;

    public UTS_API(String username, String password, String umlsRelease, int maxResults) 
            throws UtsFault_Exception {
        this.username = username;
        this.password = password;
        this.umlsRelease = umlsRelease;
        this.ticketGrantingTicket= securityService.getProxyGrantTicket(this.username
                , this.password);
        this.maxResults=maxResults;
    }

    public UTS_API(String propertiesFile, int maxResults) throws IOException, UtsFault_Exception {
        UTS_Properties props=new UTS_Properties(propertiesFile);
        this.username = props.getUsername();
        this.password = props.getpassword();
        this.umlsRelease = props.getumlsRelease();
        this.ticketGrantingTicket= securityService.getProxyGrantTicket(this.username
                , this.password);
        this.maxResults=maxResults;
    }
    
    private String getSingleUseTicket() throws UtsFault_Exception{
        return securityService.getProxyTicket(this.ticketGrantingTicket, UTS_API.serviceName);
    }
    
    private Psf getPSF(){
        Psf psfMetathesaurusContent = new Psf();	    
        //Limita el numero de resultados mostrados por página a 50
        psfMetathesaurusContent.setPageLn(this.maxResults);
        return psfMetathesaurusContent;
    }
    
    public List<UiLabel> findTerm(String term) throws UtsFault_Exception{
        return this.utsFinderService.findConcepts(this.getSingleUseTicket(), this.umlsRelease
                , "atom", term, "words", this.getPSF());
    }
}
