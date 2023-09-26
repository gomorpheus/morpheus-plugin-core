package com.morpheusdata.gradle

import groovy.io.FileType
import groovy.transform.CompileDynamic
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction

class MorpheusI18nPackage extends DefaultTask {
	private String destinationDirectoryPath

	@Internal
	MorpheusPluginExtension pluginExtension = new MorpheusPluginExtension()

	@InputDirectory
    @PathSensitive(PathSensitivity.RELATIVE)
	File getI18nDir() {
		def path = pluginExtension.i18nDir
		return path ? new File(path) : null
	}

	void setI18nDir(File i18nDir) {
		pluginExtension.i18nDir = i18nDir.absolutePath
	}

	@OutputDirectory
	File getI18nTarget() {
		def path = pluginExtension.i18nTarget
		return path ? new File(path) : null
	}

	void setI18nTarget(File dir) {
		pluginExtension.i18nTarget = dir.absolutePath
	}

	@InputFiles
	FileTree getSource() {
		FileTree src = getProject().files(this.getI18nDir()).getAsFileTree()
		return src
	}

	@OutputFile
	File getI18nManifest() {
		def outputDir = getI18nTarget()
		def outputFile = pluginExtension.i18nManifest
		return outputFile ? new File(outputDir, outputFile) : null
	}

	void setI18nManifest(File manifestFile) {
		pluginExtension.packageManifest = manifestFile.name
	}

	@TaskAction
	@CompileDynamic
	void compile() {
		//list of file names
		def messageFileNames = []
		//input directory
		def packageSource = getI18nDir()
		//if we have a package dir
		if(packageSource.exists()) {
			//output directory
			def packageTarget = getI18nTarget()
			//manifest
			def packageManifest = getI18nManifest()
			//ensure it exists
			if(!packageTarget.exists())
				packageTarget.mkdirs()
			//iterate the files
			packageSource.eachFileRecurse(FileType.FILES) { file ->
				//get the relative path
				def relativePath = relativePathToResolver(file.canonicalPath, packageSource.canonicalPath)
				if(relativePath.indexOf('.') == 0) {
					println("ignoring hidden file: ${relativePath} at ${file} and not adding to package manifest")
				} else {
					messageFileNames << relativePath
				}
			}
			//make the manifest
			if(!packageManifest.exists()) {
				packageManifest.parentFile.mkdirs()
				packageManifest.createNewFile()
			}
			//write the manifest
			OutputStream manifestOutput
			try {
				manifestOutput = packageManifest.newOutputStream()
				manifestOutput <<  messageFileNames.join('\n')
			} finally {
				manifestOutput.flush()
				manifestOutput.close()
			}
		}
	}

	protected String relativePathToResolver(String filePath, String scanDirectoryPath) {
		if(filePath.startsWith(scanDirectoryPath)) {
			return filePath.substring(scanDirectoryPath.size() + 1).replace(File.separator, '/')
		}
		return filePath
	}
}
