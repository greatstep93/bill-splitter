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

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "payer", schema = "user_data")
public class PayerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "created_user_id", nullable = false)
    private UserInfoEntity createdUser;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "bill_id", nullable = false)
    private BillDataEntity billData;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "consumer_id")
    private Set<PositionConsumerEntity> positionConsumers = new LinkedHashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "payer_id")
    private Set<PositionPayerEntity> positionPayers = new LinkedHashSet<>();

}