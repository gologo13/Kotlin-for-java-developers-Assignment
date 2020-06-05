package mastermind

import mastermind.Evaluation

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation {
    assert(secret.length == guess.length)

    val rightPositions = mutableMapOf<Int, Boolean>()

    secret.forEachIndexed { index, _ ->
        if (secret[index] == guess[index]) {
            rightPositions[index] = true
        }
    }

    val secretMap = mutableMapOf<Char, Int>()
    secret.forEachIndexed { index, ch ->
        if (!rightPositions.getOrDefault(index, false)) {
            secretMap[ch] = secretMap.getOrDefault(ch, 0) + 1
        }
    }

    var wrongPosition = 0
    guess.forEachIndexed { index, c ->
        if (!rightPositions.getOrDefault(index, false)) {
            secretMap[c]?.let {
                if (it > 0) {
                    secretMap[c] = it - 1
                    ++wrongPosition
                }
            }
        }
    }

    return Evaluation(
            rightPosition = rightPositions.values.count { e -> e },
            wrongPosition = wrongPosition
    )
}
