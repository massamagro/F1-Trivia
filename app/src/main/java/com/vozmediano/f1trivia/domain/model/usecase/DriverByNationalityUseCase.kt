package com.vozmediano.f1trivia.domain.model.usecase

import android.util.Log
import com.vozmediano.f1trivia.domain.F1DriverRepository
import com.vozmediano.f1trivia.domain.WikiRepository
import com.vozmediano.f1trivia.domain.model.quiz.Option
import com.vozmediano.f1trivia.domain.model.quiz.Question
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

class DriverByNationalityUseCase(
    private val f1DriverRepository: F1DriverRepository,
    private val wikiRepository: WikiRepository
) {
    suspend operator fun invoke(): Question? = withContext(Dispatchers.IO) {
        val drivers = try {
            f1DriverRepository.getDrivers()
        } catch (e: Exception) {
            return@withContext null
        }
        val correctDriver = drivers.random()
        Log.i("DriverByNationalityUseCase", "random driver: ${correctDriver.driverId}")

        Log.i("DriverByNationalityUseCase", "driver.url: ${correctDriver.url}")

        var title = URLDecoder
            .decode(correctDriver.url, StandardCharsets.UTF_8.name())
            .substringAfterLast("/")

        Log.i("DriverByNationalityUseCase", "title: $title")

        var imageUrl = wikiRepository.getImage(title)

        val question = Question(
            title = "Which of this drivers is ${correctDriver.nationality}?",
            options = mutableListOf(),
            imageAfter = imageUrl
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