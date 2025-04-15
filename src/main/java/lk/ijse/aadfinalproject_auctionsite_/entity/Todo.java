package lk.ijse.aadfinalproject_auctionsite_.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "todos")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String assignAdminEmail;

    private String completed;

    private LocalDate date;


    @PrePersist
    protected void onUpdate() {
        date = LocalDate.now();
    }




}
