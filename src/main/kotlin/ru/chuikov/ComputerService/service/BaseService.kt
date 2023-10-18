package ru.chuikov.ComputerService.service

import ru.chuikov.ComputerService.dao.Account

interface BaseService<T> {

    fun add(data: T): T?
    fun delete(id: Int)
    fun getById(id: Int): T?
    fun edit(data: T): T?
    fun getAll(): List<T>?
}