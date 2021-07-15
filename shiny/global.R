## list of users
usernames <- readLines("./users")

library(data.table)
source("functions_load_files.R")
filename_nounphrases <- Sys.getenv("FILENAME_NOUNPHRASES")
filename_res_algo1 <- Sys.getenv("FILENAME_RES_ALGO1")
filename_res_algo2 <- Sys.getenv("FILENAME_RES_ALGO2")

noun_phrases <- load_file(filename_nounphrases, header=F)
colnames(noun_phrases) <- c("lemma","freq")

### Algo1
res_algo_1 <- load_file(filename_res_algo1)
abb_res_1 <- get_abb_algo_1(res_algo_1)
noun_phrases_algo1 <- subset(res_algo_1, select=c("shortForm","TermLongForm","FreqTermLongForm"))
colnames(noun_phrases_algo1) <- c("shortForm","lemma","freq")

### Algo2 
res_algo_2 <- load_file(filename_res_algo2)
abb_res_2 <- get_abb_algo_2(res_algo_2)
noun_phrases_algo2 <- get_noun_phrases_algo_2(noun_phrases, res_algo_2)

### Displayed examples of noun phrases
examples <- rbind(noun_phrases_algo1, noun_phrases_algo2)
examples <- examples[order(-examples$freq),]

### abbs to be validated:
abbs <- rbind(abb_res_1, abb_res_2)
abbs <- abbs[order(-abbs$freqShort),]
unique_short <- unique(abbs$shortForm)
length(unique_short) # number of abbs to validate in the interface
