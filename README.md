<p align="center"> 
    <img height=170 src="https://cdn.discordapp.com/attachments/326432556037832704/936299117766926406/logo3.png"/> 
</p>

## <div align="center">Pythia</div>

#### <div align="center">Java library that produces an automated statistical profile of an input dataset.</div>

A standard dataset is just a text file, with lines, where each line is a record, the fields of which
are separated by a separator (eg. tabs, comma, pipe, etc).
After registering a data set, the system produces a 100% automatic statistical profile of the dataset
and generates a report of the findings.

### <div align="center">Setup</div>

---

#### Intellij IDEA Installation
Requirements
- You need to have [**Intellij IDEA**](https://www.jetbrains.com/idea/download/#section=windows) installed (Community edition is free)
- Import the project as a Maven project and it runs out of the box

#### Eclipse Installation
Requirements
- You need to have [**Eclipse**](https://www.eclipse.org/downloads/) installed
- Import the project as a Maven project.

_Note_: This project uses [**lombok**](https://projectlombok.org/) to generate boilerplate code at compile time

- Follow the instructions at Lombok's official website to set it up in Eclipse



#### Maven
The project uses a Maven wrapper so there is no need to install it to your system as long as you have the JAVA_HOME 
environmental variable pointing to your [**Java 8**](https://www.oracle.com/java/technologies/downloads/) installation folder.

### <div align="center">Run with Maven</div>

---

Navigate to the root folder of the repo and run,
~~~~
./mvnw clean install
~~~~
and wait for the procedure to finish

After that, there should be a folder called `target` that has two jar files:
~~~~
Pythia-x.y.z-all-deps.jar and Pythia-x.y.z.jar
~~~~
The difference is that the all deps jar file is an uber jar so you can import Pythia to a project and run it out of the box. (All dependecies are embedded into the all deps jar)
* Otherwise you will need to provide the Pythia dependencies to your pom.xml file.

To run with the driver Main method, navigate to the root folder of the repo:
~~~~
java -jar target/Pythia-x.y.z-all-deps.jar
~~~~