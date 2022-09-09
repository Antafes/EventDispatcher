package scripts.laniax.framework.event_dispatcher;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Laniax
 */
@Test
public class EventTest {

    private TestEvent event;

    @BeforeMethod
    public void setUp()
    {
        event = new TestEvent();
    }

    @AfterMethod
    public void tearDown()
    {
        event = null;
    }

    public void isPropagationStopped()
    {
        Assert.assertFalse(event.isPropagationStopped());
    }

    public void stopPropagation()
    {
        event.stopPropagation();
        Assert.assertTrue(event.isPropagationStopped());
    }
}
