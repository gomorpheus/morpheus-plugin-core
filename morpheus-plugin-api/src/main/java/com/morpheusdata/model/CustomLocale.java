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
  
}
