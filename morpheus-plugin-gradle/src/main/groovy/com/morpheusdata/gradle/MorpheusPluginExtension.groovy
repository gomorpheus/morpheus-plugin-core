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

package com.morpheusdata.gradle

/**
 * Allows configuration of the Gradle plugin
 * @author bdwheeler
 */
class MorpheusPluginExtension {
	
	String scribeSource = 'src/main/resources/scribe'
	String scribeTarget = 'scribe'
	String scribeManifest = 'scribe.manifest'

	String packageSource = 'src/main/resources/packages'
	String packageTarget = 'packages'
	String packageManifest = 'packages.manifest'
	
	String i18nDir = 'src/main/resources/i18n'
	String i18nTarget = 'i18n'
	String i18nManifest = 'i18n.manifest'


	Map toMap() {
		return [
			scribeSource: scribeSource,
			scribeTarget: scribeTarget,
			scribeManifest: scribeManifest,
			packageSource: packageSource,
			packageTarget: packageTarget,
			packageManifest: packageManifest,
			i18nDir: i18nDir,
			i18nTarget: i18nTarget,
			i18nManifest: i18nManifest
		]
	}

}
