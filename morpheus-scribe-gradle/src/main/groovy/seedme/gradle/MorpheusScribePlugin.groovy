/*
* Copyright 2014 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.morpheus.scribe.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.UnknownTaskException
import org.gradle.api.UnknownDomainObjectException
import org.gradle.api.artifacts.Configuration
import org.gradle.api.distribution.DistributionContainer
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.Delete
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.bundling.Jar
import groovy.io.FileType
import org.gradle.language.jvm.tasks.ProcessResources

/**
 * task: sribePackage - builds a manifest of your scribe packages
 * task: sribeResources - builds a manifest of your scribe resources
 * @author bdwheeler
 */
class MorpheusScribePlugin implements Plugin<Project> {

	void apply(Project project) {
		//default config
		def defaultConfiguration = project.extensions.create('scribe', MorpheusScribeExtension)
		//setup package task
		project.tasks.create('scribePackage', MorpheusScribePackage)
		//setup scribe task
		project.tasks.create('scribeResources', MorpheusScribeResources)
		//get the package task
		def scribePackageTask = project.tasks.getByName('scribePackage')
		def scribeResourcesTask = project.tasks.getByName('scribeResources')
		//needed?
		project.afterEvaluate {
			//get loaded config
			def scribeConfig = project.extensions.getByType(MorpheusScribeExtension)
			ProcessResources processResources
			try {
				processResources = (ProcessResources)project.tasks.processResources
			} catch(UnknownTaskException ex) {
				//we dont care this is just to see if it exists
			}
			//configure the package tasks
			scribePackageTask.configure {
				packageDir = project.file(scribeConfig.packageSource)
				destinationDir = project.file("${processResources?.destinationDir}/${scribeConfig.packageTarget}")
			}
			//configure the resource tasks
			scribeResourcesTask.configure {
				scribeDir = project.file(scribeConfig.scribeSource)
				destinationDir = project.file("${processResources?.destinationDir}/${scribeConfig.scribeTarget}")
			}
			//add task dependencies
			processResources.dependsOn(scribePackageTask)
			processResources.dependsOn(scribeResourcesTask)
		}
	}

}
