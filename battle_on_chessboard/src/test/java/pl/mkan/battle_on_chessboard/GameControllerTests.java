package pl.mkan.battle_on_chessboard;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.mkan.battle_on_chessboard.controller.dto.Color;
import pl.mkan.battle_on_chessboard.controller.dto.GameDTO;
import pl.mkan.battle_on_chessboard.controller.dto.UnitDTO;
import pl.mkan.battle_on_chessboard.controller.rest.GameController;
import pl.mkan.battle_on_chessboard.persistence.model.UnitType;
import pl.mkan.battle_on_chessboard.service.GameService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameController.class)
public class GameControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @Test
    public void testStartNewGame() throws Exception {
        GameDTO gameDTO = new GameDTO(1L, "active", List.of(new UnitDTO(10L, UnitType.ARCHER.name(), Color.WHITE, 1, 1, "active", 0)));
        when(gameService.startNewGame()).thenReturn(gameDTO);

        mockMvc.perform(post("/api/game/new")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("active"));
    }

    @Test
    public void testGetUnits() throws Exception {
        UnitDTO unitDTO = new UnitDTO(1L, "ARCHER", Color.WHITE, 0, 0, "ACTIVE", 0);
        List<UnitDTO> units = List.of(unitDTO);
        when(gameService.getUnits(1L, Color.WHITE)).thenReturn(units);

        mockMvc.perform(get("/api/game/1/units")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("color", Color.WHITE.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].type").value("ARCHER"))
                .andExpect(jsonPath("$[0].color").value("WHITE"));
    }

    @Test
    public void testExecuteCommand() throws Exception {

        mockMvc.perform(post("/api/game/1/command")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("unitId", "1")
                        .param("color", Color.WHITE.name())
                        .param("command", "move")
                        .param("direction", "right")
                        .param("distance", "1"))
                .andExpect(status().isOk());

        verify(gameService, times(1)).executeCommand(1L, 1L, Color.WHITE, "move", "right", 1);
    }

    @Test
    public void testExecuteRandomCommand() throws Exception {
        mockMvc.perform(post("/api/game/1/unit/1/random-command")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("color", Color.WHITE.name()))
                .andExpect(status().isOk());

        verify(gameService, times(1)).executeRandomCommand(1L, 1L, Color.WHITE);
    }
}