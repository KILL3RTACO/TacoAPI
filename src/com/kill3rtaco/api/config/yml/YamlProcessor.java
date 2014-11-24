package com.kill3rtaco.api.config.yml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.DumperOptions.LineBreak;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.representer.Representer;

class YamlProcessor {
	
	private static final Yaml	YAML;
	private static final String	NEWLINE;
	
	static {
		DumperOptions options = new DumperOptions();
		options.setIndent(2);
		options.setDefaultFlowStyle(FlowStyle.BLOCK);
		LineBreak linebreak = LineBreak.getPlatformLineBreak();
		options.setLineBreak(linebreak);
		NEWLINE = linebreak.getString();
		Representer representer = new Representer();
		representer.setDefaultFlowStyle(FlowStyle.BLOCK);
		YAML = new Yaml(new SafeConstructor(), representer, options);
	}
	
	public static Map<String, Object> getValues(File file) {
		try {
			return getValues(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			return new HashMap<String, Object>();
		}
	}
	
	public static Map<String, Object> getValues(String source) {
		return getValues(new ByteArrayInputStream(source.getBytes()));
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getValues(InputStream stream) {
		Map<String, Object> values = new HashMap<String, Object>();
		try {
			Object data = YAML.load(stream);
			stream.close();
			return (Map<String, Object>) data;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return values;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getValues(Reader reader) {
		return (Map<String, Object>) YAML.load(reader);
	}
	
	public static void save(YamlDocument document) {
		File file = document.getSaveFile();
		String header = document.getHeader();
		List<YamlNode> nodes = document.getNodes();
		try {
			if (!file.exists()) {
				file.getParentFile().mkdirs();
			} else {
				file.delete();
				file.createNewFile();
			}
			FileWriter writer = new FileWriter(file);
			if (header != null && !header.isEmpty()) {
				outComment(header, writer);
				writer.append(NEWLINE);
			}
			dumpNodeList(writer, nodes, 0);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private static void dumpNodeList(Writer writer, List<YamlNode> children, int indent) throws IOException {
		String tab = "  "; //2 spaces
		String indentPrefix = "";
		for (int i = 0; i < indent; i++) {
			indentPrefix += tab;
		}
		for (YamlNode n : children) {
			String name = n.getName();
			if (name.matches("[0-9]+"))
				name = "'" + name + "'";
			else if (name.isEmpty())
				name = "''";
			
			if (n.getComment() != null)
				writer.append(NEWLINE + indentPrefix + "# " + n.getComment() + NEWLINE);
			
			if (!n.isSection()) {
				Object toDump = n.asObject();
				if (toDump instanceof YamlCollection) {
					toDump = ((YamlCollection) toDump).toMapList();
				}
				String dump = YAML.dump(toDump);
				if (toDump instanceof List) {
					dump = NEWLINE + indentPrefix + dump.replace(NEWLINE, NEWLINE + indentPrefix);
					//remove trailing spaces but not newline character
					dump = dump.substring(0, dump.length() - indentPrefix.length());
				}
				writer.append(indentPrefix + n.getName() + ": " + dump);
			} else {
				writer.append(indentPrefix + n.getName() + ":" + NEWLINE);
				dumpNodeList(writer, n.getNodes(), ++indent);
				indent--;
			}
		}
	}
	
	private static void outComment(String comment, Writer writer) throws IOException {
		for (String s : comment.split("\n")) {
			writer.append("# " + s + "\n");
		}
	}
}
