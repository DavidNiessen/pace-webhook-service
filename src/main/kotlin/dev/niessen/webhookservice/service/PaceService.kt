package dev.niessen.webhookservice.service

import dev.niessen.webhookservice.converter.PaceJsonToModelConverter
import dev.niessen.webhookservice.model.MenuModel
import dev.niessen.webhookservice.repository.PaceRepository
import org.springframework.stereotype.Service

@Service
class PaceService(
    private val paceRepository: PaceRepository,
    private val paceJsonToModelConverter: PaceJsonToModelConverter
) {

    fun getAndDispatchTodaysMenu(): List<MenuModel> {
        val paceJson = paceRepository.fetchPaceJson()
        return paceJsonToModelConverter.convert(paceJson)
    }

}
