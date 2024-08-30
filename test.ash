class Animal {

    name() {
        return "Animal";
    }
}

class Dog < Animal {

    name() {
        return "Dog";
    }
}

var dog = Dog();
print dog.name();