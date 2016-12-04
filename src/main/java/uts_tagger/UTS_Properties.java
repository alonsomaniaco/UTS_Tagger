package uts_tagger;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author amaranto
 */
public class UTS_Properties {
    private FileInputStream fich;
    private Properties props;
    private static String conFile="/usr/local/GATE_Developer_8.0/plugins"
            + "/UTS_Tagger/conf.properties";
    
    public UTS_Properties() throws FileNotFoundException, IOException {
        this.fich=new FileInputStream(UTS_Properties.conFile);
        this.props=new Properties();
        this.props.load(this.fich);
    }
    
    public String getUsername(){
        return this.getProperty("username");
    }
    
    public String getpassword(){
        return this.getProperty("password");
    }
    
    public String getumlsRelease(){
        return this.getProperty("umlsRelease");
    }
    
    private String getProperty(String prop){
        return this.props.getProperty(prop);
    }
    
    
}
