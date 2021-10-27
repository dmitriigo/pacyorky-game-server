package ee.pacyorky.gameserver.gameserver.exceptions;

import ee.pacyorky.gameserver.gameserver.controllers.GamesController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
public class ExceptionTest {

    private MockMvc mockMvc;

    @Mock
    GamesController gamesController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(gamesController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void GlobalExceptionHandlerRuntimeExceptionTest() throws Exception {

        Mockito.when(gamesController.getGame(1l))
                .thenThrow(new RuntimeException("Unexpected Exception"));

        mockMvc.perform(get("/v1/rooms/get/1"))
                .andExpect(status().is(500))
                .andExpect(jsonPath("$.code").value("INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.message").value("Unexpected Exception"));
    }

    @Test
    public void GlobalExceptionHandlerExceptionTest() throws Exception {

        Mockito.when(gamesController.getGame(1l))
                .thenThrow(new GlobalException("Global Exception", GlobalExceptionCode.CAPACITY_LIMIT_REACHED));

        mockMvc.perform(get("/v1/rooms/get/1"))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.code").value("CAPACITY_LIMIT_REACHED"))
                .andExpect(jsonPath("$.message").value("Global Exception"));
    }
}
