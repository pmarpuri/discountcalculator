package ae.pmarpuri.discountcalculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTests {
	@Autowired
	private MockMvc mockMvc;

	@Test
	void testAddUsers() throws Exception {
		String userJson = "[{\"name\": \"Purushothma\",\"type\": \"EMPLOYEE\",\"registrationDate\": \"2020-01-01\"},{\"name\": \"Hrutvik\", \"type\": \"AFFILIATE\",\"registrationDate\": \"2019-05-14\"}]";

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/user/saveUsers").content(userJson)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
		String body = result.getResponse().getContentAsString();
		assertEquals("Users have been added successfully!", body);
	}
}
