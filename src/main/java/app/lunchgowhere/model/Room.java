package app.lunchgowhere.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true,
            fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    List<LocationSubmission> locationSubmissions;

    @Column(name = "is_active", nullable = false)
    Boolean isActive;

    //room owner
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    User roomOwner;

}
