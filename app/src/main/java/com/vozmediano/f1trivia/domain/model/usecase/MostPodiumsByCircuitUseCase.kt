package com.vozmediano.f1trivia.domain.model.usecase

import android.util.Log
import com.vozmediano.f1trivia.domain.F1CircuitRepository
import com.vozmediano.f1trivia.domain.F1RaceRepository
import com.vozmediano.f1trivia.domain.WikiRepository
import com.vozmediano.f1trivia.domain.model.f1.Driver
import com.vozmediano.f1trivia.domain.model.f1.Race
import com.vozmediano.f1trivia.domain.model.quiz.Question
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

class MostPodiumsByCircuitUseCase(
    private val f1RaceRepository: F1RaceRepository,
    private val f1CircuitRepository: F1CircuitRepository,
    private val wikiRepository: WikiRepository
) {
    suspend operator fun invoke(): Question? = withContext(Dispatchers.IO){
        val circuits = try {
            Log.i("MostPodiumsByCircuitUseCase", "Fetching circuits")
            f1CircuitRepository.getCircuits()
        } catch (e: Exception) {
            Log.i("MostPodiumsByCircuitUseCase", "Error fetching circuits: ${e.message}")
            return@withContext null
        }

        var question: Question?
        var attempts = 0
        do{
            attempts++
            val correctCircuit = circuits.random()

            Log.i("MostPodiumsByCircuitUseCase", "attempt $attempts - ${correctCircuit.circuitId}")
            val raceResults: MutableList<Race> = emptyList<Race>().toMutableList()
            val raceResults1 = try{
                f1RaceRepository.getRacesByCircuitAndPosition(
                    circuitId = correctCircuit.circuitId,
                    position = "1"
                ).toMutableList()
            } catch (e: Exception) {
                Log.i("MostPodiumsByCircuitUseCase", "Error fetching results: ${e.message}")
                return@withContext null
            }
            val raceResults2 = try{
                f1RaceRepository.getRacesByCircuitAndPosition(
                    circuitId = correctCircuit.circuitId,
                    position = "2"
                ).toMutableList()
            } catch (e: Exception) {
                Log.i("MostPodiumsByCircuitUseCase", "Error fetching results: ${e.message}")
                return@withContext null
            }
            val raceResults3 = try{
                f1RaceRepository.getRacesByCircuitAndPosition(
                    circuitId = correctCircuit.circuitId,
                    position = "3"
                ).toMutableList()
            } catch (e: Exception) {
                Log.i("MostPodiumsByCircuitUseCase", "Error fetching results: ${e.message}")
                return@withContext null
            }

            raceResults.addAll(raceResults1)
            raceResults.addAll(raceResults2)
            raceResults.addAll(raceResults3)

            Log.i("MostPodiumsByCircuitUseCase", "circuit.url: ${correctCircuit.url}")

            var title = URLDecoder
                .decode(correctCircuit.url, StandardCharsets.UTF_8.name())
                .substringAfterLast("/")

            Log.i("MostPodiumsByCircuitUseCase", "title: $title")

            var imageUrl = wikiRepository.getImage(title)

            Log.i("MostPodiumsByCircuitUseCase", "wiki url: $imageUrl")

            question = Question(
                title = "Who has the most podiums at ${correctCircuit.circuitName}?",
                options = mutableListOf(),
                image = imageUrl
            )

            val driverSet = mutableSetOf<String>()

            val podiumsMap = mutableMapOf<Driver, Int>()

            raceResults.forEach { race ->
                val driver = race.results!!.first().driver
                podiumsMap[driver] = podiumsMap.getOrDefault(driver, 0) + 1
                Log.i("MostWinsByCircuitUseCase", "${driver.givenName} ${driver.familyName} ${podiumsMap[driver]}")
            }

            val mostPodiumsDriver = podiumsMap.maxBy { it.value }.key

            if (podiumsMap.maxBy { it.value }.value <= 1 || podiumsMap.size < 4) continue

            driverSet.add(mostPodiumsDriver.driverId)

            question.options.add(
                com.vozmediano.f1trivia.domain.model.quiz.Option(
                    id = 0,
                    shortText = "${mostPodiumsDriver.givenName} ${mostPodiumsDriver.familyName}",
                    longText = "${mostPodiumsDriver.givenName} ${mostPodiumsDriver.familyName} has ${podiumsMap[mostPodiumsDriver] ?: 0} podiums at ${correctCircuit.circuitName}",
                    isCorrect = true
                )
            )

            //remove drivers with equal podiums to the most podiums driver
            val keysToRemove = mutableListOf<Driver>()
            podiumsMap.forEach { entry ->
                if (entry.value == podiumsMap[mostPodiumsDriver]) {
                    keysToRemove.add(entry.key)
                }
            }
            keysToRemove.forEach { key ->
                podiumsMap.remove(key)
            }
            while (driverSet.size < 4) {
                val driver = podiumsMap.maxBy { it.value }.key
                driverSet.add(driver.driverId)
                question.options.add(
                    com.vozmediano.f1trivia.domain.model.quiz.Option(
                        id = question.options.size,
                        shortText = "${driver.givenName} ${driver.familyName}",
                        longText = "",
                        isCorrect = false
                    )
                )
                podiumsMap.remove(driver)
            }

            Log.i("MostWinsByCircuitUseCase","Question generated")
            Log.i("MostWinsByCircuitUseCase",question.toString())

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