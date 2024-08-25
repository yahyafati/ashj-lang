var a = 0;
while (a < 10) {
    if (a == 2) {
        a = a + 1;
        continue;
    }
    print a;
    if (a == 7) {
        break;
    }
    a = a + 1;
}