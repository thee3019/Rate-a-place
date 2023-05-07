package controllers;

import models.User;
import models.UserRepository;

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

public class UserController extends Controller {

    private final FormFactory formFactory;
    private final UserRepository userRepository;
    private final HttpExecutionContext ec;

    @Inject
    public UserController(FormFactory formFactory, UserRepository userRepository, HttpExecutionContext ec) {
        this.formFactory = formFactory;
        this.userRepository = userRepository;
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
    public CompletionStage<Result> addUser() {
        JsonNode js=request().body().asJson();
        User user= Json.fromJson(js,User.class);
        return userRepository.add(user).thenApplyAsync(p -> {
            //return redirect(routes.UserController.index());
            return ok("inserted name.."+user.username);
        }, ec.current());
    }
    public CompletionStage<Result> getUsers() {
        return userRepository
                .list()
                .thenApplyAsync(userStream -> ok(toJson(userStream.collect(Collectors.toList()))), ec.current());
    }

    public Result login(){
        JsonNode j = request().body().asJson();
        String email = j.get("email").asText();
        String password = j.get("password").asText();
        User ps = userRepository.login(email,password);
        if(ps.email== null || ps.password==null){
            return null;
        }
        else{
            String msg = "{\"name\":\""+ps.username+"\"," +
                    "\"id\":\""+ps.id+"\"}";
            return ok(Json.parse(msg));
        }
    }



}
