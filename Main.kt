package org.example



fun constructRoom(rows: Int, seats: Int, occRow: Int = -1, occSeat: Int = -1 ): MutableList<MutableList<String>> {
    val room: MutableList<MutableList<String>> = MutableList(rows + 1) { MutableList(seats + 1) { "" } }

    for( i in 0..rows) {
        for( j in 0..seats) {
            room[i][j] = when {
                occRow == i && occSeat == j -> "B"
                i == 0 && j == 0 -> " "
                i == 0 && j > 0 -> "$j"
                i > 0 && j == 0 -> "$i"
                else -> "S"
            }
        }
    }
    printRoom(room)
    return room
}

fun printRoom(room: MutableList<MutableList<String>>) {
    println("\nCinema:")
    for(i in 0..<room.size) {
        println(room[i].joinToString(" "))
    }
}

fun occupySeat(room: MutableList<MutableList<String>>, row: Int, col: Int): MutableList<MutableList<String>> {
    room[row][col] = "B"
    return room
}

data class RoomValues(
    val room: MutableList<MutableList<String>>,
    val rows: Int,
    val seatsPerRow: Int,
    val totalIncomeOfRoom: Int
)

//fun valuesForRoom(): Triple<MutableList<MutableList<String>>, Int, Int> {
fun valuesForRoom(): RoomValues {
    // Read the number of rows and seats per row
    println("Enter the number of rows:")
    val rows = readln().toInt()

    println("Enter the number of seats in each row:")
    val seatsPerRow = readln().toInt()

    var totalRoomIncome = 0
    val totalSeats = rows * seatsPerRow
    if(totalSeats <= 60) {
        totalRoomIncome = totalSeats * seatsPerRow * 10
        println("<= 60 ::: $totalSeats :::: $totalRoomIncome")
    } else {
        val cost10Rows = rows/2
        totalRoomIncome = cost10Rows * seatsPerRow * 10
        val rest = rows - cost10Rows
        totalRoomIncome += rest  * seatsPerRow * 8
        println("> 60 ::: FrontRows: $cost10Rows ::: FrontRowsIncome: ${cost10Rows * 10} ::: BackRows: $rest :: val: ${rest * 8} :: total: $totalRoomIncome")
    }

    return RoomValues(constructRoom(rows, seatsPerRow), rows, seatsPerRow, totalRoomIncome)
//    return (constructRoom(rows, seatsPerRow), rows, seatsPerRow)
}

fun askValuesForSeat(rows: Int, cols: Int, room: MutableList<MutableList<String>>, row: Int = 0, col: Int = 0 ): Pair<MutableList<MutableList<String>>, Int> {

    var r = row
    var c = col
    if(row == 0) {
        println("\nEnter a row number:")
        r = readln().toInt()

        println("Enter a seat number in that row:")
        c = readln().toInt()
    }

    val localRoom = occupySeat(room, r, c)


    // Calculate the total number of seats
    val totalSeats = rows * cols

    // Determine the ticket price and calculate the total income
    val totalIncome = if (totalSeats <= 60 || r <= rows / 2) 10 else 8

    // Print the result
    println("\nTicket price: $$totalIncome")
    return Pair(localRoom, totalIncome)
}

fun calculateStatistics(totalSeats: Int = 0, occupied: Int = 0, actualIncome: Int = 0, totalRoomIncome: Int = 0) {
    var pr = 0.00
    println("\nNumber of purchased tickets: $occupied")
    if(totalSeats != 0) {
        pr =  occupied.toDouble() / totalSeats.toDouble() * 100.0
    }
    val formatPercentage = "%.2f".format(pr)
    println("Percentage: $formatPercentage%")
    println("Current income: $$actualIncome")
    println("Total income: $$totalRoomIncome")
    println()
}

fun askRowCol(): Pair<Int, Int> {
    println("\nEnter a row number:")
    val row = readln().toInt()

    println("Enter a seat number in that row:")
    val col = readln().toInt()
    return Pair(row, col)
}

fun displayMenu(){
    var (room, r, seats, totalRoomIncome) = valuesForRoom()
    var actualIncome = 0
    var totalOccupied = 0

    var option = -1
    while(option != 0) {
        println()
        println("1. Show the seats")
        println("2. Buy a ticket")
        println("3. Statistics")
        println("0. Exit")
        print("> ")
        val value = readln().toInt()

        when(value) {
            1 -> printRoom(room)
            2 -> {
                var (row, col) = askRowCol()
                // If out of bounds
                if ( row > r || col > seats) {
                    while(row > r || col > seats) {
                        println("\nWrong input!")
                        val (r, c) = askRowCol()
                        row = r
                        col = c
                    }
                    val (r, totalIncome) = askValuesForSeat(r, seats, room, row, col)
                    room = r
                    totalOccupied++
                    actualIncome += totalIncome
                }

                // If already occupied
                if (room[row][col] == "B") {
                    while(room[row][col] == "B") {
                        println("\nThat ticket has already been purchased!")
                        val (r, c) = askRowCol()
                        row = r
                        col = c
                    }
                    val (r, totalIncome) = askValuesForSeat(r, seats, room, row, col)
                    room = r
                    totalOccupied++
                    actualIncome += totalIncome
                } else {
                    val (r, totalIncome) = askValuesForSeat(r, seats, room, row, col)
                    room = r
                    totalOccupied++
                    actualIncome += totalIncome
                }


            }
            3 -> calculateStatistics(r*seats, totalOccupied, actualIncome, totalRoomIncome)
            else -> option = 0
        }
    }
}

fun main() {
   displayMenu()
}
