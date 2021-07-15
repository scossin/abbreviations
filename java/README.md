# Algorithms that detect abbreviations

## Build  
To build it, you will need Java 1.8 (or higher) JDK a recent version of Maven (https://maven.apache.org/download.cgi) and put the `mvn` command on your path. Now you can run `mvn clean package` 

## Abbreviations detection
To detect abbreviations the algorithms expect a list of noun phrases. A file example is given in the *nounphrases* folder. The file must contain two columns: the noun phrase and its frequency. In the paper, French nounphrases were extracted with this program: https://github.com/scossin/CandidateTerm


Run algorithm 1:  
```bash
java -jar algo1/target/algo1-0.0.1.jar ./nounphrases/example_nounphrases.tsv ./res_algo1.txt
```

Run algorithm2: 
```bash
java -jar algo2/target/algo2-0.0.1.jar ./nounphrases/example_nounphrases.tsv ./res_algo2.txt
```
