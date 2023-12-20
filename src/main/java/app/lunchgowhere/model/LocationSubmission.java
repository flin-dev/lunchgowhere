package app.lunchgowhere.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@Table(name = "location_submission")
public class LocationSubmission extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", insertable = true, updatable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    Room room;

    @Column(name = "location", nullable = false)
    String name;

    @Column(name = "reason", nullable = true)
    String reason;

    @Column(name = "description", nullable = true)
    String description;

    @OneToOne(cascade = CascadeType.ALL)
    User summiter;

    //default false
    @Column(name = "selected", nullable = false, columnDefinition = "boolean default false")
    Boolean selected;
}
