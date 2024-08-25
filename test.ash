var a = 0;
while (a < 10) {
    print "a: " + a;
    var b = a;

    for (var i = 0; i < b; i = i + 1) {
        print i;

        if (i == 5) {
            break;
        }
    }
    if (b == 6) {
        break;
    }
    print "b - end";
    a = a + 1;
}