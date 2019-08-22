package com.morpheusdata

import com.morpheusdata.response.ServiceResponse
import spock.lang.Specification

class ServiceResponseSpec extends Specification {
    void "happy path"() {
        expect:
        !ServiceResponse.success().hasErrors()
        ServiceResponse.error().hasErrors()
        ServiceResponse.error("message").hasErrors()
        ServiceResponse.error("message", [error: "something"]).hasErrors()
        ServiceResponse.error("message", [error: "something"]).errors.size() == 1
    }

    void "generic response"() {
        given:
        def resp = new ServiceResponse<SimpleDTO>()
        Object data = [name: "foo", age: 1]

        when: "Object assigned to Strongly typed response data"
        resp.data = data

        then:
        resp.data.name == "foo"
        resp.data.age == 1
    }

    void "error setting"() {
        given:
        def resp = new ServiceResponse<>()
        when:

        resp
        then:
        !resp.success
        resp.hasErrors()
        resp.errors.size() == 0

        when:
        resp.success = true
        then:
        !resp.hasErrors()
        resp.errors.size() == 0

        when:
        resp.addError("whoops")
        then:
        resp.hasErrors()
        resp.errors.size() == 1

        when:
        resp.removeError()
        then:
        !resp.hasErrors()
        resp.errors.size() == 0
        resp.success

        when:
        resp.addError("alloc", "failed ot allocate mem")
        then:
        !resp.success
        resp.hasErrors()
        resp.errors.size() == 1

        when:
        resp.clearErrors()
        then:
        !resp.hasErrors()
        resp.success

        when:
        resp.addError('default error key')
        resp.addError('alloc', 'alloc failure')
        then:
        resp.hasErrors()
        !resp.success
        resp.errors.size() == 2
    }
}

class SimpleDTO {
    String name
    Integer age
}
