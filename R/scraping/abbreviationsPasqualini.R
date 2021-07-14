## http://abreviationsmedicales.ch/abreviation/AAS
# licence creative commons
# Attribution-NonCommercial 4.0 International (CC BY-NC 4.0) 
# script non reproductible

url <- "http://abreviationsmedicales.ch/abreviation/AAS"
library(rvest)
page <- xml2::read_html(x = url)

abbr_list <- page %>% html_node("#abbr-list")
h3 <- shortForms %>% html_nodes(xpath = "//h3") 
abbs <- h3 %>% html_text()

abbr_list %>% html_text()

abb <- "BHE"
df <- NULL
abbs <- abbs[-1] ## first is empty ""
table(nchar(abbs))
for (abb in abbs[1:length(abbs)]) {
  print(abb)
  tryCatch(expr = {
    url <- paste0("http://abreviationsmedicales.ch/wp-content/themes/abreviationsmedicales/getAbrevAjax.php?abbr=",abb)
    page_right <- xml2::read_html(x = URLencode(url))
    longForms <- page_right %>% html_nodes("section > p") %>% html_text()
    ajout <- data.frame(
      shortForm = abb,
      longForm = longForms
    )
    df <- rbind(df,
                ajout)
    Sys.sleep(0.1)
  }, error = function(e){
    print(e)
  })
}

source("normalize.R")
empty_char <- df$longForm[22]
df$shortForm2 <- gsub(empty_char, " ", df$shortForm, useBytes = T)
df$longForm <- gsub(empty_char, " ", df$longForm, useBytes = T)
df$shortForm <- normalize(df$shortForm,"UTF-8")
df$longForm <- normalize(df$longForm, "UTF-8")
df <- unique(df)
bool <- df$longForm == " " | df$shortForm == " "
sum(bool)
df <- subset(df, !bool)
sum(is.na(df$longForm))
df$longForm[20:30]

pasqualini_abbs <- df
write.table(x = pasqualini_abbs,
            file = "./pasqualini_abbs.tsv",
            sep="\t",
            col.names = T,
            row.names = F,
            fileEncoding = "UTF-8")