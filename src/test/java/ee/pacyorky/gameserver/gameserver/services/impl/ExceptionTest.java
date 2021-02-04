package ee.pacyorky.gameserver.gameserver.services.impl;

import ee.pacyorky.gameserver.gameserver.controllers.GamesController;
import ee.pacyorky.gameserver.gameserver.exceptions.ExceptionFilter;
import ee.pacyorky.gameserver.gameserver.exceptions.GlobalException;
import ee.pacyorky.gameserver.gameserver.exceptions.GlobalExceptionHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExceptionTest {

    private MockMvc mockMvc;

    @Mock
    GamesController gamesController;
    @Autowired
    private ExceptionFilter exceptionFilter;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(gamesController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .addFilter(exceptionFilter, "/games/1")
                .build();
    }

    @Test
    public void GlobalExceptionHandlerRuntimeExceptionTest() throws Exception {

        Mockito.when(gamesController.getGame(1l))
                .thenThrow(new RuntimeException("Unexpected Exception"));

        mockMvc.perform(get("/games/1"))
                .andExpect(status().is(500))
                .andExpect(jsonPath("$.code").value("500"))
                .andExpect(jsonPath("$.message").value("Unexpected Exception"));
    }

    @Test
    public void GlobalExceptionHandlerExceptionTest() throws Exception {

        Mockito.when(gamesController.getGame(1l))
                .thenThrow(new GlobalException("Global Exception"));

        mockMvc.perform(get("/games/1"))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("Global Exception"));
    }
}
