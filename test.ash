class Bacon {

    init(name) {
        this.name = name;
        this.text = "Hello World " + this.name;
    }

    sayIt() {
        print this.text;
    }
}

var bacon = Bacon("Yahya");
bacon.sayIt();