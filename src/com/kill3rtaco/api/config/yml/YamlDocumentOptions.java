package com.kill3rtaco.api.config.yml;

/**
 * Represents various options for a YamlDocument
 * 
 * @author KILL3RTACO
 *
 * @since TacoAPI/Config 1.0
 */
public class YamlDocumentOptions {
	
	/**
	 * Set to true if the document should save any default value when a
	 * {@code get*(String path, Object def)} method is called. Default: true
	 */
	public boolean	saveDefaults	= true;
	
	/**
	 * Set to true if the document should be saved whenever a {@code set(...)}
	 * method is called. Default: true
	 */
	public boolean	saveOnSet		= true;
	
	/**
	 * Set to an array of synonyms for the boolean true. For example, say the
	 * synonyms {"yes", "allow"} were set. When the document tries to convert a
	 * string to a boolean, it will check if the string matches "yes" or "allow"
	 * (ignoring case). Either match, then true is returned. Default: empty
	 * array
	 */
	public String[]	trueSynonyms	= new String[]{};
	
}
