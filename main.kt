package connectfour

import java.lang.IndexOutOfBoundsException
import java.lang.NumberFormatException

var field = mutableListOf<MutableList<String>>()

fun printField(columns: Int) {
    for (i in 1..columns) {
        print(" $i")
    }
    print(" \n")
    for (f in field) {
        for (s in f)
            print("|${s}")
        print("|\n")
    }
    repeat(columns * 2 + 1) {print("=")}
}

fun game(name: String, x: String, columns: Int): String {
    var turn: String
    var check = false
    var full = true
    do {
        println("\n$name's turn:")
        turn = readln()
        try {
            if (turn != "end") {
                loop@ for (f in field.lastIndex downTo 0) {
                    if (field[f][turn.toInt()-1] == " ") {
                        full = false
                        field[f][turn.toInt()-1] = x
                        break@loop
                    }
                }
                if (full) print("Column $turn is full")
                else check = true
            } else check = true
        } catch (e: IndexOutOfBoundsException) {
            print("The column number is out of range (1 - ${columns})")
        } catch (e: NumberFormatException) {
            print("Incorrect column number")
        }
    } while (!check)
    return turn
}

fun check(name: String, x: String): String {
    var count: Int
    var result = ""
    var draw = true
    loop@ for (f in field.lastIndex downTo 0) {
        if (f >= 3) {
            for (s in field[f].indices)
                if (field[f][s] == field[f-1][s] && field[f-1][s] == field[f-2][s] && field[f-2][s] == field[f-3][s] && field[f][s] == x) {
                    result = "Player $name won"
                    break@loop
                }
        }

        count = 1
        for (s in field[f].indices) {
            if (s != field[f].lastIndex)
                if (field[f][s] == field[f][s+1] && field[f][s] == x) {
                    count ++
                    if (count == 4) {
                        result = "Player $name won"
                        break@loop
                    }
                } else count = 1
        }

        for (s in field[f].indices) {
            if (f >= 3 && s <= field[f].lastIndex - 3)
                if (field[f][s] == field[f-1][s+1] && field[f-1][s+1] == field[f-2][s+2] && field[f-2][s+2] == field[f-3][s+3] && field[f][s] == x) {
                    result = "Player $name won"
                    break@loop
                }
        }

        for (s in field[f].indices) {
            if (f >= 3 && s >= 3)
                if (field[f][s] == field[f-1][s-1] && field[f-1][s-1] == field[f-2][s-2] && field[f-2][s-2] == field[f-3][s-3] && field[f][s] == x) {
                    result = "Player $name won"
                    break@loop
                }
        }
    }
    if (result == "") {
        for (f in field.indices) {
            if (field[f].contains(" ")) draw = false
        }
        if (draw) result = "It is a draw"
    }

    return result
}

