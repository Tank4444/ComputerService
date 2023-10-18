package ru.chuikov.ComputerService.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("","/")
class IndexController {

    @GetMapping
    fun index()= mapOf("Status" to "ok")
}