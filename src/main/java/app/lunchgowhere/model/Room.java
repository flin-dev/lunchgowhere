package app.lunchgowhere.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "room")
public class Room extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "description", nullable = false)
    String description;

    @Column(name = "target_time", nullable = false)
    Date targetTime;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    List<LocationSubmission> locationSubmissions;

    @Column(name = "is_active", nullable = false)
    Boolean isActive;

    //room owner
    @OneToOne
    @JoinColumn(name = "owner_id")
    User roomOwner;

}
