package com.vozmediano.f1trivia.domain.model.usecase

import android.util.Log
import com.vozmediano.f1trivia.domain.F1ResultRepository
import com.vozmediano.f1trivia.domain.model.f1.Driver
import com.vozmediano.f1trivia.domain.model.quiz.Option
import com.vozmediano.f1trivia.domain.model.quiz.Question
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WhoWonAtCircuitAndSeasonUseCase(
    private val f1ResultRepository: F1ResultRepository
) {
    suspend operator fun invoke(): Question? = withContext(Dispatchers.IO) {
        val correctSeason = (1950..2025).random().toString()

        val results = try {
            Log.i("WhoWonAtCircuitAndSeasonUseCase", "Fetching circuits")
            f1ResultRepository.getResultsBySeason(correctSeason)
        } catch (e: Exception) {
            Log.i("WhoWonAtCircuitAndSeasonUseCase", "Error fetching circuits: ${e.message}")
            return@withContext null
        }

        val correctRound = results.random().round
        val correctResults = results.filter { it.round == correctRound }
        val correctDriver = correctResults.first { it.position == "1" }.driver
        val correctRaceName = correctResults.first().raceName

        val question = Question(
            title = "Who won the ${correctRaceName} in $correctSeason?",
            options = mutableListOf()
        )
        val driverSet = mutableSetOf<String>()

        question.options.add(
            Option(
                id = 0,
                shortText = "${correctDriver.givenName} ${correctDriver.familyName}",
                longText = "${correctDriver.givenName} ${correctDriver.familyName} won the ${correctRaceName} in $correctSeason",
                isCorrect = true
            )
        )

        driverSet.add(correctDriver.driverId)

        while (driverSet.size < 4) {
            lateinit var distractDriver: Driver
            when((1..5).random()){
                //same circuit, different season
                1 -> {
                    Log.i("WhoWonAtCircuitAndSeasonUseCase", "Same circuit, different season")
                    val diffSeason = (-2..2).random().toString()
                    try{
                        val differentSeasonResults = f1ResultRepository.getResultsBySeason(diffSeason)
                        distractDriver = differentSeasonResults.first().driver
                    } catch (e: Exception){
                        Log.i("WhoWonAtCircuitAndSeasonUseCase", "error on: same circuit different season($diffSeason)")
                    }
                }
                //Different round, same season
                2 -> {
                    Log.i("WhoWonAtCircuitAndSeasonUseCase", "Same circuit, different season")
                    val diffResult = results.filter { it.round != correctRound }.filter{it.position == "1"}.random()
                    distractDriver = diffResult.driver
                }
                //Same round, same season, 2nd position
                3 -> {
                    Log.i("WhoWonAtCircuitAndSeasonUseCase", "Same round, same season, 2nd position")
                    distractDriver = correctResults.first { it.position == "2" }.driver
                }
                //Same round, same season, 3rd position
                4 -> {
                    Log.i("WhoWonAtCircuitAndSeasonUseCase", "Same round, same season, 3rd position")
                    distractDriver = correctResults.first { it.position == "3" }.driver
                }
                //Different round, same year, podium position
                5 -> {
                    Log.i("WhoWonAtCircuitAndSeasonUseCase", "Different race, same year, podium position")
                    val diffResult = results.filter { it.round != correctRound }.filter{it.position == (2..3).random().toString()}.random()
                    distractDriver = diffResult.driver
                }
            }
            try{
                if(driverSet.contains(distractDriver.driverId)) continue
                driverSet.add(distractDriver.driverId)

                question.options.add(
                    Option(
                        id = driverSet.size,
                        shortText = "${distractDriver.givenName} ${distractDriver.familyName}",
                        longText = "",
                        isCorrect = false
                    )
                )
            } catch (e: Exception){
                continue
            }
        }

        question.options.shuffle()

        return@withContext question


    }


}
