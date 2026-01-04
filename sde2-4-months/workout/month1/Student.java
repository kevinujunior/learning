import java.util.*;


class NameComparator implements Comparator<Student> {
    @Override
    public int compare(Student s1, Student s2) {
        return s1.getName().compareTo(s2.getName());
    }
}

public class Student implements Comparable<Student> {

    private Integer age;

    private String name;


    public Integer getAge(){
        return this.age;
    }

    public String getName(){
        return this.name;
    }

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }



    @Override
    public int compareTo(Student o) {
        return this.age-o.age;
    }

    @Override
    public String toString(){

        return "{name=" + this.name +"," +
        "age="+ this.age + "}";

    }


    public static void main(String[] args){
        Map<Integer, Student> studentMap = new HashMap<>();
        studentMap.put(101, new Student("Udit", 25));
        studentMap.put(102, new Student("Smile", 29));
        studentMap.put(103, new Student("Rajan", 19));

        System.out.println("Original Map: " + studentMap);


        List<Student> students = new ArrayList<>(studentMap.values());

        Collections.sort(students,Comparator.comparing(Student::getName));
        System.out.println("Students sorted by name: " + students);
        

    }
    
}
