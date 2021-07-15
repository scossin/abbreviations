#' @description remove and add UI abbreviation
#' @param output: shiny output
#' @param abbreviation: an abbreviation like "avc"
add_abbreviation <- function(output, abbreviation) {
  print("removing/adding abbreviation UI")
  shiny::removeUI(selector = "#abbreviation",
                  immediate = T)
  ui <- shiny::uiOutput(outputId = "abbreviation")
  shiny::insertUI(selector = "#anchor_abbreviation",
                  where = "afterBegin",
                  ui,
                  immediate = T)
  output$abbreviation <- shiny::renderUI({
    shiny::span(id="abbreviation_text",
                abbreviation)
  })
}


#' @description remove and add UI propositions
#' @param output: shiny output
#' @param choices: longForm choices
add_propositions <- function(output, choices, selected) {
  print("removing/adding propositions UI")
  shiny::removeUI(selector = "#checkGroup",
                  immediate = T)
  ui <- shiny::uiOutput(outputId = "propositions")
  shiny::insertUI(selector = "#anchor_props",
                  where = "afterBegin",
                  ui,
                  immediate = T)
  output$propositions <- shiny::renderUI({
    # Copy the chunk below to make a group of checkboxes
    checkboxGroupInput(inputId = "checkGroup",
                       label = h3("Propositions:"),
                       choices = unique(choices),
                       selected = selected,
                       inline=T)
  })
}

#' @description current progress
#' @param output: shiny output
#' @param id: id number of the abbreviation
add_progression <- function(output, id, n_abbs) {
  print("removing/adding progression UI")
  shiny::removeUI(selector = "#progression",
                  immediate = T)
  ui <- shiny::uiOutput(outputId = "progression")
  shiny::insertUI(selector = "#anchor_progression",
                  where = "afterBegin",
                  ui,
                  immediate = T)
  output$progression <- shiny::renderUI({
    # Copy the chunk below to make a group of checkboxes
    div (id="numerotation",
      shiny::numericInput(inputId = "idabb",
                          label = "N°",
                          value = id,
                          min = 1,
                          max = n_abbs,
                          step = 1,
                          width="70px"),
      shiny::actionButton(inputId = "change_id_abb",
                          label = "Go")
    )
  })
}


#' @description current progress
#' @param output: shiny output
#' @param id: id number of the abbreviation
add_user_proposition <- function(output, value) {
  id_anchor <- "anchor_user_proposition"
  id_ui <- "user_proposition"
  print("removing/adding user_proposition UI")
  shiny::removeUI(selector = paste0("#",id_ui),
                  immediate = T)
  ui <- shiny::uiOutput(outputId = id_ui)
  shiny::insertUI(selector = paste0("#",id_anchor),
                  where = "afterBegin",
                  ui,
                  immediate = T)
  output[[id_ui]] <- shiny::renderUI({
    # Copy the chunk below to make a group of checkboxes
    shiny::textAreaInput(inputId = "user_proposition_values",
                         label = "Significations:",
                         value = value,
                         placeholder = "Entrez une ou plusieurs significations, 1 par ligne:
proposition1
proposition2
...")
  })
}


#' @description current progress
#' @param output: shiny output
#' @param id: id number of the abbreviation
add_examples <- function(output, abb) {
  id_anchor <- "anchor_examples"
  id_ui <- "examples"
  print("removing/adding examples UI")
  shiny::removeUI(selector = paste0("#",id_ui),
                  immediate = T)
  ui <- DT::dataTableOutput(outputId = id_ui)
  shiny::insertUI(selector = paste0("#",id_anchor),
                  where = "afterBegin",
                  ui,
                  immediate = T)
  examples_short <- subset(examples, shortForm == abb)
  output[[id_ui]] <- DT::renderDataTable({
    # Copy the chunk below to make a group of checkboxes
    examples_short
  },options = list(
    pageLength = 5
  ),rownames= FALSE)
}
