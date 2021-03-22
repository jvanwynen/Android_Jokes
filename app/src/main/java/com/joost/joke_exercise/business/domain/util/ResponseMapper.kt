package com.joost.joke_exercise.business.domain.util

interface ResponseMapper <Response, DomainModel>{

    fun mapFromResponse(response: Response): DomainModel

}