import groovy.json.JsonOutput
@Grab("thymeleaf-spring4")
@Grab('groovy-sql')
@Grab('groovy-json')
//@Grab("org.codehaus.groovy:groovy-json")
// @Grab(group='org.postgresql', module='postgresql', version='9.4-1205-jdbc42')

import org.springframework.core.env.*
import com.fasterxml.jackson.databind.*
import groovy.sql.Sql
import groovy.json.*

@Controller
class Application {


	@Autowired
	private ObjectMapper json;

	@Value('${VCAP_APPLICATION:{}}')
	private String application;

	@Value('${VCAP_SERVICES:{}}')
	private String services;

	def dbUrl      = "jdbc:postgresql://stampy.db.elephantsql.com:5432/tvttqqbo"
	def dbUser     = "tvttqqbo"
	def dbPassword = "X0kh2qDEon9S95yPGIQmgssqUomBA_Dv"
	def dbDriver   = "org.postgresql.Driver"

	def sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriver)

	@RequestMapping("/services")
	public String index(Model model) {
		model.addAttribute("cfapp", json.readValue(application, LinkedHashMap.class))
		try {
			def cfservices = json.readValue(services, LinkedHashMap.class)
			def cfservicename = cfservices.keySet().iterator().next();
			def cfservice = cfservices.get(cfservicename).get(0);
			model.addAttribute("cfservices", cfservices)
			model.addAttribute("cfservicename", cfservicename)
			model.addAttribute("cfservice", cfservice)
		} catch (Exception ex) {
			// No services
			model.addAttribute("cfservice", "")
			model.addAttribute("cfservice", new LinkedHashMap())
		}
		return "index"
	}

  @GetMapping("/message")
  public @ResponseBody ResponseEntity<String> getMessage() {
	def rows = sql.rows("select * from ar_messages where recipient = 'todd@rimesmedia.com'")
	// def output = rows.join('\n')
	def output = new JsonBuilder(messages:rows).toPrettyString()

    return new ResponseEntity<String>(output, HttpStatus.OK);
  }
    @GetMapping("/")
    public @ResponseBody ResponseEntity<String> getHome() {
        return new ResponseEntity<String>("Hello World!",HttpStatus.OK);
    }
    @GetMapping("/message/{id}")
    public @ResponseBody ResponseEntity<String> getMessageById(@PathVariable String id) {
        return new ResponseEntity<String>("Response from GET with id " +id,HttpStatus.OK);
    }
  @PostMapping("/message")
  public @ResponseBody ResponseEntity<String> postMessage() {
    return new ResponseEntity<String>("Response from POST method", HttpStatus.OK);
  }
  @PutMapping("/message")
  public @ResponseBody ResponseEntity<String> putMessage() {
    return new ResponseEntity<String>("Response from PUT method", HttpStatus.OK);
  }
  @DeleteMapping("/message")
  public @ResponseBody ResponseEntity<String> deleteMessage() {
    return new ResponseEntity<String>("Response from DELETE method", HttpStatus.OK); 
  }
  @PatchMapping("/message")
  public @ResponseBody ResponseEntity<String> patchMessage() {
    return new ResponseEntity<String>("Response from PATCH method", HttpStatus.OK); 
  }

}
