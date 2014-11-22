package com.kill3rtaco.api.bukkit.dev;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import org.bukkit.Bukkit;

//org.json pacakage
import com.kill3rtaco.api.util.json.JSONArray;
import com.kill3rtaco.api.util.json.JSONException;
import com.kill3rtaco.api.util.json.JSONObject;

/**
 * Represents a CurseForge/BukkitDev project.
 * 
 * @author KILL3RTACO
 * @since TBukkit 1.0
 *
 */
public class Project implements Cloneable {
	
	private int				_id;
	private Project.Stage	_stage;
	private String			_name, _slug;
	private Project.File[]	_files;
	private JSONObject		_meta;
	
	private static String	HOST		= "https://api.curseforge.com/servermods/";
	private static String	EP_FILES	= HOST + "files?projectIds=";
	private static String	EP_PROJECT	= HOST + "projects?search=";
	
	//made private to ensure that only this class calls the constructer
	private Project(JSONObject dto) {
		try {
			_id = dto.getInt("id");
			_stage = Project.Stage.valueOf(dto.getString("stage").toUpperCase());
			_name = dto.getString("name");
			_slug = dto.getString("slug");
			JSONArray files = dto.getJSONArray("files");
			_files = new Project.File[files.length()];
			for (int i = 0; i < _files.length; i++) {
				_files[i] = new Project.File(this, files.getJSONObject(i));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get the ID of the project
	 * 
	 * @return the ID of the project
	 * @since TBukkit 1.0
	 */
	public int getId() {
		return _id;
	}
	
	/**
	 * Get the development stage of the project
	 * 
	 * @return the development stage
	 * @see {@link Project.Stage}
	 * @since TBukkit 1.0
	 */
	public Project.Stage getDevelopmentStage() {
		return _stage;
	}
	
	/**
	 * Get the name of the project
	 * 
	 * @return the name of the project
	 * @since TBukkit 1.0
	 */
	public String getName() {
		return _name;
	}
	
	/**
	 * Get the project's slug. The slug is a string used to identify the project
	 * when making a link. For instance, Mineopoly can be found at
	 * http://dev.bukkit.org/bukkit-plugins/monopoly, Where 'monopoly' is the
	 * slug.
	 * 
	 * @return The projects slug
	 * @since TBukkit 1.0
	 */
	public String getSlug() {
		return _slug;
	}
	
	/**
	 * Get a copy of all files associated with this project in ascending order
	 * from when they were uploaded. (these are not the same as the File class
	 * from java.io)
	 * 
	 * @return The project's files
	 * @since TBukkit 1.0
	 */
	public Project.File[] getFiles() {
		Project.File[] files = new Project.File[_files.length];
		for (int i = 0; i < _files.length; i++) {
			files[i] = _files[i].clone();
		}
		return files;
	}
	
	/**
	 * Opposite of {@link #getFiles()}, gets a copy of all files in
	 * <i>descending</i> from when they were uploaded.
	 * 
	 * @return The project's files
	 * @since TBukkit 1.0
	 */
	public Project.File[] getFilesReversed() {
		Project.File[] files = getFiles(); //clone
		Project.File[] reverse = new Project.File[files.length]; //clone
		for (int i = files.length - 1; i >= 0; i++) {
			reverse[files.length - 1 - i] = files[i];
		}
		return reverse;
	}
	
	/**
	 * Get the latest file uploaded. Equivalent to:
	 * 
	 * <pre>
	 * Project.File[] files = project.getFiles();
	 * return files[files.length - 1];
	 * </pre>
	 * 
	 * @return the latest file, or null if this project has no files
	 * @since TBukkit 1.0
	 */
	public Project.File getLatestFile() {
		if (_files.length == 0)
			return null;
		return _files[_files.length - 1];
	}
	
	/**
	 * Get the latest
	 * 
	 * @param extension
	 *            The extension to look for, leading period (.) optional. If the
	 *            given String is empty or null, {@link #getLatestFile()} is
	 *            returned.
	 * @return The latest file with the given extension, or null if no file was
	 *         found.
	 * @since TBukkit 1.0
	 */
	public Project.File getLatestFile(String extension) {
		String ext = (extension.startsWith(".") ? extension : "." + extension);
		for (Project.File f : getFilesReversed())
			if (f.getFileUrl().endsWith(ext))
				return f;
		return null;
	}
	
	/**
	 * Tests whether an update is available for this project. This method
	 * assumes that the "latest file" has the extension '.jar', and checks its
	 * version, assuming its name follows {NAME} v{VERSION}
	 * 
	 * @param currentVersion
	 *            The current version of the project
	 * @return true if an update is available. Returns false if there was not a
	 *         file found that follows the above format
	 * @see TBukkit#extractVersion(String)
	 * @since TBukkit 1.0
	 */
	public boolean updateAvailable(String currentVersion) {
		Project.File latest = getLatestFile("jar");
		if (latest == null)
			return false;
		String name = latest.getName();
		String regex = "(.* v)" + TBukkit.VERSION_REGEX;
		if (!name.matches(regex))
			return false;
		
		String version = TBukkit.extractVersion(name);
		return TBukkit.latestVersion(currentVersion, version).equals(version);
	}
	
	public Project clone() {
		return new Project(_meta);
	}
	
	public boolean equals(Project project) {
		return project.getId() == _id;
	}
	
	/**
	 * Compares this project and another project by there development stages.
	 * Development stages are sorted by their ordinal position in the enum.
	 * 
	 * @param project
	 * @return the same value as {@link Integer#compareTo(Integer)}
	 * @since TBukkit 1.0
	 */
	public int compareByStage(Project project) {
		return new Integer(_stage.ordinal()).compareTo(project.getDevelopmentStage().ordinal());
		
	}
	
	/**
	 * Represents a project file
	 * 
	 * @author KILL3RTACO
	 * @since TBukkit 1.0
	 * @see Project
	 *
	 */
	public class File {
		
		private Project		_project;
		private Stage		_releaseType;
		private String		_name, _filename, _fileUrl, _downloadUrl,
							_gameVersion,
							_md5;
		private JSONObject	_meta;
		
		/**
		 * Construct a file object
		 * 
		 * @param project
		 *            The project this file belongs to
		 * @param dto
		 *            The JSON object to contruct from
		 * @since TBukkit 1.0
		 */
		private File(Project project, JSONObject dto) {
			try {
				_meta = dto;
				_project = project;
				_releaseType = Stage.valueOf(dto.getString("releaseType").toUpperCase());
				_name = dto.getString("name");
				_filename = dto.getString("fileName");
				_fileUrl = dto.getString("fileUrl").replace("\\/", "/");
				_downloadUrl = dto.getString("downloadUrl").replace("\\/", "/");
				_gameVersion = dto.getString("gameVersion");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * Get the project this file belongs to
		 * 
		 * @return The project this file belongs to
		 * @since TBukkit 1.0
		 */
		public Project getProject() {
			return _project;
		}
		
		/**
		 * Get the release type
		 * 
		 * @return The file's release type
		 * @since TBukkit 1.0
		 */
		public Project.Stage getReleaseType() {
			return _releaseType;
		}
		
		/**
		 * Get the name of the file. Note that this is the name of the online
		 * file and not the same as the name of the physical file.
		 * 
		 * @return the file name
		 * @since TBukkit 1.0
		 * @see #getFileName()
		 */
		public String getName() {
			return _name;
		}
		
		/**
		 * Get the name of the physical file..
		 * 
		 * @return the physical file's name
		 * @since TBukkit 1.0
		 * @see #getName()
		 */
		public String getFileName() {
			return _filename;
		}
		
		/**
		 * Get the file url. Note that this is the url used to view the
		 * information of a file, and not the download link.
		 * 
		 * @return the file's url
		 * @since TBukkit 1.0
		 * @see #getDownloadUrl()
		 */
		public String getFileUrl() {
			return _fileUrl;
		}
		
		/**
		 * Get the url used to download the physical file.
		 * 
		 * @return the download url
		 * @since TBukkit 1.0
		 */
		public String getDownloadUrl() {
			return _downloadUrl;
		}
		
		/**
		 * Get the game version associated with this file.
		 * 
		 * @return the game version
		 * @since TBukkit 1.0
		 */
		public String getGameVersion() {
			return _gameVersion;
		}
		
		/**
		 * Get the md5 checksum of the physical file.
		 * 
		 * @return the md5 checksum
		 * @since TBukkit 1.0
		 */
		public String getMD5() {
			return _md5;
		}
		
		/**
		 * Download this file to Bukkit.getServer().getUpdateFolderFile()
		 * 
		 * @since TBukkit 1.0
		 */
		public void download() {
			downloadTo(Bukkit.getServer().getUpdateFolderFile());
		}
		
		/**
		 * Download this file to a specified directory
		 * 
		 * @param dir
		 *            The directory to download to
		 * @since TBukkit 1.0
		 */
		public void downloadTo(String dir) {
			downloadTo(new java.io.File(dir));
		}
		
		/**
		 * Download this file to a specified directory
		 * 
		 * @param dir
		 *            The directory to download to
		 * @since TBukkit 1.0
		 * @throws IllegalArgumentException
		 *             if the directory does not exist or is not a directory
		 */
		public void downloadTo(java.io.File dir) {
			if (!dir.exists())
				throw new IllegalArgumentException("dir does not exist");
			else if (!dir.isDirectory())
				throw new IllegalArgumentException("dir is not a directory");
			
			BufferedInputStream in = null;
			FileOutputStream out = null;
			try {
				URL url = new URL(_downloadUrl);
				in = new BufferedInputStream(url.openStream());
				out = new FileOutputStream(new java.io.File(dir, _filename));
				
				int kb = 1024; //1024 bytes = 1 kilobyte
				byte[] data = new byte[kb];
				
				int count;
				while ((count = in.read(data, 0, kb)) != -1) {
					out.write(data, 0, count);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (in != null)
						in.close();
					
					if (out != null)
						out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		
		public Project.File clone() {
			return new Project.File(_project, _meta);
		}
		
	}
	
	/**
	 * Represents a stage in development or a release type for a file
	 * 
	 * @author KILL3RTACO
	 * @since TBukkit 1.0
	 * @see Project
	 *
	 */
	public enum Stage {
		
		/**
		 * The project is being planned and is only a concept at the moment.
		 * 
		 * @since TBukkit 1.0
		 */
		PLANNING,
		
		/**
		 * Alpha stage in project development, or an alpha build of a file.
		 * 
		 * @since TBukkit 1.0
		 */
		ALPHA,
		
		/**
		 * Beta stage in project development, or a beta build of a file.
		 * 
		 * @since TBukkit 1.0
		 */
		BETA,
		
		/**
		 * The project is no longer in beta stage, or a stable build of a file.
		 * 
		 * @since TBukkit 1.0
		 */
		RELEASE,
		
		/**
		 * The project does not need much updating and will not be updated very
		 * frequently, or the project has been worked on for a while and the
		 * author is very confident in its code.
		 * 
		 * @since TBukkit 1.0
		 */
		MATURE,
		
		/**
		 * The project has not been updated in a while.
		 * 
		 * @since TBukkit 1.0
		 */
		INACTIVE,
		
		/**
		 * The project has been completely abandoned and there will be no
		 * updates by the original author.
		 * 
		 * @since TBukkit 1.0
		 */
		ABANDONED;
	}
	
	/**
	 * Get a Project by its slug.
	 * 
	 * @param slug
	 *            The <i>exact</i> slug of the project on BukkitDev
	 * @return a Project, or null if a project could not be found with the given
	 *         slug or TBukkit is disabled
	 * @since TBukkit 1.0
	 */
	public static Project getProject(String slug) {
		if (!TBukkit.isEnabled())
			return null;
		try {
			URLConnection projectConn = new URL(EP_PROJECT + slug).openConnection();
			TBukkit.prepConnection(projectConn, false);
			
			String output = TBukkit.getOutput(projectConn);
//			TBukkit.say("Project(s): " + output);
			if (output == null || output.isEmpty()) {
				TBukkit.say("Failed to fetch search information for projects with slug '" + slug + "'");
				return null;
			}
			JSONArray searchResults = new JSONArray(output);
			JSONObject meta = null;
			for (int i = 0; i < searchResults.length(); i++) {
				JSONObject json = searchResults.getJSONObject(i);
				if (json.getString("slug").equals(slug)) {
					meta = json;
					break;
				}
			}
			
			if (meta == null) {
				TBukkit.say("Failed to fetch information on project with slug '" + slug + "' - Search results empty");
				return null;
			}
			
			int id = meta.getInt("id");
			URLConnection filesConn = new URL(EP_FILES + id).openConnection();
			TBukkit.prepConnection(filesConn, false);
			output = TBukkit.getOutput(filesConn);
//			TBukkit.say("File(s): " + output);
			
			//output should never be empty - if a project has no files "[]" is returned
			if (output == null || output.isEmpty())
				TBukkit.say("Failed to fetch file information for project with slug '" + slug + "'");
			meta.put("files", new JSONArray(output));
			return new Project(meta);
			
		} catch (Exception e) {
			TBukkit.say("An exception was thrown when fetching information for '" + slug + "' - " + e.getMessage());
			return null;
		}
	}
}
