package examples

import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.streams.toList

data class Person(val name: String, val id: Int = Random().nextInt()){
    var friends: List<Person> = mutableListOf()
}

fun generateSocialNetworkOfSize(numberOfPeople: Int, maxNumberOfFriends: Int = numberOfPeople): List<Person>{
    val randomNamesFile = Paths.get(ClassLoader.getSystemResource("random_names.lsv").toURI())
    val names = Files.lines(randomNamesFile).limit(numberOfPeople.toLong()).toList().toTypedArray()
    val people = Arrays.stream(names).map{name -> Person(name) }.toList()

    val random = Random()
    for (i in 0 until numberOfPeople){
        people[i].friends = people.minus(people[i]).shuffled().take(random.nextInt(maxNumberOfFriends))
    }
    return people.toList()
}