$ ->
  $.get "/persons", (persons) ->
    $.each persons, (index, person) ->
      $("#persons").append("<li>"+ person.name + "( " + person.city + " ) <a href='/persons/update/" + person.name + "'> Edit </a></li>")