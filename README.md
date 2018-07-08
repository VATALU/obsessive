# Obsessive
Obsessive is a featherweight framework for JavaWeb development based on Vert.x. Obsessive is aimed to help you to code in the style of spring-boot. 
## 1.Feature
    * Follow the COC principle
    * IoC
    * Use Annotation for Configuration
## 2.Environment
### 2.1 Download and install
    git clone https://github.com/VATALU/Obsessive.git
    cd .\Obsessive\Obsessive\
    mvn install
### 2.2 Append dependency

Creare a web app project by Maven and append dependency in your `pom.xml`

    <dependency>
            <groupId>org.vatalu</groupId>
            <artifactId>Obsessive</artifactId>
            <version>1.0.0-beta</version>
    </dependency>

## 3.Example
The Obsessive-sample will show the example how to use Obsessive.

In your project, you can provide main entry only as following to run Obsessive.

``` Java
public class Main {
    public static void main(String[] args) {
        ObsessiveStarter.run(Main.class);
    }
}
```

Obsessive will scan all annotated class which in the same directory of Class `Main` or under it.

@Controller

``` Java
@Controller
public class UserController {
    @Inject
    private UserService userService;

    @Value("Obsessive")
    private String value;

    @Route(value = "/users/:userId", method = HttpMethod.GET)
    public void helloWorld(RoutingContext routingContext) {
        String userId = routingContext.request().getParam("userId");
        User user = userService.findUserById(userId);
        routingContext.response().setChunked(true).write("Hello World " + user.getUserName() + ", Welcome to " + value).end();
        }
}
```

@Service

``` Java
@Service
public class UserService {
    @Inject
    private UserRepository userRepository;

    public User findUserById(String id) {
        //do something
        return userRepository.findUserById(Long.parseLong(id));
    }
}
```

@Repository

``` Java
@Repository
public class UserRepository {

    //simple demo
    public User findUserById(long id) {
        User user = new User();
        user.setUserName("VATALU");
        user.setUserId(id);
        return user;
    }
}
```

This example accept HTTP GET requests at

    http://localhost:8080/users/{userId}
and response

    Hello World VATALU, Welcome to Obsessive.

You also can config your server port or server host in Obsessive.properties.

    server.port=8081
    server.host=obsessive

## 4.Future
Request Param Parser
## 5.More
[Vert-x](https://vertx.io/)
