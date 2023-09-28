package com.morpheus.controller

import com.morpheus.service.ProjectGeneratorService
import groovy.util.logging.Slf4j
import io.micronaut.context.annotation.Parameter
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.server.types.files.StreamedFile

import jakarta.inject.Inject
import java.util.zip.ZipOutputStream

@Controller("/")
@Slf4j
class StartController {
	@Inject ProjectGeneratorService projectGeneratorService

	@Get("/start")
	public HttpResponse index(HttpRequest<?> request) {
		HttpResponse.redirect(URI.create('/start/index.html'))
	}

	@Get(uri = "/generate", consumes = MediaType.APPLICATION_FORM_URLENCODED)
	public StreamedFile generate(HttpRequest<?> request, @QueryValue String pluginName, @QueryValue String pluginCode, @QueryValue String language, @QueryValue String morpheusVersion, @QueryValue String basePackage, @QueryValue("providers") Optional<List<String>> providersOption) {
		log.info("Generating")

		List<String> providers = providersOption.get()
		PipedOutputStream outStream = new PipedOutputStream()
		new Thread().start() {
			log.info("Starting Thread with ${providers}")
			ZipOutputStream  zipOut = new ZipOutputStream(outStream)
			projectGeneratorService.generateProject(zipOut,language,morpheusVersion,pluginName,pluginCode,basePackage,providers)
			zipOut.close()
			outStream.close()
		}
		InputStream is = new PipedInputStream(outStream)
		return new StreamedFile(is, MediaType.APPLICATION_OCTET_STREAM_TYPE).attach("${pluginName}.zip")
	}
}
