mochajs-eclipse
===============

Do you want to unit test your Node.js JavaScript inside Eclipse with Mocha.js?
Look no further.



Get started
-----------

The easy way would be:

1. Clone this repo.
2. Import this as Existing Project inside Eclipse.
3. Find MochaTestSuiteWithAnnotation Java class inside test/jsrunner directory.
4. Change the value of @TestsFolder("path-to-my-mocha-test-folder") annotation and point it to yout Mocha.js test folder.
5. Right-click Run As JUnit.
6. Enjoy.

![A screen capture of Eclipse's JUnit view running Mocha tests](doc/eclipse-junit-view.png "Eclipse's JUnit view running Mocha tests")


Of course, this is a super-hack. It's not what it's supposed to be, but hey, it's a good start!
The idea is from http://js-testrunner.codehaus.org/index.html, which runs browser-side JavaScript with Phantom.js
