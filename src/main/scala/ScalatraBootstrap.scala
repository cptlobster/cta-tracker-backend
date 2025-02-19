import dev.cptlobster.cta_tracker._
import org.scalatra._
import jakarta.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext): Unit = {
    context.mount(new CtaTrackerServlet, "/*")
  }
}
