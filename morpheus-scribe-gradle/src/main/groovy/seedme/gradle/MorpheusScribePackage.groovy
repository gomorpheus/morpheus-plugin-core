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
class MorpheusScribePackage extends DefaultTask {

	private String destinationDirectoryPath

	@Delegate MorpheusScribeExtension scribeExtension = new MorpheusScribeExtension()

	@Input
	File getPackageDir() {
		def path = scribeExtension.packageSource
		return path ? new File(path) : null
	}

	void setPackageDir(File packageDir) {
		scribeExtension.packageSource = packageDir.absolutePath
	}

	@OutputDirectory
	File getDestinationDir() {
		def path = scribeExtension.packageTarget
		return path ? new File(path) : null
	}

	void setDestinationDir(File dir) {
		scribeExtension.packageTarget = dir.absolutePath
	}

	@InputFiles
	FileTree getSource() {
		FileTree src = getProject().files(this.getPackageDir()).getAsFileTree()
		return src
	}

	File getManifestFile() {
		def outputDir = getDestinationDir()
		def outputFile = scribeExtension.packageManifest
		return outputFile ? new File(outputDir, outputFile) : null
	}

	void setManifestFile(File manifestFile) {
		scribeExtension.packageManifest = manifestFile.name	
	}

	@TaskAction
	@CompileDynamic
	void compile() {
		//list of file names
		def packageNames = []
		//input directory
		def packageSource = getPackageDir()
		//if we have a package dir
		if(packageSource.exists()) {
			//output directory
			def packageTarget = getDestinationDir()
			//manifest
			def packageManifest = getManifestFile()
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
					packageNames << relativePath
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
				manifestOutput <<  packageNames.join('\n')
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
