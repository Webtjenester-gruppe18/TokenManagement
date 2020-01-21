package ws18;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.CucumberOptions.SnippetType;
import org.junit.runner.RunWith;

/* Important: 
for Cucumber tests to be recognized by Maven, the class name has to have
either the word Test in the beginning or at the end. 
For example, the class name CucumberTests (Test with an s) will be ignored by Maven.
*/

/**
 * @author Oliver Køppen, s175108
 */

@RunWith(Cucumber.class)
@CucumberOptions(features="features", snippets=SnippetType.CAMELCASE)
public class CucumberTest {
}
