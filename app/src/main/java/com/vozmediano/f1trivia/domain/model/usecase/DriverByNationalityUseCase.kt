package com.vozmediano.f1trivia.domain.model.usecase

import com.vozmediano.f1trivia.domain.F1DriverRepository
import com.vozmediano.f1trivia.domain.model.quiz.Option
import com.vozmediano.f1trivia.domain.model.quiz.Question
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DriverByNationalityUseCase(
    private val f1DriverRepository: F1DriverRepository
) {
    suspend operator fun invoke(): Question? = withContext(Dispatchers.IO) {
        val drivers = try {
            f1DriverRepository.getDrivers()
        } catch (e: Exception) {
            return@withContext null
        }
        val correctDriver = drivers.random()
        val question = Question(
            title = "Which of this drivers is ${correctDriver.nationality}?",
            options = mutableListOf()
        )
        val setNationalities = mutableSetOf<String>()
        setNationalities.add(correctDriver.nationality)
        question.options.add(
            Option(
                id = 0,
                shortText = "${correctDriver.givenName} ${correctDriver.familyName}",
                longText = "${correctDriver.givenName} ${correctDriver.familyName} is ${correctDriver.nationality}",
                isCorrect = true
            )
        )
        while (question.options.size < 4) {
            val driver = drivers.filter { it.nationality != correctDriver.nationality }.random()
            if (setNationalities.contains(driver.nationality)) continue
            setNationalities.add(driver.nationality)
            question.options.add(
                Option(
                    id = question.options.size,
                    shortText = "${driver.givenName} ${driver.familyName}",
                    longText = "",
                    isCorrect = false
                )
            )
        }
        question.options.shuffle()
        return@withContext question
    }
}