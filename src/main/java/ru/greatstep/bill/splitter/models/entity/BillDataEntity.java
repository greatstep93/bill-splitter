package ru.greatstep.bill.splitter.models.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "bill_data", schema = "user_data")
public class BillDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "create_user_id", nullable = false)
    private UserInfoEntity createUser;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "date", nullable = false)
    @ColumnDefault("CURRENT_DATE")
    private LocalDate date = LocalDate.now();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "bill_id")
    private Set<BillPositionEntity> billPositions = new LinkedHashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "bill_id")
    private Set<PositionConsumerEntity> positionConsumers = new LinkedHashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "bill_id")
    private Set<PayerEntity> payers = new LinkedHashSet<>();
}