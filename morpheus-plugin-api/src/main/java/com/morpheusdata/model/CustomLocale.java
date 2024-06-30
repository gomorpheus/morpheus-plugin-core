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

package com.morpheusdata.model;

/**
* Custom Locale Class for providing custom locale settings to the Morpheus UI.
*/
public class CustomLocale {
  
  /**
  * Display name of the Locale
  */
  private String name;
  
  /**
  * Locale code. Typically made of language code and country code 
  * and should match locale code used on corresponding .properties file 
  */
  private String code;
  
  /**
  * Description of the Custom Locale
  */
  private String description;
  
  public CustomLocale(String name, String code, String description) {
    this.name = name;
    this.code = code;
    this.description = description;
  }
  
  public CustomLocale(String name, String code) {
    this(name, code, null);
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getCode() {
    return this.code;
  }
  
  public String getDescription() {
    return this.description;
  }
  
  /**
  * Imitates java.util.Locale getDisplayName()
  * @return the display name
  */
  public String getDisplayName() {
    String displayName = this.name;
    if (this.description != null) {
      displayName = String.format("%s (%s)", displayName, this.description);
    }
    return displayName;
  }
  
  /**
  * Imitates java.util.Locale toString() by providing locale code
  */
  @Override
  public String toString() {
    return this.code;
  }

	public void setName(String name) {
		this.name = name;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
