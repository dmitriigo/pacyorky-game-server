package ee.pacyorky.gameserver.gameserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Date: 30.11.2021
 * Time: 16:30
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReportDto {
    private String message;
    private String targetId;
}
