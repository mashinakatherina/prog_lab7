package ifmo.programming.lab7.client;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Human implements Comparable<Human>, Serializable {

    private String name /*= "Безымянная";*/;
    private int height, age, weight = 50; //weight - фиксированнвя глубина, age - ширина, height - высота
    private int x, y; //только по длине и высоте
//    private Clock clock = Clock.system(ZoneId.of("Europe/Moscow"));
    private LocalDateTime creationDate = LocalDateTime.now();
    private Skill skill;
    private int size;

    Human(int age, int height, int x, int y) {
        setBounds(x, y, age, height);
    }

    Human(int age, int height, int x, int y, String name) {
        setBounds(x, y, age, height);
        setName(name);
    }

    public Human(int age, int height, int x, int y, String name, Skill skill) {
        setBounds(x, y, age, height);
        size = age *height* weight;
        setName(name);
        this.skill = skill;
    }

    public Skill getSkill() { return skill; }


    public void setBounds(int x, int y, int age, int height) {
        setPosition(x, y);
        setSize(age, height);
    }

    public void setPosition(int x, int y) {
        setX(x);
        setY(y);
    }

    public void setSize(int age, int height) {
        setAge(age);
        setHeight(height);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }

    public int getWeight() { return weight; }
    public int getAge() { return age; }
    public int getHeight() { return height; }

    public void setAge(int age) {
        if (age < 0)
            throw new IllegalArgumentException("Ширина не может быть отрицательной");
        this.age = age;
    }

    public void setHeight(int height) {
        if (height < 0)
            throw new IllegalArgumentException("Высота не может быть отрицательной");
        this.height = height;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDateTime getCreationDate() {return creationDate; }

    /**
     *осуществляет сортировку по имени
     */
    @Override
    public int compareTo(Human o) {
        return this.getName().compareTo(o.getName());
    }

    /**
     * читабельное представление об объекте
     * @return String - информация о человеке
     */
    @Override
    public String toString() {

        StringBuilder humaninfo = new StringBuilder("Человек");
        if (name.isEmpty()) {
            humaninfo.append("без имени");
        } else {
            humaninfo.append("-" + this.getName());
        }
        humaninfo.append(", возраст: " + age + " рост: " + height + " вес: " + weight + ", ")
                .append("и координаты: x: " + getX() + ", y: " + getY() );
        if (skill.getThingcount() == 0) {humaninfo.append(" , без навыков.");}
        else {
            humaninfo.append(", имеющий " + skill.getThingcount() + " навыков.");
        }
        return humaninfo.toString();

    }


    public static /*static*/ class Skill implements Serializable {
        private int thingcount;
        private String name;

        public Skill(String name) {
            setName(name);
        }

        public Skill() {}

        public Skill(int thingcount, String name) {
            setName(name);
            setThingcount(thingcount);
        }

        public void setThingcount(int thingcount) {this.thingcount = thingcount;}
        public int getThingcount() {return thingcount;}

        public String getName() {return name;}
        public void setName(String name) {this.name = name;}

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Skill skill = (Skill) o;
            return thingcount == skill.thingcount &&
                    Objects.equals(name, skill.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(thingcount, name);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Human human = (Human) o;
        return height == human.height &&
                age == human.age &&
                weight == human.weight &&
                x == human.x &&
                y == human.y &&
                Objects.equals(name, human.name) &&
                Objects.equals(skill, human.skill);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, height, age, weight, x, y, creationDate, skill);
    }
}
