# https://fr.wikipedia.org/wiki/Liste_d'abr%C3%A9viations_en_m%C3%A9decine
# attention code non reproductible !
library(rvest)
page <- xml2::read_html(x = "https://fr.wikipedia.org/wiki/Liste_d'abr%C3%A9viations_en_m%C3%A9decine")

tables <- page %>% html_node('#mw-content-text') %>% html_nodes(xpath = "//li")
tables[[500]] %>% html_text()
df <- NULL
i <- 1
df <- vector(mode="character",length = length(tables))
for (i in 1:length(tables)){
  ajout <- tables[[i]] %>% html_text()
  df[i] <- ajout
}
df[1:100]
regex_shortForm <- "[^ ]+"
shortForm <- stringr::str_extract(string = df,pattern = regex_shortForm)
df2 <- data.frame(
  text = df,
  shortForm = shortForm
)

firstEntry <- "5-HiAA"
num_line <- which(df2$shortForm == "5-HiAA : ")
num_line <- 42 ## first entry "5-HiAA"
df2 <- df2[num_line:nrow(df2),]

i <- 1
for (i in 1:nrow(df2)){
  nchar_short <- nchar(df2$shortForm[i])
  df2$longForm[i] <- substr(x = df2$text[i],
                           start = nchar_short + 1,
                           stop = nchar(df2$text[i]))
}
df2$shortForm <- gsub(":", "", df2$shortForm)
df2$shortForm[1:10]
df2$longForm <- gsub("\\[[0-9]\\]","",df2$longForm)

df2$text <- NULL
colnames(df2) <- c("shortForm","longForm")
df2$longForm[1:10]
df2$longForm <- gsub("\u03b1","alpha",df2$longForm,useBytes = T)
source("./normalize.R")
df2$shortForm <- normalize(df2$shortForm,"UTF-8")
df2$longForm <- normalize(df2$longForm,"UTF-8")
bool <- df2$shortForm == "?"
df2 <- subset(df2,!bool)
bool <- df2$shortForm == "Liste"
which(bool)
df2 <- df2[1:1647,]
df2$longForm2 <- NULL

wikipedia_abbs <- df2
write.table(x = wikipedia_abbs,
            file = "./wikipedia_abbs.tsv",
            sep="\t",
            col.names = T,
            row.names = F,
            fileEncoding = "UTF-8")