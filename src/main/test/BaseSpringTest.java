import com.mall.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

//指定bean注入的配置文件
@ContextConfiguration(locations = {"classpath:/spring/applicationContext-srping.xml", "classpath:/spring/servletContext-mvc.xml"})
//使用标准的JUnit @RunWith注释来告诉JUnit使用Spring TestRunner
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class BaseSpringTest extends AbstractJUnit4SpringContextTests {
    Logger logger = LogManager.getLogger("AsyncLogger");

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private CartService cartService;
    @Autowired
    private ShippingService shippingService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;


}
