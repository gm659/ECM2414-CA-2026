A card game using Java.
<br/><br/>

**Requirements:**
- JDK 17+ (Tested and ran on JDK 25)
- JUnit 4.13.2
- Hamcrest Core 1.3 (required by JUnit 4)
<br/><br/>

**Running the game:**

    java -jar cards.jar
<br/><br/>

**Compiling the tests:**

    mkdir out\test
    javac -cp "cards.jar;lib\*" -d out\test test\*.java
<br/><br/>

**Running the tests:**

    java -cp "cards.jar;out\test;lib\*" org.junit.runner.JUnitCore AllTests
