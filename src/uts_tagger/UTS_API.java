/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts_tagger;

import gov.nih.nlm.uts.webservice.finder.Psf;
import gov.nih.nlm.uts.webservice.finder.UiLabel;
import gov.nih.nlm.uts.webservice.finder.UtsWsFinderController;
import gov.nih.nlm.uts.webservice.finder.UtsWsFinderControllerImplService;
import gov.nih.nlm.uts.webservice.security.UtsFault_Exception;
import gov.nih.nlm.uts.webservice.security.UtsWsSecurityController;
import gov.nih.nlm.uts.webservice.security.UtsWsSecurityControllerImplService;
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

    public UTS_API(String username, String password, String umlsRelease) 
            throws UtsFault_Exception {
        this.username = username;
        this.password = password;
        this.umlsRelease = umlsRelease;
        this.ticketGrantingTicket= securityService.getProxyGrantTicket(this.username
                , this.password);
    }

    public UTS_API() throws IOException, UtsFault_Exception {
        PropertiesUTS props=new PropertiesUTS();
        this.username = props.getUsername();
        this.password = props.getpassword();
        this.umlsRelease = props.getumlsRelease();
        this.ticketGrantingTicket= securityService.getProxyGrantTicket(this.username
                , this.password);
    }
    
    private String getSingleUseTicket() throws UtsFault_Exception{
        return securityService.getProxyTicket(this.ticketGrantingTicket, UTS_API.serviceName);
    }
    
    private Psf getPSF(){
        Psf psfMetathesaurusContent = new Psf();	    
        //Limita el numero de resultados mostrados por página a 50
        psfMetathesaurusContent.setPageLn(150);
        return psfMetathesaurusContent;
    }
    
    public List<UiLabel> findTerm(String term) throws UtsFault_Exception, gov.nih.nlm.uts.webservice.finder.UtsFault_Exception{
        return this.utsFinderService.findConcepts(this.getSingleUseTicket(), this.umlsRelease
                , "atom", term, "words", this.getPSF());
    }
}
