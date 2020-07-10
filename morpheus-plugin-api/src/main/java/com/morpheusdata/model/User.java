package com.morpheusdata.model;

import java.util.Map;

public class User extends MorpheusModel {
    public String username;
    public Map<String, String> permissions;
}
