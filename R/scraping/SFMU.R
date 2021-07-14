## sfmu
# https://www.sfmu.org/upload/70_formation/02_eformation/02_congres/Urgences/urgences2009/donnees/aide/fs_abreviations.htm
library(rvest)
page <- xml2::read_html(x = "https://www.sfmu.org/upload/70_formation/02_eformation/02_congres/Urgences/urgences2009/donnees/aide/abreviations.htm")
tables <- page %>% html_nodes(xpath = "//table")
df <- NULL
i <- 1
for (i in 1:length(tables)){
  ajout <- tables[[i]] %>% html_table()
  df <- rbind(df, ajout)
}
colnames(df) <- c("shortForm","longForm")

source("normalize.R")
df$shortForm <- normalize(df$shortForm,"UTF-8")
df$longForm <- normalize(df$longForm,"UTF-8")
write.table(x = df,
            file="sfmu.tsv",
            sep="\t",
            col.names = T,
            row.names = F)

smfu <- read.table("./sfmu.tsv",
                   sep="\t",
                   header=T)
