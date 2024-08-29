fun closureExample() {
    var x = 10;
    fun closure() {
        x = x + 1;
    }
    closure();
    print x;
}

closureExample();