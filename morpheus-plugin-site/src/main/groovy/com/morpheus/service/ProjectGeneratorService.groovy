package com.morpheus.service

import com.github.jknack.handlebars.Handlebars
import com.github.jknack.handlebars.Template
import jakarta.inject.Singleton
import org.yaml.snakeyaml.Yaml

import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

@Singleton
public class ProjectGeneratorService {

	def generateProject(ZipOutputStream zipOutputStream, String language, String version, String pluginName, String pluginCode, String basePackage, List<String> providers) {
		URL resource = classLoader.getResource("project-templates/${version}/${language}/project-structure.yaml".toString())
		def is = resource.newInputStream()
		try {
			def yaml = new Yaml()
			Map<String, Object> projectStructure = yaml.load(is)

			Map<String,Object> bindings = [
				basePackage: basePackage,
				basePackagePath: "src/main/${language}/${basePackage.replace(".","/")}".toString(),
				pluginName: pluginName,
				pluginCode: pluginCode,
				pluginNameCamel: camelCase(pluginName,false),
				pluginNameInstance:camelCase(pluginName,true),
				providers: providers
			]
			providers.each { provider ->
				bindings[provider] = true
			}

			projectStructure.base?.each { Map<String,Object> baseFileInfo ->
				writeTemplateToZip(baseFileInfo, language,version,zipOutputStream,bindings)
			}
			providers.each { provider ->
				projectStructure.providers[provider]?.each { Map<String,Object> fileInfo ->
					writeTemplateToZip(fileInfo, language,version,zipOutputStream,bindings)
				}
			}
		} finally {
			is.close()
		}



	}


	protected writeTemplateToZip(Map<String,Object> fileInfo, String language, String version, ZipOutputStream zipOutputStream,Map<String,Object> bindings) {
		String fileName = generateTemplateFromString(fileInfo.name as String, bindings)
		String content = generateTemplate(fileInfo.template as String, language, version, bindings)
		ZipEntry entry = new ZipEntry(fileName)
		zipOutputStream.putNextEntry(entry)
		zipOutputStream.write(content.getBytes("UTF-8"))
		zipOutputStream.flush()
	}

	protected generateTemplate(String resourcePath, String language, String version, Map<String,Object> bindings) {
		URL resource = classLoader.getResource("project-templates/${version}/${language}/${resourcePath}" .toString())
		Handlebars handlebars = new Handlebars();
		Template template = handlebars.compileInline(resource.text)
		return template.apply(bindings)
	}


	protected generateTemplateFromString(String templateString, Map<String,Object> bindings) {
		Handlebars handlebars = new Handlebars();
		Template template = handlebars.compileInline(templateString)
		return template.apply(bindings)
	}

	protected String camelCase(String source, Boolean lower=false) {
		String[] words = source.split("[\\W_]+");
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			if (i == 0 && lower) {
				word = word.isEmpty() ? word : word.toLowerCase();
			} else {
				word = word.isEmpty() ? word : "${Character.toUpperCase(word.charAt(0))}${word.substring(1).toLowerCase()}";
			}
			builder.append(word);
		}
		return builder.toString();
	}

	protected ClassLoader getClassLoader() {
		this.getClass().getClassLoader()
	}
}
