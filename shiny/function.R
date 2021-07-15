source("add_ui.R")

# FOLDER_SAVE <- "Sebastien"
getLastIdAbb <- function(FOLDER_SAVE) {
  json_files <- list.files(path = FOLDER_SAVE,pattern = "json$",full.names = T)
  if (length(json_files) == 0) {
    return (1)
  }
  idabbs <- as.numeric(stringr::str_extract(json_files, pattern="[0-9]+"))
  idabbs <- sort(idabbs,decreasing = T)
  idabb <- idabbs[1] 
  return(idabb)
}

getJson <- function(FOLDER_SAVE, idabb) {
  jsonFileName <- getJsonFileName(FOLDER_SAVE, idabb)
  if (file.exists(jsonFileName)) {
    json <- jsonlite::fromJSON(jsonlite::fromJSON(txt = jsonFileName))
    return(json)
  } else {
    return(NULL)
  }
}

save_to_json <- function(input, FOLDER_SAVE) {
  json_list <- list(
    not_abb = input$not_abb,
    checked = input$checkGroup,
    user_proposition = input$user_proposition_values,
    username = input$username,
    idabb = input$idabb
  )
  json <- jsonlite::toJSON(json_list)
  print(json_list)
  filename <- getJsonFileName(FOLDER_SAVE,input$idabb)
  jsonlite::write_json(x = json,
                       path = filename)
}

getJsonFileName <- function(FOLDER_SAVE, idabb) {
  return(paste0(FOLDER_SAVE, "/abb",idabb,".json"))
}