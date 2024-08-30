fun closureExample() {
    var x = 10;
    fun closure() {
        var x = 12;
        x = x + 1;
        print "Closure x: " + x;
    }
    closure();
    print "Outer x: " + x;
}

closureExample();