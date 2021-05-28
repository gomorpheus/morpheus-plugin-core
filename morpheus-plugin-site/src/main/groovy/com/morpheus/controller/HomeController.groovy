package com.morpheus

import io.micronaut.http.*
import io.micronaut.http.annotation.*

@Controller("/")
class HomeController {


    @Get("/")
    public HttpResponse index(HttpRequest<?> request) {
        HttpResponse.redirect(URI.create('/index.html'))
    }

}
 
