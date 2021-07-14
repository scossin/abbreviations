normalize <- function(x, encoding){
  x <- gsub(pattern = "[ ]+"," ",x)
  x <- trimws(x)
  x <- iconv(x, from = encoding, to="ASCII//TRANSLIT")
  return(x)
}
