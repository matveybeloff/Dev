package org.example.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "school")
public class SchoolData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column(nullable = false)
    private String name;

    @Getter
    @Setter
    @Column(nullable = false)
    private String grades;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id", nullable = false)
    private DistrictData district;

    @Getter
    @Setter
    @OneToOne(mappedBy = "school", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private StatisticsData statistics;
}
