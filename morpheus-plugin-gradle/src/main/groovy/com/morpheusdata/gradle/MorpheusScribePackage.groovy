/*
 *  Copyright 2024 Morpheus Data, LLC.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://raw.githubusercontent.com/gomorpheus/morpheus-plugin-core/v1.0.x/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.morpheusdata.gradle

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
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
import groovy.io.FileType

/**
 * bdwheeler
 */
@CompileStatic
class MorpheusScribePackage extends DefaultTask {

	private String destinationDirectoryPath

	@Internal
	MorpheusPluginExtension scribeExtension = new MorpheusPluginExtension()

	@InputDirectory
    @PathSensitive(PathSensitivity.RELATIVE)
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

	@OutputFile
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
