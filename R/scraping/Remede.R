## retrieve abbreviations on this site: http://www.remede.org/projets/dico/dico.html

library(rvest)
getAbb <- function(remedeURL) {
  page <- xml2::read_html(x = url)
  shortForms <- page %>% html_nodes(xpath = "//dl/dt")  %>%  html_text
  longForms <- page %>% html_nodes(xpath = "//dl/dd")  %>%  html_text
  if (!length(shortForms) == length(longForms)) {
    stop("short and longForms don't have the same length")
  }
  ajout <- data.frame(shortForm = shortForms,
                      longForm = longForms)
  return(ajout)
}

letter <- LETTERS[2]
df <- NULL
for (letter in LETTERS) {
  print(letter)
  url <- paste0("http://www.remede.org/projets/dico/dico.html?lettre=",letter)
  new_abbs <- getAbb(url)
  df <- rbind(df,
              new_abbs)
  Sys.sleep(time = 1)
}

source("./normalize.R")
df$shortForm <- normalize(df$shortForm,"ANSI")
df$longForm <- normalize(df$longForm,"ANSI")
remedeAbbs <- df
write.table(x = remedeAbbs,
            file = "remedeAbbs.tsv",
            sep="\t",
            col.names = T,
            row.names = F,
            fileEncoding = "UTF-8")

df <- read.table(file = "./remedeAbbs.tsv",
                 sep = "\t",
                 header=T)

