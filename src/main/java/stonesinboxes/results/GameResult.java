package stonesinboxes.results;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Duration;
import java.time.ZonedDateTime;

/**
 * Class representing the result of a game played by a specific player.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class GameResult {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * The name of the player1.
     */
    @Column(nullable = false)
    private String player1;



    /**
     * The number of steps made by the player1.
     */
    private int steps1;

    /**
     * The name of the player2.
     */
    @Column(nullable = false)
    private String player2;

    /**
     * The number of steps made by the player.
     */
    private int steps2;

    /**
     * Indicates whether the player won the game.
     */
    private boolean winner;

    /**
     * The duration of the game.
     */
    @Column(nullable = false)
    private Duration duration;

    /**
     * The timestamp when the result was saved.
     */
    @Column(nullable = false)
    private ZonedDateTime created;

    @PrePersist
    protected void onPersist() {
        created = ZonedDateTime.now();
    }

}