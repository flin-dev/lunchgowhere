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

    @ManyToOne
    @JoinColumn(name = "room_id", insertable = false, updatable = false) // Maps to Room's ID
    Room room;

//    @Column(name = "room_id", nullable = false)
//    Long roomId;

    @Column(name = "location", nullable = false)
    String location;

    @Column(name = "reason", nullable = true)
    String reason;

    @OneToOne(cascade = CascadeType.ALL)
    User summiter;

    //default false
    @Column(name = "selected", nullable = false, columnDefinition = "boolean default false")
    Boolean selected;
}
