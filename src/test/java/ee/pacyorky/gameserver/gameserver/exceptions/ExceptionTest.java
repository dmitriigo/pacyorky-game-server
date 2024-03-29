package ee.pacyorky.gameserver.gameserver.exceptions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ee.pacyorky.gameserver.gameserver.controllers.v1.RoomsController;

@SpringBootTest
@ActiveProfiles("test")
class ExceptionTest {
    
    private MockMvc mockMvc;
    
    @Mock
    RoomsController roomsController;
    
    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(roomsController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }
    
    @Test
    void GlobalExceptionHandlerRuntimeExceptionTest() throws Exception {
        
        Mockito.when(roomsController.getGame(1L))
                .thenThrow(new RuntimeException("Unexpected Exception"));
        
        mockMvc.perform(get("/v1/rooms/get/1"))
                .andExpect(status().is(500))
                .andExpect(jsonPath("$.code").value("INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.message").value("Unexpected Exception"));
    }
    
    @Test
    void GlobalExceptionHandlerExceptionTest() throws Exception {
        
        Mockito.when(roomsController.getGame(1L))
                .thenThrow(new GlobalException("Global Exception", GlobalExceptionCode.CAPACITY_LIMIT_REACHED));
        
        mockMvc.perform(get("/v1/rooms/get/1"))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.code").value("CAPACITY_LIMIT_REACHED"))
                .andExpect(jsonPath("$.message").value("Global Exception"));
    }
}
