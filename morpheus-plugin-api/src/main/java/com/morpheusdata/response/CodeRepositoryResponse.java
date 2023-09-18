package com.morpheusdata.response;

import java.io.File;
import java.net.URI;


/**
 * The contents of a code repository
 */
public class CodeRepositoryResponse {

	public File root;
	public URI relativeRoot;
	public String digestFolderName;
	public String gitUrl;
	public String localGitRef;
	public String remoteGitRef;
	public String repoName;



}
