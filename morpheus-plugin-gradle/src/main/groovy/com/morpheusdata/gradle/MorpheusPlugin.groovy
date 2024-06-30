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

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.UnknownTaskException
import org.gradle.language.jvm.tasks.ProcessResources

/**
 * task: sribePackage - builds a manifest of your scribe packages
 * task: sribeResources - builds a manifest of your scribe resources
 * task: morpheusI18n - packages i18n properties files for localization efforts
 * @author bdwheeler
 */
class MorpheusPlugin implements Plugin<Project> {

	void apply(Project project) {
		//default config
		def defaultConfiguration = project.extensions.create('morpheusPlugin', MorpheusPluginExtension)
		//setup package task
		project.tasks.create('scribePackage', MorpheusScribePackage)
		//setup scribe task
		project.tasks.create('scribeResources', MorpheusScribeResources)
		//setup i18n task
		project.tasks.create('i18nPackage',MorpheusI18nPackage)
		//get the package task
		def scribePackageTask = project.tasks.getByName('scribePackage')
		def scribeResourcesTask = project.tasks.getByName('scribeResources')
		def morpheusI18nPackageTask = project.tasks.getByName('i18nPackage')
		//needed?
		project.afterEvaluate {
			//get loaded config
			def morpheusPluginConfig = project.extensions.getByType(MorpheusPluginExtension)
			ProcessResources processResources
			try {
				processResources = (ProcessResources)project.tasks.processResources
			} catch(UnknownTaskException ex) {
				//we dont care this is just to see if it exists
			}
			//configure the package tasks
			scribePackageTask.configure {
				packageDir = project.file(morpheusPluginConfig.packageSource)
				destinationDir = project.file("${processResources?.destinationDir}/${morpheusPluginConfig.packageTarget}")
			}
			//configure the resource tasks
			scribeResourcesTask.configure {
				scribeDir = project.file(morpheusPluginConfig.scribeSource)
				destinationDir = project.file("${processResources?.destinationDir}/${morpheusPluginConfig.scribeTarget}")
			}

			//configure the resource tasks
			morpheusI18nPackageTask.configure {
				i18nDir = project.file(morpheusPluginConfig.i18nDir)
				i18nTarget = project.file("${processResources?.destinationDir}/${morpheusPluginConfig.i18nTarget}")
			}


			//add task dependencies
			
			if(project.file(morpheusPluginConfig.packageSource).exists()) {
				processResources.dependsOn(scribePackageTask)
			}

			if(project.file(morpheusPluginConfig.scribeSource).exists()) {
				processResources.dependsOn(scribeResourcesTask)
			}

			if(project.file(morpheusPluginConfig.i18nDir).exists()) {
				processResources.dependsOn(morpheusI18nPackageTask)
			}
			
		}
	}

}
