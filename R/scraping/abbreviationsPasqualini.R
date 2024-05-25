## http://abreviationsmedicales.ch/abreviation/AAS
# licence creative commons
# Attribution-NonCommercial 4.0 International (CC BY-NC 4.0) : https://creativecommons.org/licenses/by-nc/4.0/
# Attribution: Créé et maintenu par Christine Pasqualini, codeuse médicale aux Hôpitaux Universitaires de Genève.

url <- "http://abreviationsmedicales.ch/abreviation/AAS"
library(rvest)
page <- xml2::read_html(x = url)

abbr_list <- page %>% html_node("#abbr-list")
h3 <- abbr_list %>% html_nodes(xpath = "//h3") 
abbs <- h3 %>% html_text()

df <- NULL
abbs <- abbs[-1] ## first is empty ""
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

source("R/scraping/normalize.R")
df_norm <- df
df_norm$shortForm <- normalize(df$shortForm,"UTF-8")
df_norm$longForm <- normalize(df_norm$longForm,"UTF-8")
df_norm <- unique(df_norm)
bool <- df_norm$longForm == " " | df_norm$shortForm == " "
sum(bool)
df_norm <- subset(df_norm, !bool)
sum(is.na(df_norm$longForm))
pasqualini_abbs <- df_norm
filename <-  "R/scraping/pasqualini_abbs.tsv"
write.table(x = pasqualini_abbs,
            file = filename,
            sep="\t",
            col.names = T,
            row.names = F,
            fileEncoding = "UTF-8",
            quote = F)

# Check we can read it:
test <- read.table(file = filename,
                   header = T,
                   sep = "\t",
                   quote = "")
nrow(test) == nrow(pasqualini_abbs) # TRUE
