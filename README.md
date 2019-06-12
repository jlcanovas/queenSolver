# queenSolver

Java implementation for the N Queens problem + additional constraint:

* Problem: Place N queens on an NxN chess board so that none of them attack each other.
* Constraint:  make sure that no three queens are in a straight line at ANY angle, so queens on A1, C2 and E3, despite not attacking each other, form a straight line at some angle.

## Documentation

The source code has been fruitfully commented to explain the behavior, we kindly invite you to have a look :)

## Runing the tool

The fastest way to run the implementation is to execute the following command, which will execute the main method of the QueenSolver class for a board of size 4.

```
gradle run
```

If you want to play with different board sizes, just change the value of the parameter in the main method.

There are also some test cases to illustrate the behaviour of the methods, this is the command to run them:

```
gradle test
```

## Dependencies

The project uses [gradle](https://gradle.org/) as build tool and has these dependencies:

* [Junit](https://junit.org) 4.12
* [Apache Commons Mathematics Library](https://commons.apache.org/proper/commons-math/) 3.6

## Questions

Any comment to improve the solution or notify bugs will be welcome.