#
# This is a Shiny web application. You can run the application by clicking
# the 'Run App' button above.
#
# Find out more about building applications with Shiny here:
#
#    http://shiny.rstudio.com/
#

# Run the application 
# shinyApp(ui = ui, server = server)
runApp(appDir = getwd(), port = 3720,
       launch.browser = getOption("shiny.launch.browser", interactive()),
       host = "0.0.0.0", workerId = "",
       quiet = FALSE, display.mode = c("auto", "normal", "showcase"),
       test.mode = getOption("shiny.testmode", FALSE))