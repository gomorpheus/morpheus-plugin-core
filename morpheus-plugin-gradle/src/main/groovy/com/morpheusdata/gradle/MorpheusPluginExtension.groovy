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
