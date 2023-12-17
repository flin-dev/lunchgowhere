package app.lunchgowhere.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "location_submission")
public class LocationSubmission extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "room_id", nullable = false)
    Long roomId;

    @Column(name = "location", nullable = false)
    String location;

    @Column(name = "reason", nullable = true)
    String reason;

    @OneToOne(cascade = CascadeType.ALL)
    User summiter;
}
