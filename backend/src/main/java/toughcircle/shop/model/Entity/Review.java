package toughcircle.shop.model.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reviewId")
    private Long id;

    @NotNull
    private int rating;
    @NotNull
    private String comment;
    private LocalDateTime createAt;
    private String image;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}
