var a = "This is a test";
var b = "This is a test too";

print a;
print b;

var c = a + " " + b;
print c;

print a + " " + b + " " + c;

var d = 10;
var e = 20;
var f = d + e;

print d;

// var z = 0;
// var y = 1;
// print y / z;

// Scope test
var x = 10;
{
    var x = 20;
    print "Local scope x: " + x;
}
print "Global scope x: " + x;

if (x == 10) {
   print "x is not 20" + " x is " + x;
} else {
     print "x is 20";
}