package com.vozmediano.f1trivia.domain.model.usecase

import android.util.Log
import com.vozmediano.f1trivia.domain.F1CircuitRepository
import com.vozmediano.f1trivia.domain.F1RaceRepository
import com.vozmediano.f1trivia.domain.WikiRepository
import com.vozmediano.f1trivia.domain.model.f1.Driver
import com.vozmediano.f1trivia.domain.model.quiz.Question
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

class MostWinsByCircuitUseCase(
    private val f1RaceRepository: F1RaceRepository,
    private val f1CircuitRepository: F1CircuitRepository,
    private val wikiRepository: WikiRepository
) {
    suspend operator fun invoke(): Question? = withContext(Dispatchers.IO) {
        val circuits = try {
            Log.i("MostWinsByCircuitUseCase", "Fetching circuits")
            f1CircuitRepository.getCircuits()
        } catch (e: Exception) {
            Log.i("MostWinsByCircuitUseCase", "Error fetching circuits: ${e.message}")
            return@withContext null
        }

        var question: Question?
        var attempts = 0
        do {
            attempts++
            val correctCircuit = circuits.random()
            Log.i("MostWinsByCircuitUseCase", "attempt $attempts - ${correctCircuit.circuitId}")
            val raceResults = try {
                f1RaceRepository.getRacesByCircuitAndPosition(
                    circuitId = correctCircuit.circuitId,
                    position = "1"
                )
            } catch (e: Exception) {
                Log.i("MostWinsByCircuitUseCase", "Error fetching results: ${e.message}")
                return@withContext null
            }
            Log.i("MostWinsByCircuitUseCase", "Race results: $raceResults")

            Log.i("MostWinsByCircuitUseCase", "circuit.url: ${correctCircuit.url}")
            var title = URLDecoder
                .decode(correctCircuit.url, StandardCharsets.UTF_8.name())
                .substringAfterLast("/")
            Log.i("MostWinsByCircuitUseCase", "title: $title")
            var imageUrl = wikiRepository.getImage(title)
            Log.i("MostWinsByCircuitUseCase", "wiki url: $imageUrl")

            question = Question(
                title = "Who has the most wins at ${correctCircuit.circuitName}?",
                options = mutableListOf(),
                image = imageUrl
            )

            val driverSet = mutableSetOf<String>()

            val winsMap = mutableMapOf<Driver, Int>()
            raceResults.forEach { race ->
                val driver = race.results!!.first().driver
                winsMap[driver] = winsMap.getOrDefault(driver, 0) + 1
                Log.i("MostWinsByCircuitUseCase", "${driver.givenName} ${driver.familyName} ${winsMap[driver]}")
            }

            val mostWinsDriver = winsMap.maxBy { it.value }.key

            if (winsMap.maxBy { it.value }.value <= 1 || winsMap.size < 4) continue

            driverSet.add(mostWinsDriver.driverId)

            question.options.add(
                com.vozmediano.f1trivia.domain.model.quiz.Option(
                    id = 0,
                    shortText = "${mostWinsDriver.givenName} ${mostWinsDriver.familyName}",
                    longText = "${mostWinsDriver.givenName} ${mostWinsDriver.familyName} has ${winsMap[mostWinsDriver] ?: 0} wins at ${correctCircuit.circuitName}",
                    isCorrect = true
                )
            )

            //remove drivers with equal wins to the most wins driver
            val keysToRemove = mutableListOf<Driver>()
            winsMap.forEach { entry ->
                if (entry.value == winsMap[mostWinsDriver]) {
                    keysToRemove.add(entry.key)
                }
            }
            keysToRemove.forEach { key ->
                winsMap.remove(key)
            }

            while (driverSet.size < 4) {
                val driver = winsMap.maxBy { it.value }.key
                driverSet.add(driver.driverId)
                question.options.add(
                    com.vozmediano.f1trivia.domain.model.quiz.Option(
                        id = question.options.size,
                        shortText = "${driver.givenName} ${driver.familyName}",
                        longText = "",
                        isCorrect = false
                    )
                )
                winsMap.remove(driver)
            }

            question.options.shuffle()
            break

        } while (attempts <= 5)
        if (attempts > 5){
            return@withContext null
        } else {
            return@withContext question
        }
    }

}