package me.kostevych.widgets;

import me.kostevych.widgets.controllers.WidgetController;
import me.kostevych.widgets.services.WidgetStoreService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
@AutoConfigureMockMvc
class WidgetsRestTests {

	@MockBean
	private WidgetStoreService widgetStore;

	@Autowired
	WidgetController widgetController;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void validationErrorTest() throws Exception {
		String widget = "{\"y\" : 10, \"height\":20, \"width\":20}";
		mockMvc.perform(MockMvcRequestBuilders.post("/widgets")
				.content(widget)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(content()
						.contentType(MediaType.APPLICATION_JSON));
	}


}
