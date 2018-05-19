package ru.apolyakov.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ThisWillActuallyRunController {

    @RequestMapping("/rest/api")
    String home() {
        return "Hello World!"
    }

}