package com.morpheus.scribe.gradle

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import groovy.io.FileType

/**
 * bdwheeler
 */
@CompileStatic
class MorpheusScribeResources extends DefaultTask {

	private String destinationDirectoryPath

	@Delegate MorpheusScribeExtension scribeExtension = new MorpheusScribeExtension()

	@Input
	File getScribeDir() {
		def path = scribeExtension.scribeSource
		return path ? new File(path) : null
	}

	void setScribeDir(File scribeDir) {
		scribeExtension.scribeSource = scribeDir.absolutePath
	}

	@OutputDirectory
	File getDestinationDir() {
		def path = scribeExtension.scribeTarget
		return path ? new File(path) : null
	}

	void setDestinationDir(File dir) {
		scribeExtension.scribeTarget = dir.absolutePath
	}

	@InputFiles
	FileTree getSource() {
		FileTree src = getProject().files(this.getScribeDir()).getAsFileTree()
		return src
	}

	File getManifestFile() {
		def outputDir = getDestinationDir()
		def outputFile = scribeExtension.scribeManifest
		return outputFile ? new File(outputDir, outputFile) : null
	}

	void setManifestFile(File manifestFile) {
		scribeExtension.scribeManifest = manifestFile.name	
	}

	@TaskAction
	@CompileDynamic
	void compile() {
		//list of file names
		def scribeNames = []
		//input directory
		def scribeSource = getScribeDir()
		//if we have a scribe dir
		if(scribeSource.exists()) {
			//output directory
			def scribeTarget = getDestinationDir()
			//manifest
			def scribeManifest = getManifestFile()
			//ensure it exists
			if(!scribeTarget.exists())
				scribeTarget.mkdirs()
			//iterate the files
			scribeSource.eachFileRecurse(FileType.FILES) { file ->
				//get the relative path
				def relativePath = relativePathToResolver(file.canonicalPath, scribeSource.canonicalPath)
				//println("scribe file: ${relativePath}")
				if(relativePath.indexOf('.') == 0) {
					println("ignoring hidden file: ${relativePath} at ${file} and not adding to scribe manifest")
				} else {
					scribeNames << relativePath
				}
			}
			//make the manifest
			if(!scribeManifest.exists()) {
				scribeManifest.parentFile.mkdirs()
				scribeManifest.createNewFile()
			}
			//write the manifest
			OutputStream manifestOutput
			try {
				manifestOutput = scribeManifest.newOutputStream()
				manifestOutput <<  scribeNames.join('\n')
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