fun main() {
    println("Connect Four\nFirst player's name:")
    val nameFirst = readln()
    println("Second player's name:")
    val nameSecond = readln()
    var dimensionArray: List<String>
    var numberOfGames = ""
    var nogCheck = false
    var message = ""
    do {
        println("Set the board dimensions (Rows x Columns)\nPress Enter for default (6 x 7)")
        var switch = true
        val dimension = readln().lowercase().replace("\t", "").replace(" ", "")
        dimensionArray = dimension.split("x")
        if (dimension.isEmpty()) {
            dimensionArray = listOf("6", "7")
            message = "$nameFirst VS $nameSecond\n${dimensionArray[0]} X ${dimensionArray[1]} board"
        } else if (dimensionArray.size == 2){
            try {
                if (dimensionArray[0].toInt() !in 5..9) {
                    println("Board rows should be from 5 to 9")
                    switch = false
                } else if (dimensionArray[1].toInt() !in 5..9) {
                    println("Board columns should be from 5 to 9")
                    switch = false
                } else if (dimensionArray[0].toInt() in 5..9 && dimensionArray[1].toInt() in 5..9) {
                    message = "$nameFirst VS $nameSecond\n${dimensionArray[0]} X ${dimensionArray[1]} board"
                }
            } catch (e: NumberFormatException) {
                println("Invalid input")
                switch = false
            }
        } else {
            println("Invalid input")
            switch = false
        }
    } while (!switch)
    do {
        println("Do you want to play single or multiple games?\nFor a single game, input 1 or press Enter\nInput a number of games: ")
        try {
            numberOfGames = readln()
            if (numberOfGames == "0") {
                println("Invalid input")
            } else {
                if (numberOfGames != "") numberOfGames.toInt()
                nogCheck = true
            }
        } catch (e: NumberFormatException) {
            println("Invalid input")
        }
    } while (!nogCheck)
    println(message)
    if (numberOfGames == "1" || numberOfGames == "") println("Single game")
    else println("Total $numberOfGames games")
    val score = mutableListOf<Int>(0, 0)
    var turn: String
    var check: String
    if (numberOfGames == "1" || numberOfGames == "") {
        val rows = dimensionArray[0].toInt()
        val columns = dimensionArray[1].toInt()
        field = MutableList(rows) {MutableList(columns) {" "}}
        printField(columns)
        do {
            turn = game(nameFirst, "o", columns)
            if (turn != "end") {
                printField(columns)
                check = check(nameFirst, "o")
                if (check != "") {
                    println(check)
                    break
                }
            } else break
            turn = game(nameSecond, "*", columns)
            if (turn != "end") {
                printField(columns)
                check = check(nameSecond, "*")
                if (check != "") {
                    println(check)
                    break
                }
            }
        } while (turn != "end")
    } else {
        for (g in 1..numberOfGames.toInt()) {
            val rows = dimensionArray[0].toInt()
            val columns = dimensionArray[1].toInt()
            field = MutableList(rows) {MutableList(columns) {" "}}
            println("Game #$g")
            printField(columns)
            if (g*3 % 2 != 0) {
                do {
                    turn = game(nameFirst, "o", columns)
                    if (turn != "end") {
                        printField(columns)
                        check = check(nameFirst, "o")
                        if (check != "") {
                            print("\n")
                            println(check)
                            if (check == "Player $nameFirst won") score[0]  = score[0] + 2
                            if (check == "It is a draw") {
                                score[0] = score[0] + 1
                                score[1] = score[1] + 1
                            }
                            break
                        }
                    } else break
                    turn = game(nameSecond, "*", columns)
                    if (turn != "end") {
                        printField(columns)
                        check = check(nameSecond, "*")
                        if (check != "") {
                            print("\n")
                            println(check)
                            if (check == "Player $nameSecond won") score[1]  = score[1] + 2
                            if (check == "It is a draw") {
                                score[0] = score[0] + 1
                                score[1] = score[1] + 1
                            }
                            break
                        }
                    }
                } while (turn != "end")
            } else if (g*3 % 2 == 0) {
                do {
                    turn = game(nameSecond, "*", columns)
                    if (turn != "end") {
                        printField(columns)
                        check = check(nameSecond, "*")
                        if (check != "") {
                            print("\n")
                            println(check)
                            if (check == "Player $nameSecond won") score[1]  = score[1] + 2
                            if (check == "It is a draw") {
                                score[0] = score[0] + 1
                                score[1] = score[1] + 1
                            }
                            break
                        }
                    }
                    turn = game(nameFirst, "o", columns)
                    if (turn != "end") {
                        printField(columns)
                        check = check(nameFirst, "o")
                        if (check != "") {
                            if (check == "Player $nameFirst won") score[0] = score[0] + 2
                            if (check == "It is a draw") {
                                score[0] = score[0] + 1
                                score[1] = score[1] + 1
                            }
                            print("\n")
                            println(check)
                            break
                        }
                    } else break
                } while (turn != "end")
            }
            println("Score\n$nameFirst: ${score[0]} $nameSecond: ${score[1]}")
        }
    }
    println("Game over!")
}
