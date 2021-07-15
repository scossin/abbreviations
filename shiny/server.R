library(shiny)
library(jsonlite)
library(DT)
# source("./global.R") ## load global automatically
N_ABBS <- length(unique_short)
# Define server logic required to draw a histogram
# FOLDER_SAVE <- NULL ## where to save (username)
source("function.R")

## start the application
initialize <- function(output, FOLDER_SAVE) {
  idabb <- getLastIdAbb(FOLDER_SAVE)
  cat("initializing to idabb:", idabb, "\n")
  go_to_abb(output, FOLDER_SAVE, idabb)
}

go_to_abb <- function(output, FOLDER_SAVE, idabb) {
  idabb <- as.numeric(idabb)
  if (is.na(idabb) || idabb < 0 ) {
    shiny::showModal(shiny::modalDialog("Error: incorrect value of idAbb !"))
    return(NULL)
  }
  if (idabb > N_ABBS) {
    shiny::showModal(shiny::modalDialog("Vous avez terminé!"))
    return(NULL)
  }
  
  shortForm <- unique_short[idabb]
  print(shortForm)
  choices <- abbs$longForm[abbs$shortForm == shortForm]
  print(choices)
  json <- getJson(FOLDER_SAVE, idabb) ## NULL if not exists
  print(json)
  add_abbreviation(output, shortForm)
  add_propositions(output, 
                   choices = choices,
                   selected = json$checked)
  add_progression(output, 
                  id = idabb, 
                  n_abbs = N_ABBS)
  add_user_proposition(output, 
                       value = json$user_proposition)
  add_examples(output,
               abb = shortForm)
  if (is.null(json$not_abb)) {
    # shinyjs::js$uncheckBox()
    shiny::updateCheckboxInput(inputId = "not_abb",
                               value = F)
  } else if (json$not_abb == T){
    shiny::updateCheckboxInput(inputId = "not_abb",
                               value = T)
    # shinyjs::js$checkBox()
  } else {
    shiny::updateCheckboxInput(inputId = "not_abb",
                               value = F)
    # shinyjs::js$uncheckBox()
  }
}

server <- function(input, output) {
  #outputOptions(output, 'abbreviation', suspendWhenHidden=FALSE)
  ##
  observeEvent(input$begin_button, {
    ## create directory
    username <- input$username # choices 
    FOLDER_SAVE <- username ## Folder is username
    if (!dir.exists(FOLDER_SAVE)){
      dir.create(FOLDER_SAVE)
      shiny::showNotification(ui = "Première utilisation de l'application",
                              duration = 5,
                              type="warning")
    }
    shinyjs::js$displayApp()
    initialize(output, FOLDER_SAVE)
    # add_abbreviation(output, "avc")
    # add_propositions(output, 
    #                  choices = c("accident vasculaire cerebral","autre"),
    #                  selected = NULL)
    # add_progression(output, 
    #                 id = 1, 
    #                 n_abbs = N_ABBS)
    # add_user_proposition(output, value = NULL)
  })
  
  observeEvent(input$save, {
    FOLDER_SAVE <- input$username 
    save_to_json(input, FOLDER_SAVE)
    shiny::showNotification(ui = "Sauvegarder",
                            duration = 5,
                            type="message")
    go_to_abb(output, FOLDER_SAVE, input$idabb + 1)
  })
  
  observeEvent(input$change_id_abb, {
    print("user wants to go to id_abb: ")
    print(input$idabb)
    FOLDER_SAVE <- input$username 
    go_to_abb(output, FOLDER_SAVE, input$idabb)
  })
}

## getUI
