package org.example.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "statistics")
public class StatisticsData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @OneToOne
    @JoinColumn(name = "school_id", nullable = false)
    private SchoolData school;

    @Getter
    @Setter
    @Column(nullable = false)
    private int students;

    @Getter
    @Setter
    @Column(nullable = false)
    private double teachers;

    @Getter
    @Setter
    @Column(nullable = false)
    private double calworks;

    @Getter
    @Setter
    @Column(nullable = false)
    private double lunch;

    @Getter
    @Setter
    @Column(nullable = false)
    private int computer;

    @Getter
    @Setter
    @Column(nullable = false)
    private double expenditure;

    @Getter
    @Setter
    @Column(nullable = false)
    private double income;

    @Getter
    @Setter
    @Column(nullable = false)
    private double english;

    @Getter
    @Setter
    @Column(nullable = false)
    private double read;

    @Getter
    @Setter
    @Column(nullable = false)
    private double math;
}
