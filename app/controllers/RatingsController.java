package controllers;

import models.Ratings;
import models.RatingsRepository;

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

public class RatingsController extends Controller {

    private final FormFactory formFactory;
    private final RatingsRepository ratingsRepository;
    private final HttpExecutionContext ec;

    @Inject
    public RatingsController(FormFactory formFactory, RatingsRepository ratingsRepository, HttpExecutionContext ec) {
        this.formFactory = formFactory;
        this.ratingsRepository = ratingsRepository;
        this.ec = ec;
    }

    public Result index() {
        return ok(views.html.index.render());
    }

//    public CompletionStage<Result> addRatings() {
//        Ratings ratings = formFactory.form(Ratings.class).bindFromRequest().get();
//        return ratingsRepository
//                .add(ratings)
//                .thenApplyAsync(p -> redirect(routes.RatingsController.index()), ec.current());
//    }

    public CompletionStage<Result> addRatings() {
        JsonNode js=request().body().asJson();
        Ratings ratings= Json.fromJson(js,Ratings.class);
        return ratingsRepository.add(ratings).thenApplyAsync(p -> {
            //return redirect(routes.PlacesController.index());
            return ok("inserted rating.."+ratings.review);
        }, ec.current());
    }
    public CompletionStage<Result> getRatings() {
        return ratingsRepository
                .list()
                .thenApplyAsync(ratingsStream -> ok(toJson(ratingsStream.collect(Collectors.toList()))), ec.current());
    }

}
