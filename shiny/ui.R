# Define UI for application that draws a histogram
ui <- fluidPage(
  tags$head(
    tags$link(rel = "stylesheet", type = "text/css", href = "./css/style.css"),
    ## barre l'abbreviation 
    tags$script("function toggleBarre() {
                  if (document.getElementById('not_abb').checked) {
                        document.getElementById('abbreviation_text').classList.add('barre')
                  } else {
                      document.getElementById('abbreviation_text').classList.remove('barre')
                  }
                }"),
    ## commencer:
    tags$script("function begin() {
                  document.getElementById('username').selectize.disable();
                  document.getElementById('begin_button').style.display='none';
                  document.getElementById('application').style.display='block';
                  document.getElementById('user').classList.remove('username_before_initialization')
                  document.getElementById('user').classList.add('username_after_initialization')
                }"),
    shinyjs::useShinyjs(),
    shinyjs::extendShinyjs(text = "shinyjs.displayApp = function() { begin();}",
                           functions=c("displayApp"))
  ),
  
  ### the user types his/her username and clicks the button "begins!"
  div(id="user", class="username_before_initialization",
      shiny::selectInput(inputId="username",
                       choices=c(usernames),
                       selected="Margaux",
                       label="Entrez votre username:"),
      shiny::actionButton(inputId = "begin_button",
                          label = "Commencer !")
  ),
  
  ### 
  div (id = "application",
       div(id="abb",
          # shiny::span("Abbreviation: "),
           div(id = "anchor_abbreviation"),
           # shiny::uiOutput(outputId = "abbreviation"),
           div(id="not_abb_div",
             shinyWidgets::awesomeCheckbox(inputId = "not_abb",
                                           label= "Ceci n'est pas une abbreviation",
                                           value = FALSE,
                                           status = "danger"),
             onclick='toggleBarre();'),
          div (
            shiny::actionButton(inputId = "save",
                                label="Sauvegarder et passer au suivant")
          ),
           div(id="anchor_progression"),
       ),
       
       ### propositions et entrées manuelles
       div(
         div ( ## propositions auto
           id="props",
           # shiny::h3("Propositions:"),
           div(id="anchor_props"),
           div(id="anchor_examples")
         ),
         
         div ( ## propositions manuelles
           id="user_prop",
           div(shiny::h3("Vos propositions:"),
               div(id="anchor_user_proposition"))
         )
       )
  ) ##end of app
)