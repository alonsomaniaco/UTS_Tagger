# UTS_Tagger
UTS_TAGGER is a plugin for [UMLS Terminology Services](https://uts.nlm.nih.gov)
to tag documents using his 
[API](https://documentation.uts.nlm.nih.gov/soap/home.html).

### Installation steps

#### Requirements

* Java JDK and JRE (1.7+)
* [Ant](http://ant.apache.org "Apache Ant")
* [Maven](https://maven.apache.org "Apache Maven")

#### Installing proccess

* Download [Gate](https://gate.ac.uk/) and follow [the installation steps]
(https://gate.ac.uk/download/) of it in your SO.
* Go to **"\<gate_root_folder\>/plugins"** and clone this repo.
* Once the repo is cloned we'll need to modify the maven configurations in 
order to be able to compile this plugin.
    * We have to go to **"C:\Users\<user>\.m2"** on Windows or **"~/.m2"**
on Linux or Macintosh. If the folder does not exists we'll have to create it.
    * Edit (or create) **settings.xml** and we have to add credentials 
like this. If we've created the file, it should look like so:
        ```xml
        <settings>
          <servers>
            <server>
              <id>FTP-SERVER</id>
              <username>anonymous</username>
              <password></password>
            </server> 
          </servers>
        </settings>
        ```
    * To compile the plugin we have to open a terminal, locate on the plugin 
root directory and run the compiling command:
        ```bash
        mvn package -Dmaven.test.skip=true
        ```
* Open Gate software and go to File → Manage CREOLE Plugins.
* Search for **UTS_tagger** and enable it.

#### Description of the plugin

To use the plugin we'll need to add an **UTS Tagger** on processing resources
subtree. We'll need to configure user and password to use api(We can get one 
[here](https://uts.nlm.nih.gov//home.html)).

 * UTSUser: User to make each request to UTS api.
 * UTSPassword: PAssword to make each request to UTS api.

Once the processing resource has been added and has been included in any 
gate application we'll need to configure the next parameters:

 * UMLSRelease: Metathesaurus versión to use for search each term. A versión 
list can be found 
[here](https://www.nlm.nih.gov/research/umls/knowledge_sources/metathesaurus/release/bugs_previous.html).
 * CategoryName: With this param the plugin will know from where it can extract the category of each term.
 * InputAnnotationSetName: Name of the set to extract term names to search for.
 * MaxResults: Max api results to extract on each call.
 * OutputAnnotationSetName: Output set to add each result of the api.
 * TermsToSearch: Category names to use on api calls.
 * WordString: Key name to use to extract the original word to send it to the api.
