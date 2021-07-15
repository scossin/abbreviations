load_file <- function(filename, header = T) {
  res <- data.table::fread(file = filename,
                           sep="\t",
                           header=header,
                           quote="")
  return(res)
}

get_noun_phrases_algo_2 <- function(noun_phrases, res_algo_2) {
  noun_phrases$shortForm <- unlist(lapply(noun_phrases$lemma, function(x){
    tokens <- unlist(strsplit(x = x, split=" "))
    return(tokens[1])
  }))
  short_in_res <- noun_phrases$shortForm %in% res_algo_2$shortForm
  noun_phrases <- subset(noun_phrases, short_in_res)
  return(noun_phrases)
}

get_abb_algo_1 <- function(res_algo_1) {
  bool <- res_algo_1$FreqTermLongForm > res_algo_1$FreqTermShortForm
  res_algo_1 <- subset(res_algo_1, bool)
  sub_res_1 <- subset(res_algo_1, select=c("shortForm","longForm","FreqTermShortForm"))
  colnames(sub_res_1) <- c("shortForm","longForm","freqShort")
  return(sub_res_1)
}

get_abb_algo_2 <- function(res_algo_2) {
  sub_res_2 <- subset(res_algo_2, select=c("shortForm","longForm","freqLong"))
  colnames(sub_res_2) <- c("shortForm","longForm","freqShort")
  return(sub_res_2)
}