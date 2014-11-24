package com.kill3rtaco.api.config.yml;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

/**
 * @author KILL3RTACO
 *
 * @since
 */
public class YamlDocument extends YamlNodeContainer {
	
	private File	_file;
	private String	_header	= null;
	
	/**
	 * Construct an empty YamlDocument
	 * 
	 * @since TacoAPI/Config 1.0
	 */
	public YamlDocument() {
		
	}
	
	/**
	 * Construct a YamlDocument from a file, loading its contents. The constructed YamlDocument can then be saved via
	 * {@code save()}
	 * 
	 * @param file
	 *            the file to load from
	 * @since TacoAPI/Config 1.0
	 */
	public YamlDocument(File file) {
		this(YamlProcessor.getValues(file));
		setSaveFile(file);
	}
	
	/**
	 * Construct a YamlDocument from an input stream, loading its contents.
	 * 
	 * @param stream
	 *            the stream to load from
	 * @since TacoAPI/Config 1.0
	 */
	public YamlDocument(InputStream stream) {
		this(YamlProcessor.getValues(stream));
	}
	
	/**
	 * Construct a YamlDocument from a raw YAML string
	 * 
	 * @param source
	 *            the YAML source
	 * @since TacoAPI/Config 1.0
	 */
	public YamlDocument(String source) {
		this(YamlProcessor.getValues(source));
	}
	
	/**
	 * Construct a YamlDocument from a raw map. Nodes and Collections are recursively added to the document, based on
	 * its contents
	 * 
	 * @param map
	 *            the raw data
	 * @since TacoAPI/Config 1.0
	 */
	public YamlDocument(Map<String, Object> map) {
		addNodes(map);
	}
	
	/**
	 * Set the options for this document
	 * 
	 * @param options
	 *            the new options
	 * @since TacoAPI/Config 1.0
	 * @see YamlDocumentOptions
	 */
	@Override
	public void setOptions(YamlDocumentOptions options) {
		super.setOptions(options);
	}
	
	/**
	 * Get the options for this document
	 * 
	 * @since TacoAPI/Config 1.0
	 * @see YamlDocumentOptions
	 * 
	 */
	@Override
	public YamlDocumentOptions options() {
		return super.options();
	}
	
	/**
	 * Set the header of the document. The header is displayed at the top of the file when it is saved.
	 * 
	 * @param header
	 *            the header, or null to remove it
	 * @since TacoAPI/Config 1.0
	 */
	public void setHeader(String header) {
		_header = header;
	}
	
	/**
	 * Get the header of the document.
	 * 
	 * @return the header
	 * @since TacoAPI/Config 1.0
	 */
	public String getHeader() {
		return _header;
	}
	
	/**
	 * Set the save file for this document.
	 * 
	 * @param file
	 *            where this document should be saved
	 * @since TacoAPI/Config
	 */
	public void setSaveFile(File file) {
		_file = file;
	}
	
	/**
	 * Set the save of for this document.
	 * 
	 * @param fileLocation
	 *            the path to the file
	 * @since TacoAPI/Config 1.0
	 */
	public void setSaveFile(String fileLocation) {
		setSaveFile(new File(fileLocation));
	}
	
	/**
	 * Get the save file of this document.
	 * 
	 * @return the save file
	 * @since TacoAPI/Config 1.0
	 */
	public File getSaveFile() {
		return _file;
	}
	
	/**
	 * Save this document. This method will only return true if the save file is not null.
	 * 
	 * @return true if the document was successfully saved.
	 * @since TacoAPI/Config 1.0
	 */
	public boolean save() {
		return save(_file);
	}
	
	/**
	 * Set the save file of this document, then save.
	 * 
	 * @param file
	 *            where the document should be saved
	 * @return true if the file was sucessfully saved
	 * @since TacoAPI/Config 1.0
	 */
	public boolean save(File file) {
		//seemingly redundant, but ensures that lower-level SnakeYAML operations
		//are always handled in one class
		setSaveFile(file);
		if (file != null) {
			YamlProcessor.save(this);
			return true;
		}
		return false;
	}
	
	public String getPath() {
		return "";
	}
	
}
