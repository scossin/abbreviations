# http://www.cnci.univ-paris5.fr/medecine/abreviations.html
library(rvest)
page <- xml2::read_html(x = "http://www.cnci.univ-paris5.fr/medecine/abreviations.html")
tables <- page %>% html_nodes(xpath = "//li")
tables[[1]] %>% html_text()
df <- NULL
i <- 1
df <- vector(mode="character",length = length(tables))
for (i in 1:length(tables)){
  ajout <- tables[[i]] %>% html_text()
  df[i] <- ajout
}
regex_shortForm <- "[^ ]+"
shortForm <- stringr::str_extract(string = df,pattern = regex_shortForm)
df <- data.frame(
  text = df,
  shortForm = shortForm
)
i <- 1
for (i in 1:nrow(df)){
  nchar_short <- nchar(df$shortForm[i])
  df$longForm[i] <- substr(x = df$text[i],
                     start = nchar_short + 1,
                     stop = nchar(df$text[i]))
}
df$longForm <- gsub("^ :", "", df$longForm)
bool <- df$shortForm == "(exit:"
sum(bool)
df <- subset(df, !bool)
df$text <- NULL
colnames(df) <- c("shortForm","longForm")

source("./normalize.R")
df$shortForm <- normalize(df$shortForm,"UTF-8")
df$longForm <- normalize(df$longForm,"UTF-8")
df$longForm <- gsub("\r\n","",df$longForm)
cnci_abbs <- df
write.table(x = cnci_abbs,
            file = "./cnci_abbs.tsv",
            sep="\t",
            col.names = T,
            row.names = F,
            fileEncoding = "UTF-8")

df <- read.table(file = "./cnci_abbs.tsv",
                 sep = "\t",
                 header=T)
