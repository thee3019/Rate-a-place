package controllers;

import models.Places;
import models.PlacesRepository;

import com.fasterxml.jackson.databind.JsonNode;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.libs.Json;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static play.libs.Json.toJson;

public class PlacesController extends Controller {

    private final FormFactory formFactory;
    private final PlacesRepository placesRepository;
    private final HttpExecutionContext ec;

    @Inject
    public PlacesController(FormFactory formFactory, PlacesRepository placesRepository, HttpExecutionContext ec) {
        this.formFactory = formFactory;
        this.placesRepository = placesRepository;
        this.ec = ec;
    }

    public Result index() {
        return ok(views.html.index.render());
    }

    //    public CompletionStage<Result> addUser() {
//        User user = formFactory.form(User.class).bindFromRequest().get();
//        return userRepository
//                .add(user)
//                .thenApplyAsync(p -> redirect(routes.UserController.index()), ec.current());
//    }
    public CompletionStage<Result> addPlace() {
        JsonNode js=request().body().asJson();
        Places place= Json.fromJson(js,Places.class);
        return placesRepository.add(place).thenApplyAsync(p -> {
            //return redirect(routes.PlacesController.index());
            return ok("inserted name.."+place.name);
        }, ec.current());
    }
    public CompletionStage<Result> getPlaces() {
        return placesRepository
                .list()
                .thenApplyAsync(placeStream -> ok(toJson(placeStream.collect(Collectors.toList()))), ec.current());
    }

}
