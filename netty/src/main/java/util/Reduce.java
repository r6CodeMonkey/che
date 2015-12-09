package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by timmytime on 15/09/15.
 */
public class Reduce {

    public class Person{

        private String firstName, surname, gender;
        private int age;

        public Person(String firstName, String surname, String gender, int age){
            this.firstName = firstName;
            this.surname = surname;
            this.gender = gender;
            this.age = age;
        }

        public String getGender(){
            return gender;
        }

        public int getAge(){return age;}
    }

    private List<Person> people = new ArrayList<>();

    public Reduce(){

        people.add(new Person("Tim", "Wright", "Male", 35));
        people.add(new Person("John", "Wilson", "Male", 34));
        people.add(new Person("Ed", "Lewis", "Male", 27));
        people.add(new Person("Matt", "Wall", "Male", 27));
        people.add(new Person("Rachel", "Coupland", "Female", 38));

        double averageAge = people.stream()
                .filter(p -> p.gender.equals("Male"))
                .mapToInt(p -> p.age)
                .average().getAsDouble();

        Map<String, Double> avgGender =
                people.stream()
                .collect(Collectors.groupingBy(
                        Person::getGender,
                        Collectors.averagingInt(Person::getAge)));


        System.out.println("Average age is "+averageAge);


        avgGender.forEach((s,a) ->
        System.out.println("Gender "+s+" avg age "+a)
        );



    }


    public static void main(String[] args){

        new Reduce();

    }

}


