package com.lvboaa.utils;

import org.junit.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@AutoConfigureMockMvc
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = UtilsApplication.class)
public class BaseTest {

	protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);


	@Autowired
	private WebApplicationContext context;

	@Autowired
	protected MockMvc mockMvc;

	@Before
	public void setup() {
		logger.info("初始化context...");
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@After
	public void after() {
		logger.info("@After...");
	}

	@BeforeClass
	public static void beforeClass() {
		logger.info("@BeforeClass...");
	}


	@AfterClass
	public static void afterClass() {
		logger.info("@AfterClass...");
	}

	@Test
	public void f1() {
		logger.info("test");
	}
}
